<center><h3>MySQL成神之路</h3></center>

### 一、基础理论

#### 1.事物

##### a.MVCC(多版本并发控制)

##### b.ACID

##### c.一致性读

#### 2.范式设计

#### 3.索引设计

##### 1）索引分类：

- 聚簇索引

  特点是存储数据的顺序与索引一致，默认主键会创建聚簇索引，且一张表只允许存在一个聚簇索引。

- 二级索引

  又叫辅助索引，是除了聚簇索引以外的索引。

##### 2）常见索引查找算法

- 链表遍历  o(n)
- 二分查找  log(n)
- Btree查找  log(n)
- hash查找 o(1)

#### 4.2PC

#### 5.CAP

#### 6.BASE

#### 7.PAXOS

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

设置密码

```mysql
set password for 'test'@'192.168.1.1' = password('123456');#设置密码
update user set password = password('123456') where user = 'root';#更新user表
mysqladmin -u root -h 127.0.0.1   password '123456' #命令设置
```



查看权限：

```mysql
show grants for 'test'@'%';#指定用户
show grants;#当前用户
```

单一授权

```mysql
grant select on db_name.* to 'test'@'%' ;#所有表
grant select(id, age) on testdb.user_info to test@localhost; #指定字段
```

授权并创建用户

```mysql
grant all privileges on *.* to 'test'@'%' identified by '123456';
```

撤销权限

```mysql
revoke all on *.* from test@'%';
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

场景从服务器A迁移到服务器B(**主从复制**)

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

5.服务器B上执行一次全量恢复

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

10.主从复制常见问题

- **lave_IO_Running 为connecting**（网络不通、密码不对、pos不对、用户权限不足）

#### 7.Mysql线上升级

如5.6 一主一从升级到5.7

法一：

1.安装5.7版本的数据库一主一从

2.按**6.在线迁移mysql**方式把旧库数据迁移到新库(这时两台新库为slave)

3.把新库中一台设置为master

法二：

在旧机器上安装mysql5.7，把5.7的数据和日志指向原5.6的数据和日志。

注：在bin/目录下有个mysql_upgrade -s可升级数据字典,5.6的数据字典与5.7的不一样

#### 8.备份与恢复

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
#下载解压
wget http://www.percona.com/downloads/XtraBackup/XtraBackup-2.1.9/binary/Linux/x86_64/percona-xtrabackup-2.1.9-744-Linux-x86_64.tar.gz
#如果执行innobackupex出错
yum install perl-Time-HiRes
```

**工具**：

- xtrabackup：热备份innodb表数据工具，不能备份其它类型表，也不能备份数据表结构
- innobackupex：可备份与恢复MyISAM表及数据表结构

**xtrabackup原理**：

加载数据到内存->备份数据到backup datafile,这期间产生的数据操作会记录到Xtrabackup.log中，与mysql自己的Redo.log(DML产生日志)一样

**innobackupex备份过程**：

1.start xtrabackup_log #redo位置

2.copy .ibd; ibdata1 #会比较page(内存blog块)首尾日志序列号(LSN)是否一致

3.FLUSH TABLES WITH READ LOCK 锁表(MyISAM)

4.copy FRM; MYD; MYI; misc files

5.Get binary log position （LSM）

6.UNLOCK TABLES

7.stop and copy xtrabackup_log

```shell
innobackupex \
	--defaults-file=/tmp/u01/my3306/conf/my.cnf \
	--user=root \
	--password='123456' \
	--socket=/tmp/u01/my3306/run/mysql.sock \
	--no-timestamp \
	/tmp/u01/backup/xtrabackup
```



**innobackupex恢复原理**：

先应用日志Xtrabackup.log(同redo.log),再拷贝备份数据到数据库目录，再加载到内存

```shell
innobackupex \
	--apply-log \
	--defaults-file=/tmp/u01/backup/xtrabackup/backup-my.cnf \
	--user=root \
	--password='123456' \
	/tmp/u01/backup/xtrabackup
#最后的目录与备份时的目录相同，恢复完成后拷贝到mysql的data数据目录下
```

注：备份之前和备份过程中产生的数据用innobackupex恢复，备份之后产生的数据，用mysql binlog命令恢复

**mysql binlog恢复**：

```shell
#查看
mysqlbinlog --no-defaults my3306/log/binlog/binlog.000004 --start-position="794" --stop-position="1055" | more 
#登陆后查看（推荐）
show binlog events in 'binlog.000004' 
#恢复
mysqlbinlog \
	--no-defaults my3306/log/binlog/binlog.000005 \
	--start-position="1795" \
	--stop-position="1857" | \
    mysql -uroot -p123456 
```

