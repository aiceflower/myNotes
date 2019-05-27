<center><h3>MySQL成神之路</h3></center>

### 一、基础理论

#### 1.事物

##### a.MVCC(多版本并发控制)

**是什么？**
	MVCC英文全称Multi-Version Concurrency Control多版本并发控制。在我看来如果把乐观锁看作接口的话，那MVCC就是该接口的实现而已。
	MVCC相对于传统的基于锁的并发控制特点是读不上锁，对于读多写少的场景大提高了并发。
	我们知道乐观锁主要依靠版本控制，消除锁定，Mysql写操作会加排它锁二者相互矛盾。从某种意义上说Mysql的MVCC并非真正意义上的MVCC，它只是借用了MVCC的名号实现了读的非阻塞而已。
	InnoDB中的MVCC主要是为RR隔离级别做的。
**作用？**
	锁机制可以控制并发操作，但系统开销太大，而MVCC可以在大多数情况下代替行级锁，降低系统开销。
	MVCC是通过保存数据在某个时间点的快照来实现的。

##### b.ACID

​	事务的四种属性，原子性，一致性，隔离性，持久性。

##### c.一致性读

​	一致性读，又称为快照读。使用的是MVCC机制读取undo中的已经提交的数据。所以它的读取是非阻塞的。	

不过也有特例：在本事务中修改的数据，即使未提交的数据也可以在本事务的后面部分读取到。

不同事务隔离级别的一致性读。

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

3）索引优缺点

- 优：提高查询效率
- 缺：插入、删除效率变低，重建索引文件。增加内存。

#### 4.2PC

#### 5.CAP

#### 6.BASE

#### 7.PAXOS

#### 8.其它

##### 1）MySQL连接方式

- **TCP/IP**：mysql指定-h参数后使用该方式连接
- **socket**：mysql命令不指定-h参数时使用该方式连接

##### 2）Mysql逻辑架构

**架构图**

![](https://raw.githubusercontent.com/aiceflower/assets/master/img/mysql/mysql_architecture.png)

**4层逻辑结构**	

- 连接层：客户端和连接服务，socket|tcp/ip通信。连接处理，授权认证等。
- 服务层：完成大多数的核心服务功能。缓存查询、SQL分析优化、跨存储引擎的功能过程函数等。
- 引擎层：存储引擎真正负责了数据的存储和提取。服务器通过api与各存储引擎进行通信。
- 存储层：将数据存储在运行于裸设备的文件系统之上，并完成与存储引擎的交互。

##### 3）MySQL概念

- **SQL**，结构化查询语言(Structured Query Language)。客户端使用SQL来操作服务器。
- Mysql设计之初的**目标**：成为高性能、普通用户支付得起的数据库服务器和工具。
- Mysql的**使命**：为所有人贡献一个经济实惠并且性能卓越的数据库软件。
- Mysql的**独到之处**：插件式存储引擎，存储类别的粒度小到每个表，可最大程度利用各存储引擎的优点。<span style="color:blue">插件式存储引擎架构将查询处理和其它的系统任务以及数据的存储提取相分离。</span>可根据业务需求和实际的需要选择合适的存储引擎。
- 三种觉见的**数据库系统**：面向对象型、关系型、对象关系型
- DBMS**功能模块**：网络客户端连接(客户端访问)、查询解析和优化（查询接口、查询处理、查询优化、查询执行)、存储引擎(文件读取)
- mysql**解析器**由词法分析和语法分析两部分组成。
- 查询**优化器**：通过可供选择的执行计划，找到最低估算开销的执行计划，来优化一条SQL语句。(基于开销的优化)
- **Mysql账号**由用户名和主机共同组成（'root'@'127.0.0.1'）。同一个账号，不同的主机登陆，MySQL服务认为是不同的连接请求。
- **具体优先原则**：服务器把user读到内存后，根据Host和user字段的具体程度重新对host,user字段组成的记录进行排列，然后服务器会根据最先匹配的一条记录去允许登陆连接。
- MySQL采用单向复制的方式，不支持多主服务器的复制功能。

##### 4）MySQL积累

- mysql的utf8(每个字符最多三个字节)不是真正的utf-8(每个字符最多4个字节)，<span style='color:red'>使用utf8mb4=utf8</span>
- mysql数据库的原则，如果数据是重复的插入是插在后面。
- 客户端  -->  发送sql命令-->查询接口-->查询处理/优化-->查询执行-->读写文件-->查询结果
- Mysql权限检查顺序：user(全局) -->  db(库) -->  tables_priv(表)  -->  columns_priv(列)  -->  procs_priv(函数)
- 在数据库中所有的字符串类型，必须使用<span style="color:red">单引</span>，不能使用双引
- mysql默认开启事物自动提交，可使用begin;开启一个事务
- mysql默认安装完成后表名区分大小写，字段名不区分
- InnoDB 引擎在**加锁**的时候，只有通过索引进行检索的时候才会使用行级锁，否则会使用表级锁。这个索引一定要创建成唯一索引，否则会出现多个重载方法之间无法同时被访问的问题.
- 上千行以内的数据排序操作可放在内存中，服务器可以有很多台，但DB只有几台。
- show open tables; 查看表使用和锁情况
- <span style='color:red'>innodb索引失效会使行锁变表锁</span>

-------<span style='color:red'>索引相关</span>------------

-------**join优化**------------

- 尽可能减少join语句中的循环总次数：永远用小结果驱动大结果
- 优先优化嵌套循环的内层循环
- 保证join语句中被驱动表上join条件已经被索引

- 索引建立在经常查询的字段中

- 左右连接，相反建索引(左连接建立右表索引，因为左连接左表数据都存在)
- 极端条件下可调大joinbuffer大小

