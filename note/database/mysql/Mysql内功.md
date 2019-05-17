## Life is short, you need MySql

### 一、Mysql需知

- Mysql设计之初的目标：成为高性能、普通用户支付得起的数据库服务器和工具。
- Mysql的使命：为所有人贡献一个经济实惠并且性能卓越的数据库软件。
- Mysql的独到之处：插件式存储引擎，存储类别的粒度小到每个表，可最大程度利用各存储引擎的优点，避免缺点。

### 二、数据库管理系统

#### 1.数据库系统的分类

三种觉见的数据库系统：面向对象型、关系型、对象关系型

#### 2.数据库管理系统功能模块

- 网络客户端连接：客户端访问
- 查询解析和优化：查询接口、查询处理、查询优化、查询执行
- 存储引擎：文件读取

#### 3.查询语句处理

1）语句合法性检查：对SQL语句的语法进行检查、看其是否符合语法规则。

2）语义检查：对语句中的字段，表等进行检查

3）获得对象解析锁：保证数据的一致性，防止其它用户改变(保证大量并发情况下的数据完整性)

4）数据访问权限核对：检查用户是否有这个数据（库、表、列）的访问权限。

#### 4.查询优化

查询优化器：通过可供选择的执行计划，找到最低估算开销的执行计划，来优化一条SQL语句。(基于开销的优化)

#### 5.语句执行

​	先查询高速数据缓冲区，如果有数据直接返回客户端，如果没有去数据库文件查询并把数据放入到数据缓冲区。

#### 6.模块协作

 客户端  -->  发送   select * from table_name;  -->

查询接口-->查询处理/优化-->查询执行-->读写文件-->查询结果

### 三、Mysql数据库系统

#### 1.Mysql子系统和核心库

- 网络连接和网络通信协议子系统
- 线程、进程和内存分配子系统
- 查询解析和查询优化子系统
- 存储引擎接口子系统
- 各类存储引擎子系统
- 安全管理子系统
- 日志子系统
- 其他子系统 -- 如复制功能、错误功能
- mysys核心库文件

#### 2.Mysql框架和模块

##### 1.框架和模块图

