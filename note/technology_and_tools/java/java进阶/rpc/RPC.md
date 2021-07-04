RPC

1.什么是RPC

​	RPC (Remote Procedure Call)即远程过程调用，是分布式系统常见的一种**通信方式**.

​	注：多系统数据交互方式：分布式消息队列、HTTP请求调用、数据库和分布式缓存等。RPC和HTTP调用是端到端直接数据交互，无中间件。RPC是长连接数据交互，HTTP是短链接。

2.RPC的本质

​	序列化方式 + 通信方式

| 序列化方式(rpc序列化框架)           |特点|
| -------------------- |---|
| java.io.Serializable |只支持java、数据大、效率低|
|Hessian|跨语言、数据小、效率高(小数据传输好)|
|google protobuf|跨语言，效率高(大数据传输)|
|facebook Thrift|跨语言，效率高(大数据传输)|
|json: Jackson、Gson、FastJson|可读性高，key会有冗余|
|xmlrpc(xstream)|可读性高、数据冗余较大|

**通信方式**

最古老最胡效，永不过时的，TCP/UDP的二进制传输。所有通信方式底层都是它们

HTTP、SMTP、FTP

3.不同协议加序列化方式衍生的服务

| 协议 | 序列化方式 | 服务    |特点|
| ---- | ---------- | ---- | ---- |
| http | xml | webservice |数据冗余|
| http | json | RestFul |数据冗余|
| http | Xml | webservice |数据冗余|
| http | Xml | webservice |数据冗余|
| http | Xml | webservice |数据冗余|

4.RPC框架
传输协议、序列化方式、服务注册、服务发现、服务治理、服务监控、服务负载均衡等
5.其它

RMI Remote Method Invocation Java内部的分布式通信协议

JMS Java Message Service JavaEE中的消息框架标准，为很多MQ所支持

6.总结

RPC远程方法调用，这只是一个统称，重点在于方法调用。可以有很多种方法实现。如RMI RestFul去实现， 当然不会这么用，RMI不跨语言，RestFul效率低。多用于服务器集群间通信，因此没跟使用更加高效，短小的传输模式以提高效率。