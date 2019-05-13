## Life is short, you need MySql

### 一、Mysql需知

- Mysql设计之初的目标：成为高性能、普通用户支付得起的数据库服务器和工具。
- Mysql的使命：为所有人贡献一个经济实惠并且性能卓越的数据库软件。
- Mysql的独到之处：插件式存储引擎，存储类别的粒度小到每个表，可最大程度利用各引擎的优点，避免缺点。

### 二、数据库管理系统

#### 1.数据库系统的分类

三种觉见的数据库系统：面向对象型、关系型、对象关系型

#### 2.数据库管理系统功能模块

- 网络客户端连接：客户端访问
- 查询解析和优化：查询接口、查询处理、查询优化、查询执行
- 存储引擎：文件读取

#### 3.查询语句处理

1）语句合法性检查：对SQL语句的语法进行检查、看其是否符合语法规则。

2）主义检查：对语句中的字段，表等进行检查

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

![Mysql框架和模块](<https://raw.githubusercontent.com/aiceflower/assets/master/img/mysql/mysql_frame_and_module.png>)

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

注：SELECT @@join_buffer_size;可查看Mysql为每个连接缓冲分配的大小，explain输出中Using join buffer表示使用缓存算法

##### 3）Mysql排序实现

排序不仅仅用于order by ,同样group by 也会用到排序，Mysql有两类方式进行排序：

- **作用 range、ref、index读写方式**：explain的输出 range、ref、index是描述对索引的读取方式。这类方式获得的输出都是按索引的顺序排序的
- **filesort排序算法**：将一组记录按照快速排序放入到内存缓存，然后这几个内存缓存按合并算法排序 @@sort_buffer_size

***执行排序的三种方法***：

- **使用已排序索引**：Explain不提及任何filesort #有索引且排序的是索引字段
- **在表单上使用filesort**：using filesort #如果是多表联合，先单表filesort再对排序后进行join
- **将join结果先放入临时表，然后使用filesort**：using templorary; using filesort

