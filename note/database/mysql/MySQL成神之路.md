<center><h3>MySQL成神之路</h3></center>

### 一、基础理论

#### 1.MVCC

#### 2.ACID

#### 3.范式设计

#### 4.索引设计

##### 1）索引分类：

- 聚簇索引

  特点是存储数据的顺序与索引一致，默认主键会创建聚簇索引，且一张表只允许存在一个聚簇索引。

- 二级索引

  又叫辅助索引，是除了聚簇索引以外的索引。

2）常见索引查找算法

- 链表遍历  o(n)
- 二分查找  log(n)
- Btree查找  log(n)
- hash查找 o(1)

#### 5.2PC

#### 6.CAP

#### 7.BASE

#### 8.PAXOS

### 二、MySQL相关技能

#### 1.高可用

为什么要高可用

传统主从复制

MHA

#### 2.备份

备份原理

逻辑备份

物理备份

备份工具

数据迁移

#### 3.监控

监控指示：LOAD/CPU/TPS/QPS/IOWAIT/CONN，ZABBIX监控系统

#### 4.高性能

深入理解系统结构

SQL优化

SQL审计

### 三、mysql操作

#### 1.启动

mysql.server  -->  mysqld_safe【启动且监控mysqld】 -->  mysqld

mysqld_mutil（启动多个）

```sql
mysql.server start
/etc/init.d/mysqld start #同第一种一样，只是把上面的拷贝到这个目录里重命名了下
service mysqld start
mysqld --defaults-file=my.cnf &
mysqld_safe --defaults-file=my.cnf & #推荐这种启动
mysqld_mutil start #多实例启动，推荐多个单启动
```

#### 2.关闭

```shell
mysqladmin --socket=my3306/run/mysql.sock --port=3306 shutdown & #--socket 可换成 -S
```

#### 3.账户权限

##### 1)创建用户

```mysql
#-----方式一-------
insert into user(host,user,password) values('127.0.0.1','test',password('123456'));
flush privileges;
#这种方式需要flush，它是直接把数据做了持久化，缓存是不知道的，flush是把user中的数据重新加载进缓存。
#-----方式二-------
create user test@'127.0.0.1' identified by '123456';

```

##### 2）授权

查看权限：

```mysql
show grants for 'test'@'%';
```

单一授权

```mysql
grant select on db_name.* to 'test'@'%' ;
```

授权并创建用户

```mysql
grant all privileges on *.* to 'test'@'%' identified by '123456';
```

##### 3）权限等级

1.核心开发权限 (一般给 insert delete update select)

2.管理权限----表级 (create drop lock)

3.管理权限----server级别 (dba)

##### 4)删除用户

```mysql
drop user test@'127.0.0.1'; 
```

#### 4.MYsql数据库安全配置

- 禁用多余的管理员账号
- 删除掉mysql.db表数据 #内有test库权限，所有用户都能操作test库
- 删除test库
- 修改管理员账户名
- 密码复杂度要求
- 权限最小化

#### 5.线上表删除

- 查看表： show tables;
- 检查表是否被访问：show processlist;
- 重命名临时表：rename table test to test_bak;
- 备份临时表：mysqldump -h127.0.0.1 -uroot db_name table_name > /tmp/bak.sql
- 一段时间未出问题后删除临时表：drop table test_bak;

#### 6.在线迁移mysql

场景从服务器A迁移到服务器B

1.从服务器A创建一个复制账号

```mysql
grant replication slave,replication client on *.* to 'repl' identified by '123456';	
```

2.服务器A和服务器B设置不同的server_id

```mysql
show variables like '%server_id%' #查
set global server_id=102 #改
 #永久改的话改 my.cnf中的server_id
```

3.服务器A执行一次完整的逻辑备份

```mysql
mysqldump --single-transaction --master-data=2 -uroot xtj > my3306/bak/xtj.sql
#--single-transaction 不会锁表	
#--master-data=2 做主从复制的时候需要加
```

4.从服务器A拷贝备份到服务器B

5.服务器B上执行一次命题回复

```mysql
create database xtj default character set utf8;
use xtj;
source my3306/bak/xtj.sql
```

6.服务器B上执行change master 设置主从复制

```mysql
#服务器B上执行
change master to
master_host='127.0.0.1',
master_port=3306,
master_user='repl',
master_password='123456',
master_log_file='binlog.000004',
master_log_pos=2475

#grep MASTER_LOG_FILE /tmp/u01/my3306/bak/xtj.sql 查找master_log_file和master_log_pos
```

7.服务器B上执行start slave 启动复制

```mysql
start slave
show slave status \G; #查看下状态，如下为ok
Slave_IO_Running: Yes
Slave_SQL_Running: Yes
```

 8.服务器A上设置为read only

