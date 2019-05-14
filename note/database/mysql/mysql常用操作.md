<center><h2>Mysql</h2></center>

### **一、Mysql常用操作**

#### **1.增**

**创建数据库**

```mysql
create database testdb default character set utf8;
```

#### **2.删**

#### **3.改**

#### **4.查**

**查看当前用户**

```mysql
select user();
```

***查询表***

```mysql
#1
show tables;
#2
show tables from db_name like '%user%';
#3
select table_name from information_schema.tables where table_schema='database_name' and table_type='base table';
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



#### 5.其它

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

