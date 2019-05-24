<center><h2>MySQL基础</h2></center>

### 一、MySQL基础操作

#### 1.系统相关

##### 1.查

```mysql
show engines;#查询系统支持的引擎
show table status from db_name where name = 'table_name';#查询表的存储引擎
mysql --help |grep my.cnf #查询配置文件加载顺序
show processlist;#查询进程
show engine innodb status \G;#查看引擎状态
cat data/db_name/db.opt#查看字符集与核对规则
```

##### 2.系统参数

```mysql
show variables like '%xxx%';#查看与xxx有关的参数
port	#端口
char	#系统编码
collation #核对规则
#系统使用utf8字符集，若使用utf8_bin校对规则执行SQL查询时区分大小写，使用utf8_general_ci不区分大小写
date_format #日期格式
storage_engine #默认存储引擎
sql_log_bin #1:记录二进制日志,0:不记录
log_error #错误日志文件位置
slow_query_log #慢查询
datadir #数据库数据位置
tx_isolation #查询日志隔离级别
```

##### 3.操作积累

```mysql
explain select_statement; #分析查询执行计划
prompt mysql> #\D 完整日期，\d 当前数据库，\h 服务器名称，\u 当前用户 修改提示符
show warnings; #查看警告信息
select * from t1 group by name order by null;#避免多余排序
group_concat(); #对分组后的某字段进行连接
limit 也可跟于update，delete语句后
limit 3000,20 可用 id > 3000 limit 20 替换，提高效率
mysql> system ls -lh /tmp/ #mysql登陆状态下执行系统命令
```



**MySQLroot密码修改**

```mysql
#1.知道root密码，登陆
set password for root@localhost= password('new_password');
#2.知道root密码,登陆
update user u set authentication_string = password('123456') where u.user = 'root';
flush privileges;
#3.知道root密码未登陆
mysqladmin -uroot -pold_passowrd password new_password;
mysqladmin -uroot -proot password 123456;
#4.不知道root用户密码
mysqld --skip-grant-tables；#跳过权限校验启动服务
mysql -u root;#登陆后使用1，2，3方式修改
```



##### 4.其它

***源码下载*：**

​	<https://dev.mysql.com/downloads/mysql/5.5.html?os=src&version=5.1>



#### 2.库

##### 1.增

```mysql
#创建数据库，调协编码为utf-8,
create database [IF NOT EXISTS] testdb default character set utf8;
```

##### 2.删

```mysql
#删除数据库
drop database [IF EXISTS] db_name;
```

##### 3.改

```mysql
alter database db_name character set utf8;
```



##### 3.查

```mysql
show databases;#查看所有数据库
select database();#查看当前选中数据库
show create database db_name; #查询库创建语句
```

##### 4.其它

```mysql
#切换数据库
use db_name;
```



#### 3.表

##### 1.增

```mysql
#-------表
#创建普通表
create table [IF NOT EXISTS] test (
    id int primary key auto_increment,#设置主键自增长,
    name varchar(10) not null,#非空
    id_no int(18) unique,
    flag int default 1,
    mtime timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,#自支更新时间,
    unique key u_id_no (id_no),
    constraint fk_name foreign key(column_1) references tb_name(id)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='test table'
#创建带分区的表
create table t2(id int primary key,name varchar(10)) engine=MYISAM comment '*' partition by hash(id) partitions 2;

create table t2 like t1; #复制表结构，t2可以学习到t1所有的表结构

#-------字段
alter table tb_name add col_name varchar(10) not null;#增加字段

#-------数据
insert into tb_name(col1,col2,...) values(v1,v2,..),(vv1,vv2,...);#插入
insert into tb_name1 select * from tb_name2;#将查询结果插入表，不关心列名，根据位置插入

```

##### 2.删

```mysql
#-------表
drop table [if exists] 表1, 表2...;#删除表
alter table tb_name drop foreign key fk_name;#删除外键,有外键是主表不能直接删除
#-------字段
alter table tb_name drop col_name;#删除表字段
#-------数据
delete from tb_name [where id = '1'];#删除表数据
TRUNCATE TABLE tb_name; #TRUNCATE是DDL语句，它先drop该表，再create该表。而且无法回滚
```

