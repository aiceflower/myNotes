<center><h2>Mysql</h2></center>

### 一、Mysql常用操作

#### 1.增

***查看表索引***：

​	 show index from table_name;

***查询所有表名***:

​	select table_name from information_schema.tables where table_schema='database_name' and table_type='base table';

***查询表字段***：
	select column_name from information_schema.columns where table_schema='database_name' and table_name='table_name';

***查询mysql数据存储位置***：
	show global variables like "%datadir%";

#### 2.删

#### 3.改

#### 4.查

#### 5.其它

***源码下载***：

​	<https://dev.mysql.com/downloads/mysql/5.5.html?os=src&version=5.1>

**字符集和核对规则**：

系统使用utf8字符集，若使用utf8_bin校对规则执行SQL查询时区分大小写，使用utf8_general_ci不区分大小写

show variables like 'char%' 查看字符集
show variables like 'collation_%' 查看核对规则