1.sql中过滤条件放在on和where中的区别？

​	join的过程是这样的，首先对两个表做笛卡尔积，on后面的条件是对这个笛卡尔积做一个过虑，left join中on对右表过虑，right join中on对左表过虑然后形成一张临时表，如果没有where就直接返回结果，如果有where就对临时表进一步过虑。

结论：在inner join中on和where没有区别，在left join和right join中on和where有区别。