##### 3.改

```mysql
#-------表
alter table old_name rename [to] new_name;#修改表名
alter table tb_name engine=innodb;#修改库存储引擎
alter table tb_name convert to character set utf8;#修改编码
#-------字段
alter table tb_name modify id int(10);#修改字段类型
alter table tb_name change name name_new varchar(10);#修改字段名，可用于修改字段类型
alter table tb_name modify col_name varchar(10) after col_name_source;#修改字段位置
#-------数据
update table set age = 18, name = 'z3' where id = 2;#修改表数据
```

##### 4.查

```mysql
show tables; #查询当前库下所有表
show tables from db_name like '%user%';
select table_name from information_schema.tables where table_schema='database_name' and table_type='base table';
desc tb_name;#查看表结构
show columns from user;#查看表字段
select column_name from information_schema.columns where table_schema='database_name' and table_name='table_name';#查询表字段
show create table tb_name;#查看建表语句
SELECT Auto_increment FROM information_schema.`TABLES` WHERE Table_Schema='xtj' AND table_name = 't2' limit 1;#查询表的自增长值
#表数据查询
select * from tb_name [where];
#in 包含指定集合，not in 不包含指定集合, between and 范围查询，like 匹配查询 '_'为一个占位符，is null 空查询，is not null 非空，and 并，or 或，distince 不重复，order by 排序，group by 分组，having 过虑聚合，with rollup 统计所有分组数之和，limit index, num 限制查询数量，inner join 内连接，left join 左外连接，right join 右外连接，any/some 满足其一，all满足所有，exists 存在，not exists ，union [all] 合并，regexp 使用正则
```



#### 4.索引

##### 1.增

```mysql
create table table_name ([colum data_type] [unique|fulltext] [index|key]); #建表时字段末创建索引
create table tb_name ([col data_type],[unique|fulltext] [index|key] key_name(col(len))) #建表时，所有字段后加索引
create table tb_name(id int,name char(10),age int,key idx_multi (name,age));#创建组合索引
alter table tb_name add unique idx_name (c1,c2);
alter table t3 add unique key idx_name (name(len));#在已有表上加索引
create [unique|fulltext] index idx_name on tb_name (column(len));#使用create创建索引
```

##### 2.删

```mysql
alter table tb_name drop index idx_name;#删除索引
drop index idx_name on tb_name;#使用drop删除索引
#删除外键，2条语句
alter table emp drop foreign  key fk_emp;#解除约束
alter table emp drop index fk_emp;#删除外键
```

##### 3.查

```mysql
show index in/from tb_name;#查询表上创建的索引
SELECT * FROM student WHERE MATCH(column) AGAINST('text') #全文索引查询
```



#### 5.视图

##### 1.增

```mysql
create [or replace]  view v_name  AS select_statement;#创建视图
```

##### 2.删

```mysql
drop view (if exists) v_name;
delete from v_name where id = 5;
```

##### 3.改

```mysql
create [or replace]  view v_name  AS select_statement;#与创建一样
alter view v_name as select_statement; #修改视图
update v_name set id = 5;#更新视图
```

##### 4.查

```mysql
desc v_name;
show table status like '%v_name%';
show create view v_name;
select * from information_schema.views;#在views表中查看视力力图信息
```



#### 6.权限

##### 1.登陆

```mysql
mysql -uroot -p123456 -P3306 -h127.0.0.1 -e "expr" #-e 执行命令
```

##### 2.增

```mysql
#用户
create user 'user_name'@'host' identified by 'password';#创建用户
grant select,update on *.* to 'user_name'@'host' identified by 'password';#受权并创建
insert into mysql.user(host,user,password) values('host','user_name',password('123'));
#权限
grant all on *.* to 'user_name'@'host';#授所有权限
grant select(id,name) on 'db_name'.'tb_name' to 'user_name'@'host';#给列授权
```

##### 3.删