#### 9.MySQL锁机制与事物控制

##### 1.执行语句被阻塞如何分析

1）查询条件是否是主键

2）事物的隔离级别

3）是否有索引，是否是唯一索引

4）查看sql语句的执行计划

##### 2.事物

概念：

​	事物定义了一个服务操作序列，由服务保证这些操作在多个客户并发访问和服务器出现故障情况下的原子性。数据库中发生了DML或是DDL都会发生事物。

属性：

- **A**（Atomicity）原子性：整个事物的操作要么全部完成，要么全部不完成。（由数据库Redo和Undo实现）
- **C**（Consistency）一致性：事物开始之前和结束之后数据的完整性没有被破坏，数据库从一种状态到另一种状态。（Undo）
- **I**（Isolation）隔离性：一个事物的操作对其它事物不可见。（Lock）
- **D**（Durability）持久性：事物完成以后，该事物对数据库的更改都会保存在数据库中。（Redo）

##### 3.并发会存在的问题

- 脏读（dirty read） ：事物A读取了事物B修改但未提交的数据，且B回滚了。
- 不可重复读 （unrepeatable read）：会话A读取了会话B修改且提交的数据，相同的条件导致两次读取结果不一样。
- 幻读（phantom read）：会话A读取了会话B修改且提交的数据，相同的查询，。两次查询的结果条数不一样

##### 4.事物的隔离级别（由低到高）

- Read uncommitted （读未提交）：最低的隔离级别。一个事务可以读取另一个事务并未提交的更新结果。

- Read committed（读提交）：一个事务的更新操作结果只有在该事务提交之后，另一个事务才可以的读取到同一笔数据更新后的结果。【大部分数据库默认】
- Repeatable read （可重复读）：整个事务过程中，对同一笔数据的读取结果是相同的，不管其他事务是否在对共享数据进行更新，也不管更新提交与否。【MySQL默认，生产改成读提交】
- Serializable（串行）：最高隔离级别。所有事务操作依次顺序执行。注意这会导致并发度下降，性能最差。

注：这四个级别可以逐个解决脏读 、不可重复读 、幻读这几类问题。

##### 5.图表展示