```mysql
show variables like '%read_only%';
set global read_only=on; #on改成1也可以
```

注：read_only对super用户无效

9.服务器B设为主库(服务器B把服务器A转过来的binlog消化完)

#### 7.Mysql线上升级

如5.6 一主一从升级到5.7

法一：

1.安装5.7版本的数据库一主一从

2.按**6.在线迁移mysql**方式把旧库数据迁移到新库(这时两台新库为slave)

3.把新库中一台设置为master

法二：

在旧机器上安装mysql5.7，把5.7的数据和日志指向原5.6的数据和日志。

注：在bin/目录下有个mysql_upgrade -s可升级数据字典,5.6的数据字典与5.7的不一样

#### 8.备份

##### 1.分类

**逻辑备份**：将数据库内容转存到文本中，mysql处于运行状态，可以本地或远程备份。

**物理备份**：mysql数据库文件的二进制副本，恢复也必须到相同引擎，比逻辑快。包括<font color="blue">热备份</font>(mysql运行时备份)和<font color="blue">冷备份</font>(关闭mysql后备份)

##### 2.备份工具

- mysqldump (逻辑，官方)

```mysql
mysqldump -h127.0.0.1 -P3306 -p123456 --single-transaction --master-data=2 -uroot xtj > my3306/bak/xtj.sql
# -A 导所有库 --help查看参数
#流程为：打开general.log->执行mysqldump->分析general.log->关闭general.log
#打开general_log=On可观察
```

- mysqldumper (逻辑，社区多线程)

下载mysqldumper，编译安装

```mysql
mysqldumper \
	--user=root \
	--passsword='123456' \
	--regex '^(?|(mysql))' \
	--outputdir=/apps/backup/ \
	--compress \
	--verbose=3 \
	--logfile=/apps/backup/mydumper.sql
#compress 压缩 verbose=3记录所有日志，默认为2记录warnning
```

- Xtrabackup (物理 热备)

**安装**：

```shell
 yum install percona-xtrabackup
```

**工具**：

- xtrabackup：热备份innodb表数据工具，不能备份其它类型表，也不能备份数据表结构
- innobackupex：可备份与恢复MyISAM表及数据表结构

**xtrabackup原理**：

加载数据到内存->备份数据到backup datafile,这期间产生的数据操作会记录到Xtrabackup.log中，与mysql自己的Redo.log(DML产生日志)一样

**innobackupex备份过程**：

1.start xtrabackup_log #redo位置

2.copy .ibd; ibdata1 #会比较page(内在blog块)首尾日志序列号(LSN)是否一致

3.FLUSH TABLES WITH READ LOCK 锁表(MyISAM)

4.copy FRM; MYD; MYI; misc files

5.Get binary log position （LSM）

6.UNLOCK TABLES

7.stop and copy xtrabackup_log

```shell
innobackupex \
	--default-file=my3306/conf/my.cnf \
	--user=root \
	--password='123456' \
	--socket=my3306/run/mysql.sock \
	--no-timestamp \
	/apps/backup/xtrabackup
```



**innobackupex恢复原理**：

先应用日志Xtrabackup.log(同redo.log),再拷贝备份数据到数据库目录，再加载到内存

```shell
innobackupes \
	--apply-log \
	--defaultsfile=/apps/backup/xtrabackup/backup-my.cnf \
	--user=root \
	--password='123456' \
	/apps/backup/xtrabackup
```

注：备份之前和备份过程中产生的数据用innobackupex恢复，备份之后产生的数据，用mysql binlog命令恢复

**mysql binlog恢复**：

```shell
#查看
mysqlbinlog --no-defaults my3306/log/binlog/binlog.000004 --start-position="794" --stop-position="1055" | more 
#恢复
mysqlbinlog \
	--no-defaults mysql-bin.000002 \
	--start-position="794" \
	--stop-position="1055" | \
    mysql -uroot -p123456 
```



#### 2.常用命令

查看mysqld调用my.cnf顺序

```shell
mysqld --verbos --help |grep my.cnf
```

查看当前用户

```mysql
select user();
```

创建数据库

```mysql
create databases testdb default character set utf8;
```

查看表是否正在被访问

```mysql
show processlist;
```

查看表

```mysql
show tables from db_name like '%user%';
```

查看存储引擎状态

```mysql
show engine innodb status \G;
```

binlog相关看作

```mysql
#查看所有binlog
show master logs;
#flush刷新log日志，自此刻开始产生一个新编号的binlog日志文件
flush logs;
#查看master状态
show master status;
#最新一个binlog日志的编号名称，及其最后一个操作事件pos结束点(Position)值
#查看binlog日志信息
mysqlbinlog binlog.000004 
#或
show binlog events in 'binlog.000004' \G
```