```mysql
drop user 'user_name'@'host';
delete from mysql.user where user='user_name' and host='host';
revoke select on *.* from 'user_name'@'host';
```

##### 4.改

```mysql
mysqladmin -uroot -p123 password 'new_pwd' #修改密码
update mysql.user set password=password('123') where user='user_name',host='host';
flush privileges;
set password=password('123');#修改当前用户密码
set password for 'user_name'@'host' = password('123');#修改指定用户密码
grant usage on *.* to 'user_name'@'host' identified by 'password';
mysqld --skip-grant-tables #使用此方式启动服务后，修改密码，用于root密码丢失
```

##### 5.查

```mysql
select host,user,password from mysql.user;#查询用户密码
show grant for 'user_name'@'host'; #查询用户权限

```



#### 7.备份与恢复

##### 1.备份

```mysql
mysqldump -uroot -p123  tb_name [tb_name] > backup/tb_name.sql#--single-transaction 不锁表 --master-data=2 记录binlog日志与位置 --databases 备份多个数据库 --all-databases 备份所有数据库 --socket=path,-S path 指定使用的套接字文件 
```

##### 2.恢复

```mysql
mysql -uroot -p123 db_name < backup/db_name.sql #使用mysql命令恢复
mysql > source backup/db_name.sql #使用source恢复
```

##### 3.数据迁移

```mysql
mysqldump -h 127.0.0.1 -uroot -p123 dbname | mysql -h www.abc.com -uroot -ppwd #迁移数据到www.abc.com机器上，版本相同
```



#### 8.日志文件

##### 1.查

```mysql
show master|binary logs;#查询binlog日志
mysqlbinlog binlog/binlog.000001 #查看日志内容
flush logs;#flush刷新log日志，自此刻开始产生一个新编号的binlog日志文件
show master status;#查看master状态，日志名称及position
show binlog events in 'binlog.000004' \G #查看日志事件信息
```

##### 2.删

```mysql
reset master;#删除所有binlog文件
purge master logs to 'binlog.000003'; #删除时间比binlog.000003最的所有日志文件
purge master logs before '20190519'; #删除比指定时间早的日志文件
```



#### 9.锁

##### 1.增

```mysql
#行排它锁
select * from t1 where name ='C' for update;
#行共享锁
select * from t1 where id =1 lock in share mode;

```

##### 2.查

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



#### 10.函数

##### 1.数学函数

- abs(x)：x的绝对值函数
- pi()：返回圆周率
- sqrt(x)：求x的平方根
- mod(x,y)：x被y除后的余数
- ceil(x)：向上取整
- floor(x)：向下取整
- rand([x])：随机数函数，x为种子，种子一样则随机数一样，值为(0 <= v <= 1.0)
- round(x)：x四舍五入
- round(x,y)：x四舍五入，保留小数点后y位，如y为负数，则保留小数点左y位
- sign(x)：符号函数，负返回-1，0返回0，正返回1
- pow(x,y)：求x的y次幂
- exp(x)：e的x次幂
- log(x)：x的自然对数
- log10(x)：x基数为10的对数
- radians(x)：转换为弧度
- degrees(x)：转换为角度
- sign，asin，cos，acos，tan，atan：三角函数和反三角函数

##### 2.字符串函数

- char_length(x)：计算字符长度，一个汉字长度为1
- length(x)：计算字节长度，一个汉字长度为3
- concat(s1,s2...)：连接字符串，有null则返回null，有
- concat_ws(s,s1,s2...)：以s为分隔符将s1，s2...连接，分隔符为null返回null，分隔符不为null忽略其它null
- insert(s1,index,len,s2)：返回s1从index开始的len个长度替换为s2后的串，从1开始
- lower/lcase，upper/ucase：转换大小写
- left/right(s,n)：获取s开始最左/右边的n个字符
- lpad(s,len,c)：返回s基础上长度为len的字符串，不够长度左边补c，够长度从左缩短为len字符。
- rpad(s,len,c)：返回s基础上长度为len的字符串，不够长度右边补c，够长度从左缩短为len字符。
- ltrim，rtrim，trim：去除两端空格
- trim(c from s)：去除s两端的c串
- repeat(s,n)：重复s串n次
- space(n)：返回由n个空格组成的串
- replace(s,s1,s2)：使用s2替换s中所有的s1
- strcmp(s1,s2)：比较s1和s2的大小，前大后返回1
- substring/mid(s,n,len)：返回s中从n开始长度为len的串，从1开始
- locate(c,s)：返回c在s中的位置
- instr(s,c)：返回c在s中的位置
- reverse(s)：字符串反转
- elt(n,s1,s2,...)：返回后面第n个位置的串
- field(s,s1,s2,s3...)：返回s在后面列表中第一次出现的位置，从1开始
- find_in_set(s,l)：返回s在列表l中出现的位置，列表以逗号分隔，从1开始