![](https://raw.githubusercontent.com/aiceflower/assets/master/img/mysql/tx_isolatied.png)

##### 6.编程tip

- 不要在循环中提交
- 过程中不要有DDL（DDL前后有隐匿commit）
- 不要使用自动提交
- 不使用自动回滚
- 大事物折成小事物

##### 7.不同数据库的锁实现

- InnoDB：行级锁
- MyISAM：表级锁
- Oracle：行级锁
- Sql Server：行级锁，行太多会升级为表锁

##### 8.Lock锁与Latch锁

- 对象：事务/线程
- 保护：数据库对象/内存结构对象
- 持续时间：长/短
- 模式：表锁行锁/互斥
- 死锁：有/无

##### 9.InnoDB中的锁的模式

- 意向共享锁（IS）: 
- 意向排它锁（IX）: 
- 行级共享锁（S）: select * from t1 where id =1 lock in share mode;
- 行级排它锁（X）:  select * from t1 where name ='C' for update; #name上有普通索引

##### 10.InnoDB锁的兼容性

![](https://raw.githubusercontent.com/aiceflower/assets/master/img/mysql/INNODB_LOCK.png)

##### 11.加锁分析

```mysql
update t1 set name = 'z3' where id = 1;
#在表上加了意向排它锁，行上加行级排它锁
select * from t1 where id = 1;
#在表上加意向共享锁，在行上加MDL，语句后加 for update(排它)，lock in share mode(共享)
#为什么需要意向锁？
#假设现在要删除一个表，但表有一万行数据且正在修改第一万行，如果没有意向锁，那扫描到最后一行发现有锁，不能删除报错。如果有意向锁，删除时发现表上有锁，就报错节省时间。
#每个索引上的锁，最终会加到主键上的行排它锁。
```

##### 12.InnoDB行锁范围

- Record Lock 行记录锁
- Gap Lock 间隙锁，在索引记录间隙上的锁，或是第一条索引记录之前和最后一条索引记录之后上的间隙锁
- Next-key Lock 下一键锁，上两种的组合锁

注：Gap Lock锁需要Mysql默认隔离级别RR，每两条数据之间都会加锁，这两条数据之间不能插入数据，能解决幻读不让在行与行之间插入。

##### 13.MDL(metadata lock)

mysql的select操作会加mdl，保护select语句不被ddl操作。未设置自动提交

解决MDL：

减少ddl高并发、线上db不轻易alter table、干掉ddl会话(show processlist----kill id)

##### 14.死锁的原因和分析

**1.产生回路**：两个或两个以上的事务执行过程中，分别持有一把锁，然后加另一把锁(AB---BA),产生死锁

**2.加锁顺序不一致**：两个或两个以上的事物并发执行，因争夺资源而造成一种相互等待，产生死锁

```mysql
#回路模拟
#session1
begin;
update t1 set name = 'a' where id = 1;
#session2
begin;
update t1 set name = 'b' where id = 1;
#session1
update t1 set name = 'c' where id = 2;
#session2
update t1 set name = 'd' where id = 1; #此时产生死锁
#设置innodb_print_all_deadlocks=on可把死锁信息保存在日志中
```

##### 16.减少死锁的方法

- 自动死锁检测，优先回滚小事务
- 超时设置(innodb_lock_wait_timeout)
- 尽快提交事务
- 加for update, lock in share mode 时，最好降低隔离级别RC
- 事务操作多个表时，每个事务的操作顺序保持一致
- 通过索引优化SQL效率，降低死锁概率

##### 17.与锁相关的表

![](https://raw.githubusercontent.com/aiceflower/assets/master/img/mysql/lock_table.png)

##### 18.优化

###### 1.IO调度算法

- NOOP算法： 实现了最简单的FIFO队列，所有IO请求大致按照先来后到的顺序进行操作。
- CFQ算法：对IO地址进行分组排序，把相近的放在一起，提高吞吐量。不是先来后到。
- Deadline算法：在CFQ基础上解决了请求饿死的情况，还额外为读请求和写请求提供了FIFO队列。
- Anticipatory算法：

###### ~~2.操作系统优化~~

1.关闭swap，内核参数/etc/sysctl.conf 中添加vm.swapplness=10，默认是60

2.调度算法设置deadline，echo deadline > /sys/block/.../queue/schedule

###### 3.数据库优化

**实例优化**

```mysql
innodb_buffer_pool_size #数据放到内存中处理，减少io，设置物理内存的60%-80%
innodb_thread_concurrency #设置线程并发，设置cpu的processor数少几个
query_cache_type=0 #是否开启查询缓存，修改表会清空
query_cache_size=0 #查询缓存大小
max_user_connections #最大连接数，与应用相关
interactive_timeout #交互超时,120s
wait_timeout #等待超时，jdbc连接等，120s
innodb_io_capacity#innodb的io容量，设置iops的75%左右
innodb_flush_log_at_trx_commit=1#
sync_binlog=1 #这两个是一个commit就写日志
innodb_log_file_size #日志文件大小，SSD建议4-8G，SAS建议1-2G
innodb_flush_method=O_DIRECT #直接刷新到磁盘
innodb_flush_neighbors #SSD设置0顺序访问，SAS设置1随机访问
tx_isolation #设置RC提高并发，默认RR
#安全相关
skip_name_resolve=on #禁止DNS解析，只允许通过ip认证
skip_networking=on #关闭TCP/IP网络连接服务，只允许socket(默认连接方式)方式进行连接
./configure --with-yassl #Mysql自带的yaSSL 
./configure --with-openssl #三方OpenSSL
#以上两个需要同时设置 ssl_ca=cacert.pem ssl_cert=server-sert.pem ssl_key=server-key.pem
mysqld_safe --skip-grant-tables #绕过权限表认证，root密码丢失时可用，
./configure --disable-grant-options #防范上术漏洞(非法用户登陆系统以上术方式重启服务)
```

**SQL优化**

**高效sql**

![](https://raw.githubusercontent.com/aiceflower/assets/master/img/mysql/%E9%AB%98%E6%95%88sql.png)

**索引设计**

覆盖索引：

- 查询谓词(**查询条件**)都能够通过index进行扫描
- 排序(group by、order by)谓词(**排序，分组条件**)都能够通过index进行扫描
- index包含了查询所需要的所有字段

不能使用索引：

- 不给选择充低的字段建索引，如性别
- 联合索引中：不要第一个索引列使用范围查询，第一个查询条件不是最左列 【index_name(age,name)】
- like条件不以%开始
- 两个独立索引，一个检索一个排序，建议做组合排序
- 不要在索引字段上使用函数操作
- 不使用外键索引