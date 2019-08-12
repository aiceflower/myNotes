1.查看cpu信息
	cat /proc/cpuinfo 
	其中processor数量决定了线程数

2.删除以减号开头的文件（创建也是一样）

- rm -rf ./-xxx
- rm -rf -- -xxx

3.linux多行vi编辑

ctrl-v ---> 按j选择行 --> 按 I 进入编辑模式 (x是删除) --> 输入字符 --> 按ESC