##### 3.日期函数

- currentdate/current_date()：将当前日期按照yyyy-mm-dd或yyyymmdd形式返回，curdate()+0，时转换成yyyymmdd形式的数字。
- surtime/current_time()：当前时间以hh:mm:ss或hhmmss形式返回
- now/current_timestamp/localtime/sysdate()：当前日期和时间yyyy-mm-dd hh:mm:ss或yyyymmddhhmmss格式
- unix_timestamp([date])：时间戳，date可以是时间或时间格式的字符串,GMT时间
- from_unixtime(timestamp)：把时间戳转换为日期格式，GMT时间
- utc_date/utc_time：UTC日期时间
- month/monthname(date)：获取月份和月份英文全称
- dayname/dayofweek/weekday(d)：星期全称，周的第几天
- week/weekofyear(d)：获取d为一年的第几周
- dayofyear/dayofmonth(d)：获取d为一年/月中的第几天
- year/quarter/month/day/hour/minute/second(d)：获取d的年/季度/月/日/时/分/秒
- time_to_sec(time)：将时间转换为秒
- sec_to_time：秒转换为时间
- date_add/adddate/date_sub/subdate/addtime/subtime/**datediff**：计算函数，参数为 d, interval x type
- date_format/time_format(d,f)：格式化日期/时间函数
- get_format(val_type,format_type)：获取模式，如 (datetime,'iso')

##### 4.条件判断函数

- if(expr,true_val1,false_val)：根据expr真值，返回后面值
- ifnull(v1,v2)：v1为null返回v2，不为null返回v1
- case expr whel v1 then r1 [when v2 then r2] [else m] end

##### 5.系统信息函数

- version()：查看系统版本号
- database/schema()：查询当前选择的数据库
- user/current_user/system_user/session_user：返回当前登陆账号
- charset/collation(str)：获取字符串字符集/排序方式
- last_insert_id()：查询库中最后的自增长值

##### 6.加解密函数

- password(s)：加密密码函数
- MD5(s)：md5加密
- encode/decode(str,key)：加解密字符串

##### 7.聚合函数

- count：统计行数，count(*)计算总行数，count(col)计算指定列行数，忽略空
- sum：求和
- avg：求平均数
- max：求最大值
- min：求最小值

##### 7.其它函数

- format(num,len)：四舍五入num，保留小数点后len位，不够补0，len为<=0保留整数

- conv(n,from_base,to_base)：进制转换

- bin/oct/hex(n)：返回n二(八/十六)进制的字符串

- char(n1,n2...)：将数字转换为char

- inet_ntoa(ip)：转换ip到点选地址，inet_ntoa('a.b.c.d')=a * 256^3 + b * 256^2 + c * 256 + d

- inet_ntoa(num)：将点选地址转换为ip

- get_lock(str,time)：结果为1说明得到一个名为str的锁

- is_used_lock(str)：返回当前连接id，锁正在被使用

- is_free_lock(str)：返回0说明锁在被使用

- release_lock(str)：返回1，释放锁

- benchmark(n,expr)：重复执行指定expr操作n次，可报告语句执行时间

- convert(str using gbk)：改变字符集

- cast/convert(x, as type)：改变数据类型 cast(100 as char(2))，convert('2019-05-20 10:10:10',TIME)

- group_concat(); #对分组后的某字段进行连接

  





