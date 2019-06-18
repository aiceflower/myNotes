#### 1.sql中过滤条件放在on和where中的区别？

​	join的过程是这样的，首先对两个表做笛卡尔积，on后面的条件是对这个笛卡尔积做一个过虑，left join中on对右表过虑，right join中on对左表过虑然后形成一张临时表，如果没有where就直接返回结果，如果有where就对临时表进一步过虑。

结论：在inner join中on和where没有区别，在left join和right join中on和where有区别。

#### 2.MySQL线程容器类I_LIST\<THD>的作用?

- 执行 show processlist命令时，可快速拿到所有线程状态
- 执行kill命令时，能快速定位目标线程
- 在关闭mysql服务时，可依次关闭所有mysql线程
- 作为线程缓冲队列

**注**：线程相关操作，如创建、结束、跟踪线程状态都需要获得一个互斥锁。

#### 3.Replication复制实现原理？

1）主服务器将用户对数据库的改变操作以二进制方式记录在binlog日志中，然后由Binlog Dump线程将日志中的内容传给从服务器。

2）从服务器通过一个I/O线程将主服务器的二进制日志文件中的操作事件，复制到本地的Relay Log的中继日志文件中。（start slave后启动）

3）从服务器通过另一个SQL线程将Relay Log中的操作依次顺序地在本地执行。Relay Log与Binlog有相同的格式，SQL线程会自动删除Relay Log中已经被执行的事件。

#### 4.Join的所有情况？

- A和B公共部分：SELECT <select_list> FROM tba a **INNER** JOIN tbb b ON a.key = b.key

- A的全部：SELECT <select_list> FROM tba a **LEFT** JOIN tbb b ON a.key = b.key
- B的全部：SELECT <select_list> FROM tba a **RIGHT** JOIN tbb b ON a.key = b.key

- A除去与B的公共：SELECT <select_list> FROM tba a **LEFT** JOIN tbb b ON a.key = b.key WHERE **b.key** IS NULL

- B除去与A的公共：SELECT <select_list> FROM tba a **RIGHT** JOIN tbb b ON a.key = b.key WHERE **a.key** IS NULL

- A和B的全部：SELECT <select_list> FROM tba a **FULL OUTER** JOIN tbb b ON a.key = b.key

  mysql **不支持full**

  SELECT <select_list> FROM tba a **LEFT JOIN** tbb b ON a.key = b.key 

  **UNION** 

  SELECT <select_list> FROM tba a **RIGHT JOIN** tbb b ON a.key = b.key 

- A和B的全部除去A与B的公共：SELECT <select_list> FROM tba a **FULL OUTER** JOIN tbb b ON a.key = b.key WHERE **a.key** IS NULL OR **b.key** IS NULL

  mysql **不支持full**

  SELECT <select_list> FROM tba a **LEFT JOIN** tbb b ON a.key = b.key  WHERE b.key IS NULL

  **UNION** 

  SELECT <select_list> FROM tba a **RIGHT JOIN** tbb b ON a.key = b.key where a.key IS NULL

#### 5.如何批量插入千万级数据？

1.建立表

```mysql
CREATE TABLE dept(
id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
deptno MEDIUMINT UNSIGNED NOT NULL DEFAULT 0,
dname VARCHAR(20) NOT NULL DEFAULT "",
loc VARCHAR(13) NOT NULL DEFAULT ""
)ENGINE=INNODB DEFAULT CHARSET=UTF8MB4;

create table emp(
id int unsigned primary key auto_increment,
empno mediumint unsigned not null default 0,
ename varchar(20) not null default "",
job varchar(9) not null default "",
mgr mediumint unsigned not null default 0,
hiredate date not null,
sal decimal(7,2) not null,
comm decimal(7,2) not null,
deptno mediumint unsigned not null default 0
)engine=innodb default charset=utf8mb4;
```

2.设置参数 (创建函数时会报一个错)

```mysql
set global log_bin_trust_function_creators=1
```

3.创建函数(有返回值)，保证每条数据都不同。<span style='color:red'>【第2步不设置这步会报错】</span>

```mysql
#随机产生字符串
delimiter $$ #设置结束符号
create function rand_string(n int) returns varchar(255)
begin
	declare chars_str varchar(100) default 'abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ';
	declare return_str varchar(255) default '';
	declare i int default 0;
	while i < n do
	set return_str = concat(return_str,substring(chars_str,floor(1+rand()*52),1));
	set i = i + 1;
	end while;
	return return_str;
end $$
#随机产生部门编号
delimiter $$
create function rand_num() returns int(5)
begin
	declare i int default 0;
	set i = floor(100 + rand() * 10);
	return i;
end $$
#如果需要删除
drop function rand_num;
```

4.创建存储过程(无返回值)

```mysql
#往emp表插入数据
delimiter $$
create procedure insert_emp(in start int(10),in max_num int(10))
begin
declare i int default 0;
set autocommit = 0;#设置自动提交为0
repeat
set i = i + 1;
insert into emp(empno, ename, job, mgr, hiredate, sal, comm, deptno) values((start+i),rand_string(6),'SALESMAN',0001,curdate(),2000,400,rand_num());
until i = max_num
end repeat;
commit;
end $$
#往dept表插入数据
delimiter $$
create procedure insert_dept(in start int(10),in max_num int(10))
begin
declare i int default 0;
set autocommit = 0;#设置自动提交为0
repeat
set i = i + 1;
insert into dept(deptno, dname, loc) values((start+i),rand_string(10), rand_string(8));
until i = max_num
end repeat;
commit;
end $$
```

5.调用存储过程

```mysql
#部门表
delimiter ;
call insert_dept(100,10);
#员工表
delimiter ;
call insert_emp(100001,500000);
```

#### 6.MyISAM和InnoDB中是否可以没有主键？

​	InnoDB中不一定要定义主键。如果有主键则以主键为索引构建索引及数据，如果没有定义主键，则会选择合适的列(**6位长整形**)做为主键去构建索引。如果每一 列都不ok，则会自动生成一个隐形列构建索引。

#### 7.InnoDB中为什么没有MYI文件？

​	InnoDB是聚集索引(索引顺序与物理存储顺序一致)，主键索引的叶子节点存放数据，即索引文件和数据文件是同一个。如果是非主键索引的叶子节点存放的是主键，避免了数据冗余。

#### 8.为什么建议使用自增id做为主键？

​	每次插入数据都在结尾，可以减少磁盘碎片，数据连续的放在page中。如果是其它的数据做主键，如uuid，则每次在插入数据时需要移动很多数据（数组插入数据会移动后面所有的数据），增加碎片。而且在非主键索引时叶子节点存储空间在，查询时判断更耗时。

#### 9.分布式ID生成 - 雪花算法是什么？