-------**其它优化**------------

- 组合索引第一个可单独作为条件查询(会使用索引)，后面的不可单独做为条件查询(不会使用索引)
- 使用**or**时所有条件都有索引才会使用索引
- **is null**可能会使用索引，= null不会使用
- **>=** 相对于 > 会做两次扫描
- group by 基本上都需要排序，会有临时表产生
- 对于单键索引，选择过虑性更好的索引
- 在选择组合索引时过虑性最好的字段中，位置越前越好

##### 5）SQL语句分类

1. DDL（Data Definition Language）：数据定义语言，用来定义数据库对象：库、表、列等；创建、删除、修改：库、表结构！！！
2. DML（Data Manipulation Language）：数据操作语言，用来定义数据库记录（数据）；增、删、改：表记录
3. DCL（Data Control Language）：数据控制语言，用来定义访问权限和安全级别；
4. DQL（Data Query Language）：数据查询语言，用来查询记录（数据）。

##### 6）查询语句处理

**语句形式**

```mysql
SELECT DISTINCT 
	<select_list>
FROM
	<left_table> <join_type>
JOIN <right_table> on <join_condition>
WHERE
	<where_condition>
GROUP BY
	<group_by_list>
HAVING
	<having_condition>
ORDER BY
	<order_by_condition>
LIMIT <limit_number>
```

**处理过程**

1.语句合法性检查：对SQL语句的语法进行检查、看其是否符合语法规则。

2.语义检查：对语句中的字段，表等进行检查

3.获得对象解析锁：保证数据的一致性，防止其它用户改变(保证大量并发情况下的数据完整性)

4.数据访问权限核对：检查用户是否有这个数据（库、表、列）的访问权限。

**机器读取语句过程**

```mysql
1 FROM <left_table>
2 ON <join_condition>
3 <join_type> JOIN <right_table>
4 WHERE <where_condition>
5 GROUP BY <group_by_list>
6 HAVING  <having_condition>
7 SELECT 
8 DISTINCT <select_list>
9 ORDER BY <order_by_condition>
10 LiMIT <limit_number>
```

**SQL解析**

