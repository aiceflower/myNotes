1.sql中过滤条件放在on和where中的区别？

​	join的过程是这样的，首先对两个表做笛卡尔积，on后面的条件是对这个笛卡尔积做一个过虑，left join中on对右表过虑，right join中on对左表过虑然后形成一张临时表，如果没有where就直接返回结果，如果有where就对临时表进一步过虑。

结论：在inner join中on和where没有区别，在left join和right join中on和where有区别。

2.MySQL线程容器类I_LIST\<THD>的作用?

- 执行 show processlist命令时，可快速拿到所有线程状态
- 执行kill命令时，能快速定位目标线程
- 在关闭mysql服务时，可依次关闭所有mysql线程
- 作为线程缓冲队列

**注**：线程相关操作，如创建、结束、跟踪线程状态都需要获得一个互斥锁。

3.Replication复制实现原理

1）主服务器将用户对数据库的改变操作以二进制方式记录在binlog日志中，然后由Binlog Dump线程将日志中的内容传给从服务器。

2）从服务器通过一个I/O线程将主服务器的二进制日志文件中的操作事件，复制到本地的Relay Log的中继日志文件中。（start slave后启动）

3）从服务器通过另一个SQL线程将Relay Log中的操作依次顺序地在本地执行。Relay Log与Binlog有相同的格式，SQL线程会自动删除Relay Log中已经被执行的事件。

4.Join的所有情况

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