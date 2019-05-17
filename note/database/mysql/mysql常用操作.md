<center><h2>MySQL基础</h2></center>

### 一、MySQL基础操作

#### 1.系统相关

1.启动

2.关闭

3.

#### 2.库

##### 1.增

```mysql
#创建数据库，调协编码为utf-8,
create database testdb default character set utf8;
```

##### 2.删

##### 3.改

##### 4.查

```mysql
show databases;#查看所有数据库
select database();#查看当前选中数据库
show create database db_name; #查询库创建语句
```



##### 5.其它

#### 3.表

##### 1.增

```mysql
#创建普通表
create table test (
    id int primary key auto_increment,#设置主键自增长
    mtime timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,#自支更新时间
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='test table'
#创建带分区的表
create table t2(id int primary key,name varchar(10)) engine=MYISAM comment '*' partition by hash(id) partitions 2;
```



##### 2.删

##### 3.改

##### 4.查

```mysql
show tables#查询当前库下所有表
```



##### 5.其它

#### 4.索引

##### 1.增

##### 2.删

##### 3.改

##### 4.查

##### 5.其它

### **二、Mysql常用操作**

#### **1.增**

**创建数据库**





#### **2.删**

#### **3.改**

#### **4.查**

**查看当前用户**

```mysql
select user();
```

***查询表***

```mysql
#------表名称--------
#1
show tables;
#2
show tables from db_name like '%user%';
#3
select table_name from information_schema.tables where table_schema='database_name' and table_type='base table';
#-----------表创建结构
show create table table_name;

```

***查看表索引*：**

```mysql
show index from table_name;
```

***查询表字段*：**

```mysql
#1
desc table_name;
#2
show columns from user;
#3
select column_name from information_schema.columns where table_schema='database_name' and table_name='table_name';

```

**查询表的存储引擎**

```mysql
show create table table_name;
#或 【 information_schema.tables 表中也有】
show table status from db_name where name = 'table_name';
```



#### **5.系统相关**

**查看mysqld调用my.cnf顺序**

```shell
mysqld --verbos --help |grep my.cnf
```

**查看表是否正在被访问**

```mysql
show processlist;
```

**查看存储引擎状态**

```mysql
show engine innodb status \G;
```

**字符集和核对规则：**

```mysql
show variables like 'char%' #查看字符集
show variables like 'collation_%' #查看核对规则
#系统使用utf8字符集，若使用utf8_bin校对规则执行SQL查询时区分大小写，使用utf8_general_ci不区分大小写
cat data/db_name/db.opt
```

***查询mysql数据存储位置***​	

```mysql
show global variables like "%datadir%";
```

***查看事物隔离级别***

```mysql
show variables like '%tx%';
```

#### 5.锁相关

**加锁**

```mysql
#行排它锁
select * from t1 where name ='C' for update;
#行共享锁
select * from t1 where id =1 lock in share mode;

```

**查锁**

```mysql
#查询锁
select * from information_schema.innodb_locks; 
#查询innodb引擎状态，可查看锁情况
show engine innodb status \G
#查询具体等的语句
SELECT r.trx_id waiting_trx_id,  
	r.trx_mysql_thread_id waiting_thread,
	r.trx_query waiting_query,
	b.trx_id blocking_trx_id,
	b.trx_mysql_thread_id blocking_thread,
	b.trx_query blocking_query
FROM       
	information_schema.innodb_lock_waits w
	INNER JOIN information_schema.innodb_trx b  ON  
	b.trx_id = w.blocking_trx_id
	INNER JOIN information_schema.innodb_trx r  ON  
	r.trx_id = w.requesting_trx_id;
```



#### 6.其它

***源码下载*：**

​	<https://dev.mysql.com/downloads/mysql/5.5.html?os=src&version=5.1>

**binlog相关看作**

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

mysql数据库的原则，如果数据是重复的插入是插在后面。