![Mysql框架和模块](<https://raw.githubusercontent.com/aiceflower/assets/master/img/mysql/mysql_frame_and_module.png>)

##### 2.源码部分目录结构简介

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

##### 3.mysql数据文件及目录简介

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

#### 3.Mysql查询执行过程

![Mysql查询执行过程](https://raw.githubusercontent.com/aiceflower/assets/master/img/mysql/mysql_query_executing_process.png)

#### 4.Mysql核心算法

##### 1）Bitmaps -- 位图(mysys/my_bitmap.c包含操作位图的函数)

比特位图使用少量空间，提供大量信息。用一个比特位代表true和false值。

##### 2）表连接缓存工作原理

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

##### 3）Mysql排序实现

排序不仅仅用于order by ,同样group by 也会用到排序，Mysql有两类方式进行排序：

- **作用 range、ref、index读写方式**：explain的输出 range、ref、index是描述对索引的读取方式。这类方式获得的输出都是按索引的顺序排序的
- **filesort排序算法**：将一组记录按照快速排序放入到内存缓存，然后这几个内存缓存按合并算法排序 @@sort_buffer_size

***执行排序的三种方法***：

- **使用已排序索引**：Explain不提及任何filesort #有索引且排序的是索引字段
- **在表单上使用filesort**：using filesort #如果是多表联合，先单表filesort再对排序后进行join
- **将join结果先放入临时表，然后使用filesort**：using templorary; using filesort

#### 5.Mysql网络通信

##### 1.网络通信图示

![](<https://raw.githubusercontent.com/aiceflower/assets/master/img/mysql/mysql_network_communications.png>)

##### 2.MySQL线程容器类

Mysql线程容器类I_LIST\<THD>主要用途：

- 执行 show processlist命令时，可快速拿到所有线程状态
- 执行kill命令时，能快速定位目标线程
- 在关闭mysql服务时，可依次关闭所有mysql线程
- 作为线程缓冲队列

**注**：线程相关操作，如创建、结束、跟踪线程状态都需要获得一个互斥锁。

#### 6.解析与优化

##### 1.Mysql解析器

​	mysql解析器由词法分析和语法分析两部分组成。

##### 2.查询优化器

​	通过产生可供选择的多个执行计划，并从中选择最低估算开销的执行计划，来优化一条sql语句。

#### 7.安全管理系统

##### 1.安全相关

1）**账号组成**

​	由用户名和主机共同组成（'root'@'127.0.0.1'）。同一个账号，不同的主机登陆，MySQL服务认为是不同的连接请求。

2）**MySQL登陆认证**

![](<https://raw.githubusercontent.com/aiceflower/assets/master/img/mysql/mysql_login_check.png>)

3）**具体优先原则**

​	服务器把user读到内存后，根据Host和user字段的具体程度重新对host,user字段组成的记录进行排列，然后服务器会根据最先匹配的一条记录去允许登陆连接。

##### 2.Mysql权限表(mysql库下)

- **user**：超级 用户表，包括Mysql操作的所有权限。也用其来进行登陆验证。
- **db**：针对数据库的权限，权限范围为该库中所有对象(包括表和字段)。对指定host的权限操作。
- **tables_priv**：数据库中指定表的权限，细化到表
- **columns_priv**：数据库中指定字段的权限，细化到表中字段
- **procs_priv**：该表对某一个单独的存储过程或函数进行权限管理。

##### 3.权限级别

MySQL给一个合法用户赋予权限是按照以下顺序得到的：

user(全局) -->  db(库) -->  tables_priv(表)  -->  columns_priv(列)  -->  procs_priv(函数)

权限检查过程也是从user到procs_priv，检查user表如果为Y则所有权限都为Y，不再往下检查，如果为N则往下检查，依此类推。

##### 4.安全级别

![](<https://raw.githubusercontent.com/aiceflower/assets/master/img/mysql/security_level.png>)

#### 8.经典存储引擎

##### 1.Mysql数据文件

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

#### 9.MySQL其它功能

##### 1.几种常见的日志

- **错误日志**：记录MySQL的启动、停止信息及在MySQL运行过程中的错误信息。log_error
- **普通日志**：记录连接请求和从客户端收到的SQL语句。**默认不记录**该日志，可开启general_log和指定目录general_log_file，记录日志到表设置log_output=TABLE,FILE，表为mysql.general_log（默认CSV存储引擎）
- **慢查询日志**：记录查询时间大于long_query_time及未使用索引的查询语句。slow_query_log，slow_query_log_file，对应的表为mysql.slow_log（CSV）
- **二进制日志**：记录所有修改数据的SQL语句，也用于MySQL的数据恢复与复制。log_bin，log_bin_basename

##### 2.复制功能

**单向复制与合并复制**：

​	MySQL采用单向复制，即所有更新都先发生在主服务器端，然后再复制给从服务器。从服务器是只读的，不会有更新传递到主服务器。

​	合并复制是多个服务器进行读写操作，并使用合并复制机制将这些更新统一，将最终的结果传递给从服务器。

**同步复制与异步复制**：

​	同步：所有的请求操作都发生在主/从服务器端，改变同时提交到两个数据库中，操作也同时写到日志中，最后把结果返回给客户端。

​	异步：将请求在主服务器端执行，执行完成将结果返回给客户端，同时记录下操作，然后将变更的记录传递给从服务器，这会存在时间差。

##### 3.Replication复制实现原理

1）主服务器将用户对数据库的改变操作以二进制方式记录在binlog日志中，然后由Binlog Dump线程将日志中的内容传给从服务器。

2）从服务器通过一个I/O线程将主服务器的二进制日志文件中的操作事件，复制到本地的Relay Log的中继日志文件中。（start slave后启动）

3）从服务器通过另一个SQL线程将Relay Log中的操作依次顺序地在本地执行。Relay Log与Binlog有相同的格式，SQL线程会自动删除Relay Log中已经被执行的事件。

##### 4.Replication应用两大模块

**主复制模块：**

​	记录操作到binlog，在index文件中更新不存在的binlog文件，并将符合要求的日志事件传递到从服务器的I/O线程。

**从复制模块：**

​	负责从服务器复制功能。通过I/O线程与主进行通信，并将更新复制到relay log中。然后由SQL线程更新。

#### 99.优化

**1.SELECT语句分析**

​	explain + SELECT语句

*结果*：

- **id**：可查看执行顺序

- **type** ：判断查询是否高效的重要依据

  const/system：主键或唯一索引扫描，最多返回一条数据，速度非常快

  eq_ref：主键索引的关联查询，

  ref ：非唯一索引查询或唯一索引关联查询，单条记录。

  range：索引范围扫描，常用语<,<=,>=,between,in,is null等操作，返回少部分数据时，数据多时为All

  index：索引全扫描

  ALL：全表扫描

  性能：ALL < index < range < ref < eq_ref < const < system

  SQL性能优化目标：至少到range，要求是ref，往上更好

- **possible_keys**: 此次查询中可能选用的索引

- **key**: 此次查询中确切使用到的索引.

- **ref**: 哪个字段或常数与 key 一起被使用 

2. STRAIGHT_JOIN  inner join