![](https://raw.githubusercontent.com/aiceflower/assets/master/img/mysql/sql_parse_flow.png)

##### 7）MySQL系统和核心库

- 网络连接和网络通信协议子系统
- 线程、进程和内存分配子系统
- 查询解析和查询优化子系统
- 存储引擎接口子系统
- 各类存储引擎子系统
- 安全管理子系统
- 日志子系统
- 其他子系统 -- 如复制功能、错误功能
- mysys核心库文件

##### 8）图

**框架和模块图**

![Mysql框架和模块](<https://raw.githubusercontent.com/aiceflower/assets/master/img/mysql/mysql_frame_and_module.png>)

**Mysql查询执行过程**

![Mysql查询执行过程](https://raw.githubusercontent.com/aiceflower/assets/master/img/mysql/mysql_query_executing_process.png)

##### 网络通信图示

![](<https://raw.githubusercontent.com/aiceflower/assets/master/img/mysql/mysql_network_communications.png>)

**MySQL登陆认证**

![](<https://raw.githubusercontent.com/aiceflower/assets/master/img/mysql/mysql_login_check.png>)

**安全级别**

![](<https://raw.githubusercontent.com/aiceflower/assets/master/img/mysql/security_level.png>)



##### 9）文件目录说明

**源码部分目录结构简介**

- **BUILD目录**：编译和安装脚本目录，执行compile-pentium-debug相当于./configure && make && make install
- **client目录**：mysql常用命令和客户端工具
- **storage目录**：Mysql各类存储引擎
- **mysys目录**：MySQL库函数文件，其实是一个大杂烩，包括文件操作、内存分配、线程控制排序算法、hash函数等。
- **sql目录**：Mysql服务器内核最为核心和重要的目录，包括线程、查询解析、查询优化，存储引擎接口等。
- **vio目录**：Virtual I/O 主要用到处理各种网络协议

-------以下是开源社区贡献代码

- **regex目录**：执行正则匹配REGEXP需要这个库支持
- **dbug目录**：调试库 使用with-debug参数编译会显示dbug输出
- **strings目录**：
- **zlib目录**：

**mysql数据文件及目录简介**

- **my.cnf**：mysql配置文件
- **.frm**：表元数据文件，描述了表的定义，
- **.ibd**：Innodb存储引擎的MySQL表数据文件、索引文件
- **.MYD**：MYISAM存储引擎的表数据文件
- **.MYI**：MYISAM存储引擎的表索引文件
- **db_name/db.opt**：记录了当前库的字符集和核对规则
- **mysql_install_db**：Mysql初始化脚本
- **mysql.sock**：socket，发起本地连接时可用
- **ibdata[x]**：innodb内部数据
- **ib_logfile[x]**：innodb内部日志
- **binlog.index**：管理主库上的binlog文件，只有在此文件中出现的日志文件才被Mysql承认并使用
- **relay.index**：功能同binlog.index，管理从库的relay log文件
- **master.info**：从库上文件，由I/O线程更新，记录连接主库时的相关信息
- **relay-log.info**：从库上文件，由SQL线程更新，记录SQL线程读取和执行Relay Log当前状态
- **bin目录**：存放可执行文件
- **data目录**：存放数据库
- **share目录**：数据库，表初始化脚本及字符集、语言等信息。
- **include目录**：头文件
- **binlog目录**：存放myhsql的binlog日志
- **iblog目录**：innodb数据存放目录，初始化时指定的

##### 10）Mysql权限表(mysql库中)

- **user**：超级 用户表，包括Mysql操作的所有权限。也用其来进行登陆验证。
- **db**：针对数据库的权限，权限范围为该库中所有对象(包括表和字段)。对指定host的权限操作。
- **tables_priv**：数据库中指定表的权限，细化到表
- **columns_priv**：数据库中指定字段的权限，细化到表中字段
- **procs_priv**：该表对某一个单独的存储过程或函数进行权限管理。

##### 11）MySQL核心算法

**Bitmaps -- 位图(mysys/my_bitmap.c包含操作位图的函数)**

​	比特位图使用少量空间，提供大量信息。用一个比特位代表true和false值。

**表连接缓存工作原理**

SELECT * FROM t1,t2,t3 where t1.col3 = t2.col2 and t3.cal4 < 30;

```c
//未使用冲存算法
while (t1_rec in t1){ //表t1中的每个记录
	while (t2_rec in t2 and t2_rec.col2 = t1_rec.col3){
	/*对于表t2中的每个记录且该记录中col2和tl_col中的col3相等*/
		while (t3_rec in t3 and t3_rec.col4 < 30){
			put the (t1_rec, t2_rec, t3_rec) combination to output buffer
		}
	}
}
//其中t1和t2组合后多次扫描t3,t3扫描有很多是重复的
//使用缓冲算法
while (t1_rec in t1){ //表t1中的每个记录
	while (t2_rec in t2 and t2_rec.col2 = t1_rec.col3){
		put (t1_rec, t2_rec) into the buffer
		if (buffer = full)
			call flush_buffer();
	}
}
flush_buffer(){
	while (t3_rec in t3 and t3_rec.col4 < 30){
		while (rec in buffer){
			put the (t1_rec, t2_rec, t3_rec) combination to output buffer
		}
	}
}
```

注：join_buffer_size为Mysql为每个连接缓冲分配的大小，explain输出中Using join buffer表示使用缓存算法

**Mysql排序实现**

排序不仅仅用于order by ,同样group by 也会用到排序，Mysql有两类方式进行排序：

- **作用 range、ref、index读写方式**：explain的输出 range、ref、index是描述对索引的读取方式。这类方式获得的输出都是按索引的顺序排序的
- **filesort排序算法**：将一组记录按照快速排序放入到内存缓存，然后这几个内存缓存按合并算法排序 @@sort_buffer_size

***执行排序的三种方法***：

- **使用已排序索引**：Explain不提及任何filesort #有索引且排序的是索引字段
- **在表单上使用filesort**：using filesort #如果是多表联合，先单表filesort再对排序后进行join
- **将join结果先放入临时表，然后使用filesort**：using templorary; using filesort

12）

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

##### 3）权限等级

1.核心开发权限 (一般给 insert delete update select)

2.管理权限----表级 (create drop lock)

3.管理权限----server级别 (dba)

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

#### 6.Mysql线上升级

如5.6 一主一从升级到5.7

法一：

1.安装5.7版本的数据库一主一从

2.按**6.在线迁移mysql**方式把旧库数据迁移到新库(这时两台新库为slave)

3.把新库中一台设置为master

法二：

在旧机器上安装mysql5.7，把5.7的数据和日志指向原5.6的数据和日志。

注：在bin/目录下有个mysql_upgrade -s可升级数据字典,5.6的数据字典与5.7的不一样

#### 7.备份与恢复

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
mysqlbinlog -vv binlog.00004 | more
#登陆后查看（推荐）
show binlog events in 'binlog.000004' 
#恢复
mysqlbinlog \
	--no-defaults my3306/log/binlog/binlog.000005 \
	--start-position="1795" \
	--stop-position="1857" | \
    mysql -uroot -p123456 
```

#### 8.MySQL锁机制与事物控制

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

##### 18.乐观锁与悲观锁

**乐观锁**

​	乐观锁是指操作数据库时(更新操作)，想法很乐观，认为这次的操作不会导致冲突，在操作数据时，并不进行任何其他的特殊处理（也就是不加锁），而在进行更新后，再去判断是否有冲突了。乐观锁不是数据库自带的，需要我们自己去实现。

​	实现方式：在表中的数据进行操作时(更新)，先给数据表加一个版本(version)字段，每操作一次，将那条记录的版本号加1。也通过增加时间戳的方式实现。

```mysql
select (id,status,version) from user where id=#{id};
update user set status=2,version=version+1 where id= 1 and version=#{version};
```

**悲观锁**

​	在操作数据时，认为此操作会出现数据冲突，所以在进行每次操作时都要通过获取锁才能进行对相同数据的操作，需要耗费较多的时间。悲观锁是由数据库自己实现，共享锁和排它锁是悲观锁的不同的实现。

​	共享锁又称为读锁，简称S锁，顾名思义，共享锁就是多个事务对于同一数据可以共享一把锁，都能访问到数据，但是只能读不能修改。

​	排他锁又称为写锁，简称X锁，顾名思义，排他锁就是不能与其他所并存，如一个事务获取了一个数据行的排他锁，其他事务就不能再获取该行的其他锁，包括共享锁和排他锁，但是获取排他锁的事务是可以对数据就行读取和修改。

​	对于update，insert，delete无法手动加共享锁和排他锁，mysql默认加排他锁。

##### 19.锁相关操作

```mysql
show open tables; #查看哪些表被加锁了
show status like 'table%'#table_locks_immediate产生表锁的次数，table_locks_waited锁等待次数
lock table tb1 read, tb2 write;#给表加读，写锁.表锁
unlock tables ; #解锁
show status like 'innodb_row_lock%' #查看行锁情况
```

##### 20.MyISAM锁

MyISAM表锁两种模式：表共享读锁，表独占写锁。<span style='color:red'>读锁阻写不阻读，写锁读写</span>都阻。

MyISAM表读操作为读锁，写操作加写锁。

#### 10.Mysql主从复制

##### 0.在线迁移mysql

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
mysql> source my3306/bak/xtj.sql #这句话需要选中数据库后再操作
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

```mysql
#查看master上连接的slave信息
show slave hosts;
#查看slave上的master信息
show slave status;
```

##### 1.主从复制参数

```mysql
#主	
server_id	#两个设置不一样
read_only 	#只读 off
sql_log_bin #产生binlog日志，on
binlog_format #日志格式 主从都是ROW，SBR是statement
binlog_cache_size #日志缓存大小
max_binlgo_size #
expire_logs_days #日志保留时间，默认7天
#从
server_id #与主不一样
read_only #on
sql_log_bin #on
log_slave_updates #ON M-S-S的话需要开启
```

##### 2.binlog日志格式

- **SBR**(statement based replicate):每条会修改数据的sql都会记录在binlog中
  - 优：不需要记录每一行的变化，减少了binlog日志量，节约了IO，提高了性能
  - 缺：记录的是语句不是具体值，如值是now()，会造成主从不一致
- **RBR**(row based replicate):不记录语句上下文相关信息，仅保存哪条记录被修改（**推荐**）
  - 优：binlog仅需要记录哪一条记录被修改成了什么，清晰的记录了每行记录的修改细节
  - 缺：可能会产生大量的日志记录，如一条update更新多条数据，则每条修改都有记录
- **Mixed**：SBR与RBR混合，不使用

##### 3.几种常见的日志

- **错误日志**：记录MySQL的启动、停止信息及在MySQL运行过程中的错误信息。log_error
- **普通日志**：记录连接请求和从客户端收到的SQL语句。**默认不记录**该日志，可开启general_log和指定目录general_log_file，记录日志到表设置log_output=TABLE,FILE，表为mysql.general_log（默认CSV存储引擎）
- **慢查询日志**：记录查询时间大于long_query_time及未使用索引的查询语句。slow_query_log，slow_query_log_file，对应的表为mysql.slow_log（CSV）
- **二进制日志**：记录所有修改数据的SQL语句，也用于MySQL的数据恢复与复制。log_bin，log_bin_basename

##### 4.Semi-sync复制（半同步复制）

将日志先传输到从库，然后再返回应用事务提交成功。减少数据丢失的风险（主从复制，主数据库宕机的话数据丢失严重），不能完全避免数据丢失。增加了额外的等待时间，性能有所下降。

```mysql
#主
mysql> install plugin rpl_semi_sync_master soname 'semisync_master.so';#lib/plugin下
#主开启 rpl_semi_sync_master_enabled=on ,配置文件中设置全局
#从
mysql> install plugin rpl_semi_sync_slave soname 'semisync_slave.so';#lib/plugin下
#主开启 rpl_semi_sync_slave_enabled=on ,配置文件中设置全局
```

##### 5.主从复制常见问题

1.**主库挂了怎么判断从库是否同步完成**？

```mysql
 show slave status #看主库同步的日志文件和位置
 #1看复制到哪个文件的哪个位置 Master_Log_File和Read_Master_Log_Pos
 #2看执行文件和位置 Relay_Master_Log_file和Exec_Master_Log_Pos
 #3看文件与位置相等不，也可查主的binlog与从的执行情况
 show processlist;
 #看到 has read all relay log同步完成
```

2.**lave_IO_Running 为connecting**（网络不通、密码不对、pos不对、用户权限不足）

3.**mysql误删库恢复**

​	1.将最新一次备份的数据回复

​	2.show binlog events in 'binlog.xxx' 记下删除之前的pos，做为stop- position

​	3.查看最新一次备份中的pos，做为start-position

​	4.使用mysqlbinlog 回复两pos之间的数据

##### 6.**单向复制与合并复制**：

​	MySQL采用单向复制，即所有更新都先发生在主服务器端，然后再复制给从服务器。从服务器是只读的，不会有更新传递到主服务器。

​	合并复制是多个服务器进行读写操作，并使用合并复制机制将这些更新统一，将最终的结果传递给从服务器。

##### 7.**同步复制与异步复制**：

​	同步：所有的请求操作都发生在主/从服务器端，改变同时提交到两个数据库中，操作也同时写到日志中，最后把结果返回给客户端。

​	异步：将请求在主服务器端执行，执行完成将结果返回给客户端，同时记录下操作，然后将变更的记录传递给从服务器，这会存在时间差。

##### 8.Replication应用两大模块

**主复制模块：**

​	记录操作到binlog，在index文件中更新不存在的binlog文件，并将符合要求的日志事件传递到从服务器的I/O线程。

**从复制模块：**

​	负责从服务器复制功能。通过I/O线程与主进行通信，并将更新复制到relay log中。然后由SQL线程更新。

#### 11.高可用的Mysql

##### 1.MHA集群架构

​	MHA管理主从，一个是备份数据，二个是主从切换，服务高可用。

​	MHA是用per语言写的一个脚本管理工具，可维持M-S中Master库的高可用性，最大的特点是可以修复多个slave之间的差异日志，最终使所有slave保持数据一致，然后从中选择一个充当新的Master，并将其它的slave指向它。

2.下载mha-manager(单独的mha机器，可管理多套mysql主从)和mha-node(每台mysql机器上)

https://github.com/yoshinorim/mha4mysql-manager

https://github.com/yoshinorim/mha4mysql-node

#### 12.Mysql框架架构设计与容量规划

##### 1.mysql架构设计

![](https://raw.githubusercontent.com/aiceflower/assets/master/img/mysql/mysql_frame_design.png)

##### 2.容量评估

- LOAD：机器负载，top命令查看load average: 0.01, 0.03, 0.00（平均一分钟，5，15），数据取自/proc/loadavg，何时达到负载，看数据占/proc/cpuinfo中cpu个数即processor的比例
- CPU：cpu使用率，  top查看cpu，取自/proc/stat
- QPS：每钞查询数
- TPS：每钞的事务数(DML操作)

- CONNECT
- IO
- NET/IN
- NET/OUT

峰值qps = (总的PV * 80%) /  (60 * 60 * 24 * 20%)  一天80%的访问会在20%的时间到达

机器数 = 总峰值qps / 压测得出的单台机器的极限qps

压测：

下载：https://codeload.github.com/akopytov/sysbench/zip/0.5

安装：

```shell
./autogen.sh    
./configure --with-mysql-includes=mysql/include --with-mysql-libs=mysql/lib && make
```

压测：

```shell
#准备工作
/root/sysbench-0.5/sysbench/sysbench --test=/root/sysbench-
0.5/sysbench/tests/db/select.lua --oltp-table-size=20000 --mysql-tableengine=innodb --mysql-user=root --mysql-password=123456 --mysql-port=3306 -
-mysql-host=127.0.0.1 --mysql-db=test --max-requests=0 --max-time=60 --oltptables-count=20 --report-interval=10 --num_threads=2 prepare
#跑
/root/sysbench-0.5/sysbench/sysbench --test=/root/sysbench-
0.5/sysbench/tests/db/select.lua --oltp-table-size=20000 --mysql-tableengine=innodb --mysql-user=root --mysql-password=root123 --mysql-port=3306 -
-mysql-host=127.0.0.1 --mysql-db=test --max-requests=0 --max-time=60 --oltptables-count=20 --report-interval=10 --num_threads=2 run
```

##### 3.读写分离方案

​	通过对数据库进行读写分离，来提升数据的处理能力。写操作集中到一个库上，读分解到其它库上。优：每台机器拥有全部数据，因此所有的数据库特性都可以实现，部分机器当机不影响系统的使用。缺：数据的复制同步是一个问题，需要考虑数据的迟滞性，一致性方面的问题。

##### 4.数据分区方案（基本不用）

​	把某一个或某几张相关的表的数据放在一个独立的数据库上，这样就可以把CPU、内存、文件IO、网络IO分解到多个机器中，从而提升系统处理能力。优：不存在数据库副本复制，性能更高。缺每个分区都是单点，如果某个分区宕机，就会影响到系统的使用。

##### 5.数据库分表方案（推荐）

​	也就是把数据库当中数据根据按照分库原则分到多个数据表当中，这样，就可以把大表变成多个小表，不同的分表中数据不重复，从而提高处理效率。优：数据不存在多个副本，不必进行数据复制，性能更高。缺：分表之间的数据很少进行集合运算；分表都是单点，如果某个分表宕机，如果使用的数据不在此分表，不影响使用。

分表方案：同库分表(分表)、不同库分表(分库)

**分库分表限制：**

1.条件查询，分页查询受限制，查询必须带上分库分表所带上的id。

2.事务可能跨多个库，数据一致性无法通过本地事务实现。

3.规则确定后，扩展变更规则要迁移数据。

**分库分表业务痛点**：

全局序列、分片规则、数据聚合、跨库JOIN、分布式事务、高可用、负载均衡、开发复杂度。

#### 13.Online DDL

**1.mysql原生**

```mysql
alter table emp drop column age,algorithm=inplace,lock=none;#添加字段，创建索引也用alter，不用create，还有copy的方式，但原生的建议使用inplace
```

**锁(lock)**：

- DEFAULT：判断当前DDL是否可以用NONE，如果不能判断是否可以用SHARED...EXCLUSIVE
- NONE：不加任何锁，可读可写，最大并发
- SHARED：共享模式，mysql的快速创建索引(fast index create)，使用的就是共享锁。可读，不可写。
- EXCLUSIVE：排他，不可读不可写。

**算法**：

- DEFAULT：根据old_alter_table来确定是用INPLACE(off)还是COPY(on)

- INPLACE：效率比COPY快
- COPY：

**COPY方式(以创建索引为例)：**

1.新键带索引的临时表，与原表结构相同

2.锁原表(s)，禁止DML，允许查询。

3.将原表数据复制到临时表

4.锁原表(x)，禁止读写，进行rename，升级字典锁

5.完成创建索引操作

**INPLACE方式：**

1.创建索引（只能是二级索引，如果是聚族索引则会使用COPY方式）数据字典

2.加锁(s)，禁止DML，允许查询。

3.读取聚族索引和将二级索引所对应的字段写到二级索引数据字典里，排序并插入新索引（一个新的数据字典，不需要复制数据），**性能高于COPY方式**。

4.等待打开当前表的所有只读事物提交

5.创建索引结束

##### 2.使用OAK工具

Openark-kit工具包的小工具oak-online-alter-table小工具是用来实现Online DDL的（copy的方式）。支持线上DDL/DML/并发。

使用时要检查是否符合oak条件：

- 单列唯一索引（联合索引会引发mysql一个bug）
- 没有外键
- 没有定义触发器（有也先删除了）

### 四、Mysql数据结构

#### 1、Mysql底层数据结构

- **hash算法**：

  查找速度快，不能进行范围查找（底层数据结构是散列的，无法进行比较大小）。

- **平衡二叉树(AVL树)**：

  左子树的值比中间值小，右子树的值比中间值大。左子树和右子树都是平衡二叉树。左子树和右子树的深度（高度）之差的绝对值不超过1。

  查找速度较快。支持范围查找，但效率较低。先定位到目标元素，再进行回旋，回旋效率较低。

- **B树**：

  在平衡二叉树的基础上，减少树的高度，也即减少了IO操作。一个节点可以有多于两个的子节点，节点元素也可存放多个。

  查询效率比平衡二叉树高，支持范围查找，效率低，原理同平衡二叉树。

- **B+树**：

  在B树的基础上，增加了叶子节点与非叶子节点的关系。叶子节点包含key和value，非叶子节点只含value，通过非叶子节点获取叶子节点从而获取value(所有的非叶子节点都会在叶子节点上再出现 一次)。所有相邻叶子节点包含非叶子节点，使用链表进行结合，有一定顺序，范围查询效率高。

  缺点：有数据的冗余，会比较占硬盘大小。

注：树的深度越大，那么查询IO的次数越多。

#### 2.MyISAM与InnoDB引擎B+树的区别

- MyISAM引擎的叶子节点存放的是数据的地址，再通过地址查询数据。
- InnoDB的叶子节点存放的是一行数据。

### 五、MySQL优化

#### 1.数据库设计要合理

a）设计原则

​	减少冗余量，遵循三范式

b）三范式

- 1F：原子约束，列不可再拆分。（是否原子，与业务挂钩）
- 2F：保证唯一性，要有主键
- 3F：消除传递依赖，即不要有冗余数据。适当的冗余可以提高查询效率。

#### 2.索引

**什么是索引**

​	索引是帮助MySQL高效获取数据的**数据结构**。可理解为：排好序的快速查找数据结构。

**覆盖索引**：

- 查询谓词(**查询条件**)都能够通过index进行扫描
- 排序(group by、order by)谓词(**排序，分组条件**)都能够通过index进行扫描
- index包含了查询所需要的所有字段

**不能使用索引**：

- 不给选择率低的字段建索引，如性别
- 联合索引中：不要第一个索引列使用范围查询，第一个查询条件不是最左列 【index_name(age,name)】
- like条件不以%开始
- 两个独立索引，一个检索一个排序，建议做组合排序
- 不要在索引字段上使用函数操作
- 不使用外键索引

**索引的优缺点**

优：提高数据**检索效率**，降低数据库的IO成本。降低数据**排序的成本**，也降低了CPU的消耗

缺：索引也占用空间。在做Insert、Update、Delete时需要维护索引。

#### 3.分表分库

##### 1.垂直拆分

​	分库。一个大项目，拆分成多个小项目，每个小项目拥有自己单独的数据库，互不影响。

##### 2.水平拆分

​	分表。根据业务需求拆分。存放日志（按年拆分），QQ号（取模，根据位数分不均匀）	。

实现方式：单独一张表做主键自增长，然后将id取模后插入到不同表中。

缺点：分页查询，查询受限制。

解决：可分主表存放所有数据，再根据业务分表。使用MyCat进行分表。使用rds阿里云(收费)，底层自带分表，解决高并发。

#### 4.读写分离

#### 5.参数配置

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



#### 6.mysql服务器升级

#### 7.随时清理碎片化

​	MyISAM储存引擎，删除数据时，并不会清理数据。需要调用如下语句，清理碎片化。

​	optimize table tb_name;#清理，可看MYD文件大小确定。

#### 8.sql语句调优

##### 1.慢查询 

**什么是慢查询？**

​	查询时间超过long_query_time(mysql默认10s)定义时间的查询就为慢查询.

**如何定位慢查询？**

​	show status like '%Slow_queries%'; 如果大于0则说明存在慢查询

​	根据slow_query_log_file查询慢查询日志存放位置，根据日志定位慢查询语句。

**慢的原因？**(执行时间长、等待时间长)

​	查询语句写的烂、索引失效、关联查询太多(设计缺陷或不得已需求)、服务器调优及各参数设置	

**开启时机**

​	需要的时候才开启，会影响一定的性能。slow_query_log(on/off)

**日志分析工具**

```mysql
#mysqldumpslow 工具
s：按何种方式排序
c：访问次数
l：锁定时间
r：返回记录
t：查询时间
al：平均锁定时间
ar：平均返回记录数
at：平均查询时间
t：返回前面多少条数据
g：后边搭配一个正则匹配模式，大小写不敏感
#得到返回记录集最多的10个slq
mysqldumpslow -s r -t 10 mysql-slow.log
#得到访问次数最多的10个sql
mysqldumpslow -s c -t 10 mysql-slow.log
#得到按照时间排序的前10条数据里面含有左连接的查询语句
mysqldumpslow -s t -t 10 -g "left join" mysql-slow.log
#建议在使用这些命令时结构|和more使用
```

​	

​	

##### 2.高效sql

![](https://raw.githubusercontent.com/aiceflower/assets/master/img/mysql/%E9%AB%98%E6%95%88sql.png)

##### 3.性能分析

1）MySQL Query Optimizer

2）MySQL常见瓶颈：

- CPU：CPU瓶颈一般在数据装入内存或从磁盘上读取数据时
- IO：硬盘IO瓶颈发生在装入数据远大于内存容量时
- 服务器硬件：通过top、free、iostat、vmstat查看系统性能状态

3）EXPLAIN

**是什么**

​	查看SQL执行计划

**能干嘛**

- 表的读取顺序(id)
- 数据读取操作的操作类型(select_type)
- 哪些索引可以使用(possible_keys)
- 哪些索引被实际应用(key)
- 表之间的引用(ref)
- 每张表有多少行被优化器查询(rows)

**怎么玩**

​	explain + SELECT语句

**结果**

a）**id**

​	select查询的序列号，包含一组数字，表示查询中执行select子句或操作表的顺序

三种情况：

​	id相同：执行顺序由上至下

​	id不同：如果是子查询，id的序号会递增，id值越大优先级越高，值越大越先被执行

​	id相同不同：同时存在

注：id如果相同，可认为是一组，从上往下顺序执行。在所有组中，id值越大，优先级越高，越先执行。

可查看执行顺序

b）**select_type**

- SIMPLE：简单的select查询，查询中不包含查询或UNION

- PRIMARY：查询中包含任何复杂的子部分，最外层标记为PRIMARY
- SUBQUERY：子查询
- DERIVED：在FROM列表中包含的子查询被标记为DERIVED(衍生)，MYSQL会递归执行这些子查询，结果放在临时表中
- UNION：第二个SELECT出现在UNION之后，则被标记为UNION：若UNION包含在FROM子句中，外层SELECT被标记为DERIVED
- UNION RESULT：从UNION表获取结果的SELECT

c）**type** ：判断查询是否高效的重要依据

- system：表只有一行记录

- const：表示通过索引一次就找到了，主键或唯一索引扫描，只匹配一条数据

- eq_ref：主键索引的关联查询，

- ref ：非唯一索引查询或唯一索引关联查询，单条记录。

- range：索引范围扫描，常用语<,<=,>=,between,in,is null等操作，返回少部分数据时，数据多时为All

- index：索引全扫描

- ALL：全表扫描

type类型结果值从**最好到最坏**依次是：

**system**>**const**>**eq_ref**>**ref**>fulltext>ref_or_null>index_merge>unique_subquery>index_subquery>**range**>**index**>**all**

注：一般来说要保证查询至少到range，最好能达到ref

d）**possible_keys**

​	 此次查询中可能被使用的索引

e）**key**

​	 此次查询中实际使用到的索引，查询中若使用了覆盖索引（查询的字段与建立复合索引一致，查询的列被所建的索引覆盖），则该索引只出现在key列表中。

f）**key_len**

​	索引中使用的字节数，通过该列获取使用的索引长度。在不损失**精确性**的情况下，**长度**越短越好。

g）**ref**

​	 显示索引的哪一列被使用了。如果可能的话，是一个常数。哪些列或常量被用于查找索引列上的值。

h）**rows**

​	大致估算出，找到所需要的记录所需要读取的行数。

i）**extra**

​	包含不适合在其它列显示，但十分重要的额外信息。

- <span style='color:red'>using filesort</span>：对数据使用外部排序（**九死一生**）
- <span style='color:red'>using temporary</span>：使用临时表保存中间结果，mysql在对查询结果排序时使用临时表。（**十死无生**）

注：以上多出现在排序数量与顺序与建立索引数量与顺序不一致的情况

- <span style='color:red'>using index</span>：使用了覆盖索引
- using where：使用了where过虑
- using join buffer：使用了连接缓存
- impossible where：where子句的值总是false，不能获取任何元组
- select tables optimized away：没有group by 的情况下基于索引优化min/max操作
- distinct：优化distinct操作

**索引建立**

一表：顺序建立就行，如果中间有范围查询索引会失效

二表：左右连接，相反建立

三表：同二表，相当于两次二表

**4）永远小表驱动大表**

**in与exists**

```mysql
select * from A where id in (select id from B) #B的数据集小于A时，in优于exists
select * from A where exists (select 1 from B where B.id = A.id)
=
for select * from A 
for select * from B where B.id = A.id #当A的数据小于B时，exists优于in
```

**EXISTS**

SELECT * FROM tb_name WHERE **EXISTS** (subquery)

将主查询的数据，放到子查询中做条件验证，根据验证结果(T or F )来决定主查询数据是否保留。

**5）ORDER BY优化**

Mysql支持**两种排序**：filesort和index

order by使用index排序**满足情况**：

- order by 语句使用索引最左前列
- 使用where子句与order by 子句条件列组合满足索引最左前列

尽可能在**索引列上完成排序**操作。

如果不在索引列上，**firesort有两种算法**：

- 双路排序：两次IO，(mysql4.1以前)
- 单路排序：一次IO，但有个问题(排序数据大于sort_buffer指定大小，需要多次取出性能不如双路)

增大sort_buffer_size和max_length_for_sort_data参数设置

**提高Order By 的速度**

a）order by时select * 是个大忌，缓冲区大小可能不够用。两种算法都可能超出sort_buffer容量，超出后会创建tmp文件进行合并排序

b）尝试提高sort_buffer_size，两种算法都会提高效率

c）尝试提高max_length_for_sort_data，会增加用改进算法的概率

**总结：**

- 为排序使用索引
- 索引同升同降会使用索引
- 如果最左前列为常量，则order by能使用索引

**为排序使用索引**

```mysql
key a_b_c(a,b,c);#创建abc三个字段组合索引
#order by 能使用索引，最左前缀
order by a
order by a,b
order by a,b,c
order by a desc, b desc, c desc
#如果where使用索引的最左前缀定义为常量，则order by能使用索引
where a = const order by b,c
where a = const and b = const order by c
where a = const and b > const order by b,c
#不能使用索引进行排序
order by a asc, b desc, c desc 	#排序不一致
where g = const order by b,c  	#丢失a索引
where a = const order by c		#丢失b索引 
where a = const order by a,d	#d不是索引的一部分
where a in(...) order by b,c 	#对于排序来说，多个相等条件也是范围查询
```

**6）GROUP BY优化**

大部分同Order By。

group by 实质是先排序后分组，遵循索引建立的最佳左前缀

where高于having，能写在where限定的条件就不要写在having

##### 4.索引失效

**如何避免**

- 全值匹配我最爱(复合索引)
- 最佳左前缀法则：复合索引中查询从最左列开始且不**跳过索引中的列**。
- 不在索引列上做任何操作（计算、函数、(自动or手动)类型转换，会导致<span style="color:red">索引失效</span>
- 存储引擎不能使用索引中范围条件右边的列
- 尽量使用覆盖索引（只访问索引的查询(索引列和查询列一致)），减少select *
- 不使用不等于(!=、<>)，会导致全表扫描
- 不使用is not null
- like条件不以通配符开头，索引失效导致全表扫描（业务就要使用两边%号，可**使用覆盖索引**）
- 字符串要加单引号，不加索引失效（用一隐匿类型转换）
- 少用or，用它列连接时索引可能会失效

注：复合索引中跳过中间或中间是范围查找，后面的不会使用索引但like常量开头后面索引还可用

<font style="color:red">总结</font>：

- 全值匹配我最爱，最左前缀要遵守
- 带头大哥不能死，中间兄弟不能断
- 索引列上少计算，范围之后全失效
- like百分加右边，覆盖索引不写星
- 不等空值还有or，索引失效要少用
- varchar引号不可丢，SQL高级也不难

##### 5.show profile

**是什么？**

​	mysql提供，可用来分析当前会话中语句执行的资源消耗情况，可用于sql的调优测量。默认参数处于关闭状态，并保存近15次的运行结果。

**怎么用？**

```mysql
#1.是否支持
show variables like 'profiling';
#2.开启功能
set profiling = on;
#3.运行sql
#4.查看结果
#5.诊断sql，show profile cpu,block io for query num; #上一步的问题sql数字号码
type:
all 				显示所有开销信息
block io 			显示块IO开销
context switches	上下文切换开销
cpu					CPU开销
ipc					发送和接收开销
memory				内存开销
page faults			页面错误开销
source				source_function, source_file, source_line 相关开销
swaps				交换次数相关开销
#6.日常开发需要注意的结论
converting HEAP to MyISAM 查询结果太大，内存都不够用了往磁盘上搬了
creating tmp table 创建临时表 (拷贝数据到临时表，用完再删除)
copying to tmp table on disk 把内存中临时表复制到磁盘，危险！！！
locked
```

##### 6.全局查询日志

<span style='color:red'>永远不上在生产环境上用</span>

```mysql
set global general_log = 1;
set global log_output='table';
```



#### 9.其它

##### 1.IO调度算法

- NOOP算法： 实现了最简单的FIFO队列，所有IO请求大致按照先来后到的顺序进行操作。
- CFQ算法：对IO地址进行分组排序，把相近的放在一起，提高吞吐量。不是先来后到。
- Deadline算法：在CFQ基础上解决了请求饿死的情况，还额外为读请求和写请求提供了FIFO队列。
- Anticipatory算法：

##### ~~2.操作系统优化~~

1.关闭swap，内核参数/etc/sysctl.conf 中添加vm.swapplness=10，默认是60

2.调度算法设置deadline，echo deadline > /sys/block/.../queue/schedule

10. #### 排查

1.观察，至少跑一天看看的慢SQL情况

2.开启慢查询日志，设置阙值，提取慢查询SQL

3.explain + 慢SQL分析

4.show profile 查询SQL在Mysql服务器里面的执行细节和生命周期情况

5.进行SQL数据库服务器的参数调优

### 六、存储引擎

#### 1.Mysql数据文件

**元数据文件  --  frm**

​	MySQL中的每个表，在磁盘上均有一个.frm的文件与之对应(每种存储引擎都有这个文件)。frm在所有平台上的格式是一样的。创建索引也会生成一个.frm文件。打开表时frm文件被缓存在table cache中，下次访问这个表时，无需再次打开和解析这个frm，直接从缓存中取。

**MYISAM数据文件 -- myd**

​	数据与元数据相互穿插存储。MYISAM支持三种不同存储格式 -- 固定的、动态的和压缩的。其中固定和动态根据使用的列类型自动选择，压缩的只能用myisampack工具创建。

**MYISAM索引文件 --  myi**

​	MYISAM存储引擎的每个表都对应一个MYI文件。MYI文件包含两部分，头部信息和索引值。

**Innodb架构**

![](<https://raw.githubusercontent.com/aiceflower/assets/master/img/mysql/innodb_frame.png>)

​	第一层：Handle AP的存在使得Innodb能够顺利插入到Mysql中，同是Innodb还为应用提供了API，用户可将innodb存储引擎添加到其它应用中。

​	第二层：事务层，在Innodb中，所有行为都发生在事务中。

​	第三层：锁功能层，完成锁功能和事务管理的功能(如回滚、提交等)。Innodb采用行级读写锁。

​	第四层：缓存管理层，高效的将数据存放在内存之中。

​	第五层：存储空间IO管理，为文件读写提供接口并维护表空间和日志空间大小。

![](https://raw.githubusercontent.com/aiceflower/assets/master/img/mysql/innodb_architecture.png)

#### 2.MyISAM和InnoDB主要区别

1）MyISAM是非事务安全型的，而InnoDB是事务安全型的。
2）MyISAM锁的粒度是表级，而InnoDB支持行级锁定。
3）MyISAM支持全文类型索引，而InnoDB不支持全文索引。
4）MyISAM相对简单，所以在效率上要优于InnoDB，小型应用可以考虑使用MyISAM。
5）MyISAM表是保存成文件的形式，在跨平台的数据转移中使用MyISAM存储会省去不少的麻烦。
6）InnoDB表比MyISAM表更安全，可以在保证数据不会丢失的情况下，切换非事务表到事务表。

![](https://raw.githubusercontent.com/aiceflower/assets/master/img/mysql/instore_compare.png)

#### 3.应用场景

1）MyISAM管理非事务表。它提供高速存储和检索，以及全文搜索能力。如果应用中需要执行大量的SELECT查询，那么MyISAM是更好的选择。
2）InnoDB用于事务处理应用程序，具有众多特性，包括ACID事务支持。如果应用中需要执行大量的INSERT或UPDATE操作，则应该使用InnoDB，这样可以提高多用户并发操作的性能。