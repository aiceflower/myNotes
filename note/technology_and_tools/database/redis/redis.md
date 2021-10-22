### Redis基础

#### 环境搭建

##### 1.安装redis
1.下载wget http://download.redis.io/releases/redis-4.0.0.tar.gz
2.解压 tar -zxvf redis-4.0.0.tar.gz
3.安装 make

##### 2.启动服务

1.运行 sh redis-4.0.0/src/redis-server
2.指定配置 sh redis-4.0.0/src/redis-server redis.conf

##### 3.配置
  redis.conf			
  bind 127.0.0.1 只能本地回环连接，注释掉该行可非本地连接
  protected-mode (yes是)是否启动保护模式，如果启动需要给客户端设置密码才能用程序访问
  daemonize 是否以守护进程方式启动(yes是)

##### 4.登陆
 redis-cli -h 192.168.1.103 -p 6379 -a password
#### Redis基本概念
##### 1.NOSQL简介
- NoSql:泛指非关系型的数据库。
- NoSQL数据库四大分类
  - 键值(key-value)存储的数据库:
    		这一类数据库主要会用到一个哈希表，这个表中有一个特定的键和一个指针指向特定的数据。如：redis，voldemort,Oracle BDB
  - 列存储数据库：
    		这部分数据库通常用来应对分布式存储的海量数据。键仍然存在，但他们的特点是指向了多列。如：HBase,Riak
  - 文档型数据库：
    		该类型的数据库模型是版本化的文档，半结构化的文档以特定的格式存储，比如JSON.文档类型数据库可以看作是键值数据库的升级版，允许之间嵌套键值。而且文档型数据库比键值数据库查询效率更高，如CouchDB,MongoDb
  - 图形数据库:
    		图形结构的数据库同其他行列以及刚性结构的SQL数据库不同，它是使用灵活的图形模型，并且能够扩展到多个服务器上，NoSql数据库没有标准的查询语言(SQL),因此进行数据库查询需要制定数据模型。许多NoSql数据库都有REST式的数据接口或者查询API。如：Neo4J,InfoGrid,Infinite Graph

##### 2.非关系性数据库特点

- 1.数据模型比较简单

- 2.需要灵活性更强的IT系统

- 3.对数据库性能要求较高

- 不需要高度的数据一致性

- 对于给定key，比较容易映射复杂的环境

##### 3.redis简介
- 以key-value形式存储，value有类型(有本地方法)、基于内存的(快)、非关系型的、分布式的、开源的。单线程，6.x版本之后有多线程的概念，但也是IO线程是多线程的，工作线程还是单线程。

- 使用场景：缓存(强)、存储(弱)

- 优点：
     对数据高并发读写
     对海量数据的高效率存储和访问
      对数据的可扩展性和高可用性

- 缺点：
     redis(ACID处理非常简单)
     无法做到太复杂关系数据库模型

- 键可以包含：(string)字符串、哈希、(list)链表、(set)集合、(zset)有集

- 数据都是缓存在内存中的，它可以周期性的把更新的数据写入磁盘或把修改操作写入追加到文件

- redis持久化方式
      rdb：定期把内存中的数据刷到硬盘
      aof:执行dml操作，把操作写到日志中(实时)

  ​	rdb + aof混合使用：先建数据快照rdb，然后增量对数据使用aof(redis4.0后)

- redis三种集群策略

  - 1.主从(redis1.0):主(可读写)从(只读)数据库，主从数据一模一样,主节点挂了之后，集群就不可用了
  - 2.哨兵(redis2.0):在主从的基础上再增加一台哨兵服务器，去监控主从服务器，当主节点挂了之后，采用一定的选举策略，选取一台最合适的从节点，升级为主节点。主从的数据都是一样的，无法做分布式数据库。
  - 3.集群(redis3.0):多主多从，主从都可增减，数据可移植

  单点故障：使用主从集群解决(主从)

  压力过大：使用分片集群解决(cluster 集群)

- 可以存入缓存的数据

  - 1.不需要高度一致性的数据如访问量，投票统计，在一定的时间间隔内保证数据一致就可以了。
  - 2.不太重要的，不涉及到钱的，可在放入到缓存

##### 4.Redis扩展 

​    水平/垂直扩展(硬盘内存不够的时候，扩展)

​	水平：添加机器(集群)

​	垂直：添加硬盘

##### 5.redis高并发场景下写操作比较慢是什么原因，如何解决

​    原因：服务开启了AOF功能，在并发写的时候都会记录日志，所以降低了redis的性能。
​	解决：可以多增加一些节点，分担高并发写的压力，或redis结合ssdb，它们两个之间做技术结合，在高并发写的发问下性能比较高。

##### 6.二进制安全

​	自己负责编码，redis只负责存储字节数组。要求客户端之间确定好编解码。类似的还有，kafka、zk、hbase。

##### 7.分流的几种形式

- 1.集群，将并发的请求分到不同的服务器上，可以是业务服务器，也可以是数据库服务器
- 2.分布式，分布式是把单次请求的多项业务逻辑分配到多个服务器上，这样可以同步处理很多逻辑，一般使用与特别复杂的业务请求。
- 3.CDN - 在域名解析层面的分流，例如将华南地区的用户请求分配到华南的服务器，华中地区的用户请求分配到华中的服务器。

#### redis基础语法

##### 1.redis基本数据类型

- 1.string
- 2.hash
- 3.list
- 4.set
- 5.zset

##### 2.字符串操作值

- 1.设置值 set name zhangsan
- 2.取值 get name ,(keys * 查询所有值)
- 3.删值 del name 
- 4.setnx  name (不存在则设置，存在不设置，返回0)
- 5.setex color 10 red (设置过期时间，10秒后过期为nil表示空，相当于null)
- 6.setrange name 5 si (从第几位开始替换，后面跟上替换的值，有几位替换几位)
- 7.批量设值 mset k1 v1 k2 v2 ...
- 8.批量取值 mget k1 v1 name ...
- 9.获取旧值并设置新值 getset k nv
- 10.递增1 incr age 
- 11.递减1 decr age 
- 12.递增指定长度 incrby age 3
- 13.递减指定长度 decrby age 3
- 14.字符串追加 append name lisi 
- 15.获取长度 strlen name 
- 16.判断是否存在 exists name

##### 3.哈希操作值

​	类似java中的hashMap

- 1.设值 hset hashname k1 v1 
- 2.取值 hget hashname k1 
- 3.批量设 hmset hashname k1 v1 k2 v2 
- 4.批量取 hmget hashname k1 k2 
- 5.hsetnx 
- 6.hincr/hincrby hdecr/hdecrby
- 7.hexists 
- 8.hlen 获取哈希中键数值
- 9.hdel 删除指定哈希的field
- 10.hkeys 返回哈希中所有字段
- 11.hvals ...value
- 12.hgetall 获取哈希中所有键值

##### 4.List

- 1.一个链表结构的集合，其主要功能有push,pop 获取元素等，更具体说它是一个双端链表结构，设计精巧，可作为栈，也可作为队列。
- 2.lpush 从头加入元素，先进后出(栈) lpush list1 hello/world
- 3.rpush 从尾部加入元素，先进先出(队列) rpush list2 hello /world
- 4.取 lrange list1/list2 0 -1 (从头部取到尾部)
- 5.插入元素 linsert list3 before "one" "three" 在list3集合中将three插入到one前
- 6.lset 将指定下标的元素替换掉 lset list1 0 "b" ,将第0个元素替换为b
- 7.lrem 删除元素，返回删除个数 lrem list1 2 "b" 删除前两个"B",删除前n个key
- 8.ltrim 保留指定key的值范围内的数据  ltrim list1 2 3 保留第二个到第三个元素，其它删除
- 9.lpop 从list头部删除元素，并返回删除的元素 lpop list1
- 10.rpop 从尾部删除元素，并返回删除的元素 rpop list1 
- 11.rpoplpush 从尾部删除从头部加
- 12.lindex 返回名称为key的list中index位置的元素 lindex list1 1 
- 13.llen 返回元素个数

##### 5.Set

- 1.set是string类型的无序集合，set是通过hashtable实现的，对集合可以取交集，并集，差集
- 2.加 sadd set1 aaa
- 3.看 smembers set1
- 4.删 srem 
- 5.spop 随机返回删除的key
- 6.sdiff 
- 7.sdiffstore 将返回的元素存到另一个集合中
- 8.sinter s1 s2 交
- 9.sinterstore  s3 s1 s2 在s3中存入s1和s2的交集
- 10.sunion 并
- 11.sunionstore 并存
- 12.smove 移动
- 13.scard 查看个数
- 14.sismember 判断
- 15.srandmember 随机返回一个元素

##### 6.zset

- 1.加 zadd set1 5 five 其中set1是集合名，5是顺序，five是值
- 2.看 zrange 0 -1 withscores
- 3.zrem
- 4.zincrby
- 5.zrangebyscore
- 6.zremrangebyrank 
- 7.zremrangebyscore 删除指定序号
- 8.zrank 从小到大排序
- 9.zrevrank 从大到小
- 10.zrangebyscore set1 2 3 withscores
- 11.zcard 
- 12.zcount 
- 13.zremrangebyrank set [from] [to] 删除索引
- 14.zremrangebyscore set [from] [to] 删除指定序号

##### 7.通用

- help 、help @generic、help @string

- 1.keys * 可模糊匹配(aa*)
- 2.exists 是否存在
- 3.expire 设置key过期时间,使用ttl查看剩余时间
- 4.persists 取消过期时间
- 5.select 选择数据库 0-15。默认进入的是0数据库
- 6.move [key][数据库下标] 转移数据到其它数据库
- 7.randomkey 随机返回数据库中的一个key
- 8.rename 重名名key
- 9.echo 打印命令
- 10.dbsize 查看数据库的key数量
- 11.info 获取数据库信息
- 12.config get 实时传储收到的请求(返回相关的配置信息) config get * 返回所有
- 13.flushdb 清空当前数据库
- 14.flushall 清空所有数据库
- 15.cluster info 查看集群状态
- type k1 查看k1类型
- object encoding k1 查看类型

### redis中级操作
#### 1.主从数据库

​	主可读写，从只读，主数据发生写操作的时候会同步到从数据库，主从数据库内容一样

- 1.建立三台redis服务器一主二从
- 2.主服务器配置正常，在从服务器配置上添加一行 slaveof ip port
- 3.如果主设置了密码，从还要添加masterauth <master-password>
- 使用info命令可以查看服务器是主还是从 role:master

#### 2.哨兵

- 1.监控redis服务器运行状况主要有两个功能
  - 1.监控主数据库和从数据库是否正常运行
  - 2.主数据库出现故障，可以自动将从数据库转换为主数据库，实现自动切换
- 2.配置
  - 1.另准备一个或多个redis服务器做为哨兵服务器
  - 2.复制修改sentinel.conf文件配置
    - 1.sentinel monitor mymaster 主节点ip 主节点端口 投票次数(有几票时可先为主)
    - 2.sentinel down-after-milliseconds mymaster 30000  每隔多久去监控一次（毫秒），默认30秒
    - 3.sentinel parallel-syncs mymaster 2 配置从节点个数
    - 4.启动哨兵 ./redis-server ../sentinel.conf --sentinel &
    - 5.查看哨兵信息，哪一台服务器都可以
      ./redis-cli -h 哨兵ip 哨兵端口(26379) info Sentinel
    - 6.主节点故障回复后重新加入后变为从节点，无法再变为主节点

#### 3.redis 事物

- 1.开启事物 multi 提交 exec discard取消事物 ，没有回滚
- 2.redis不能保证同时成功，或失败时回滚

#### 4.持久化机制（几乎所有持久化都使用这两种）

- 1.rdb(默认，快照) 周期性的(比如5秒)将内存的数据同步到硬盘上。也可使用多秒内，出现多少数据变化时（增、删、改）创建快照。不适合实时系统。全量、丢失量比较大，体积相近，快恢复。

- 2.aof append only file (执行dml操作时将持久化操作写到日志里，)

- 3.启动aof

  设置 appendonly yes(类似mysql数据库的undo) 有三种策略

  - 1.appendfsync always ,收到写命令就写入磁盘，效率慢，但保证完全的持久化（生产中使用这种，做了集群之后，效率也还好)（一般使用这种）
  - 2.appendfsync everysec 每秒写一次 在性能和持久化方面做了很好的折中
  - 3.appendfsync no 完全依赖os 性能好，无法保证持久化

#### 5.消息的发布与订阅

- 1.订阅 subscrip cctv 
- 2.发布 publish cctv aaa 

#### 6.集群搭建

- 1.至少三台redis服务器

- 2.修改如下配置
  		daemonize yes
    		port 6379
    		bind ip
    		dir /usr/local/redis_cluster 
    		cluster-enabled yes 
    		cluster-config-file nodes.conf 
    		cluster-node-timeout 5000
    		appendonly yes 

- 3.redis集群需要安装ruby,rubygems,gem install redis 

- redis集群命令

  - create 构建集群
  - fix 单点修复
  - check 集群验证
  - add-node 添加节点
  - del-node 删除节点
  - reshard 重新分片

- 4.动态添加集群

  - 1.redis-trib.rb add-node newIP:newPort oldIp:oldPort
  - 2.reshard 重新分配槽，动态新加了master节点没有槽(添加主节点 1,2)
  - 3.登陆一个集群节点 cluster replicate masterId(指定主节点id) (添加从节点 1,3)

- 5.删除从节点  redis-trib.rb del-node ip:port slaveId(从节点id)

- 6.删除主节点

  - 1.把数据(slot槽)移动到其它节点上，只能把需要删除的节点的数据迁移到一个节点上，做不到平均分配
  - 2.redis-trib.rb  reshard ip:port (需要删除的节点ip,port) 
  - 3.输入槽个数
  - 4.输入需要移动到哪个节点的id
  - 5.done
  - 6.执行删除命令

- 查看集群状态

  cluster info 查看集群状态

#### Redis 扩展

##### 第六种数据类型：布隆过虑器类型。

​	redis是基于模块的，布隆过虑器基于C语言实现，在启动redis的时候把布隆过虑器模块加到redis进程中，redis就带布隆过虑器了。

##### 如何解决高并发

- 1.前端使用Nginx或LVS或Keepalived或haproxy负载均衡器，来降压，分流
- 2.再往后nginx挂几个nginx，不同的域名在业务上做负载均衡
- 3.在java层面使用一些高并发的容器，或信号量来限流
- 4.最后在数据库层面上，分表分库或读写分离或者使用缓存，把一些可以放到缓存中的数据从数据库中拆开来放到缓存中。

​    从以上几个层面来解决高并发

### Redis高级操作

#### redis当MQ使用

​	redis性能比较高，可设置过期时间等等，使用List实现。

​	但不建议用：无法完成MQ的很多操作：过滤，顺序，消息事务等，负载均衡

#### 分布式锁

##### **分布式锁的实现方式：**

1. 基于数据库(唯一索引、乐观锁等)
2. 基于Redis的分布式锁
3. 基于ZooKeeper的分布式锁
4. Redisson实现Redis分布式锁

##### 基于Redis的分布式锁

**确保分布式锁的四个条件**

- 互斥性。在任意时刻，只有一个客户端能持有锁。
- 不会发生死锁。即使有一个客户端持锁的期间奔溃则没有动解锁，也能保证后续其他客户端能加锁。
- 具有容错性。只要大部分的Redis节点正常运行，客户端就可以加锁解锁。
- 加锁和解锁必需是同一个客户端。自己不能把别人的锁给解了。

**加解锁**

```
#加锁
redis.set(lockKey, requestId, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, expireTime);
#五个参数含义
#lockKey:锁
#requestId:代表是谁加的锁
#nx:当key不存在时在set
#px:要给key加过期时间
#expireTime:过期时间
#解锁
String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
jedis.eval(script, Collections.singletonList(lockKey), Collections.singletonList(requestId));
#eval()方法是将Lua代码交给Redis服务端执行
#使用lua确保上述操作是原子性的。
#关于redis执行Lua的原子性：就是在eval命令执行Lua代码的时候，Lua代码将被当成一个命令去执行，并且直到eval命令执行完成，Redis才会执行其他命令。
```

**缺点：**

不支持重入锁，不支持阻塞等待。

##### 基于Mysql的分布式锁

**实现**

- 利用mysql唯一索引

  1.加唯一索引 2.加锁：执行insert，若报错，表明加锁失败 3.解锁：执行delete

- 使用乐观锁

**缺点**

- 1.锁无超时自动失效机制，有死锁风险
- 2.不支持锁重入，不支持阻塞等待
- 3.操作数据库开销大，性能不高

##### 基于ZooKeeper

**实现**

​	**加锁**：在/lock目录下创建临时有序节点，判断创建的节点序号是否最小。若是，则表示获取到锁；否，则则watch /lock目录下序号比自身小的前一个节点

​	**解锁**：删除节点

**缺点**

​	需单独维护一套zk集群，维保成本高



#### 应用场景

##### String

- 字符串操作：session、token、轻量级文件系统

- 数值计算：统计、计数、限流

- bitmap位图：ascll一个字节共表示127字符，左边高位是0。

  二进制基本操作。

  - setbit key len 1/0
  - bitcount key 0 1
  - bitop and/or/ k3 k1 k2 将k1和k2进行位操作后存入k3

  场景：

  ​	统计 任意时间窗口内，用户登陆的次数(天)，使用bitmap每一位代表一天，登陆置1，未登陆置0

  ​	统计 任意时间窗口内，活跃用户数。key为天(20200101),值中每一位代表某一个用户该天是否登陆。统计时，用或所有天数，然后统计结果1的个数。

  ​	查询12306一辆车任意站点区间有没有座位可以买。每个站点用一个bitmap位图长度为总站点数，有人买票后，每个站点的包头不包尾区间置1，查询时将所有站按位或，区间中全是0则可买，否则不可以。实际12306，以空间换时间，把所有可能都生成。

##### List

​	双向链表，基本操作lpush、lpop、rpush、rpop 同向为栈，反向为队列。ltrim删除指定区间之外的所有元素。

​	场景：评论列表、消息队列、替代java容器

##### Hash

​	hashTable、hashMap

​	场景：聚合数据(某用户，不同模块(订单、商品、登陆等)的数据聚合)，详情页、用户详情

##### Set

​	无序，不重复。

​	场景：

- 获取随机元素。(抽奖、验证码、扑克牌游戏)

  srandmember k1 count (count大于0取count个元素，count大于len取len个不重复，count小于0，取count绝对值个可重复的元素，个数小于len也可能重复)

  抽奖：共10个用户  srandmember k1 10

  解决投票问题：srandmember k1 -10 加入只有4个人，看哪个情况出现次数多。

  抽完没了：spop k1  每次一个，抽完不存在了。

- 集合操作，交并差(共同好友、好友[文章/游戏等]推荐)

  做推荐系统。k1,k2中有各处认识的朋友，给k2推荐差集 sdiff k1 k2，给k1推荐差集 sdiff k2 k1 推荐相互不认识的人。 

- 多实例：集合操作消耗资源，可以多实例，服务拆分，不同场景分给不同实例做。

##### ZSet

​	对于一个元素，有分值，有排名

​	场景:(排行榜、评论无序分页)

​	如何排序？压缩表(数据量小)，跳跃表(元素比较大)

#### 灵魂拷问

1.如何学习

​	架构(宏观上干什么事情，用什么技术，流程是什么) --> 产生问号(这是什么，怎么实现的，有什么用) --> 驱动学习  --> 人(费曼学习法，跟他讲明白)

2. redis是单线程，如何处理多个并发的客户端连接？

   使用多路利用器epoll,然后程序自主去读取数据。因为只有一个线程，所以连接只能使用队列一个一个处理(具体先处理哪一个不确定)。

   串行：input -> 计算 -> output 完事后再处理下一个io 

3. 单线程如何利用多CPU

   早期：创建多实例，多个进程隔离。(数据不可见)

   现在：多线程(多IO线程，处理INPUT、OUTPUT)，单线程处理计算

4. 

5. 云原生



扩展：

Web Server与Web容器

- Web Server 静态网站  来一个url 读取文件返回给用户
- Web 容器 来一个url 调用方法(程序逻辑) 返回给用户

Nginx：

nginx是一个web server官方5W连接，并发读数据返回，没有写的操作。

微服务：

​	四点：

- AKF划分拆分

  出现单点故障问题：做主备，主挂了，备可以顶上来。高可用(x轴)

  根据业务划分：将集合、计算等占io的数据分到一台机器；将用户的点击量，访问量等短小的快速返回的放到另一台机器上(Y轴)

  如果单台的业务依然很大，内存吃不消，可使用分片将业务数据分片(取余操作)到不同机器上(Z轴)

- 服务无状态：session共享，需要把服务中共享的变量提出来。为了弹性扩展时方便。

- 通信无状态

- 前后端分离

云：弹性扩缩容、按需分配、可度量计价、多租户。

**Redis与MemCache比较**

redis与memcache都是基于内存的。

- memcache：基于K-V，V是字符串，没有类型，想获取数组第二个元素，需要获取全数据，然后做序列化。数据向计算移动。

- redis：基于K-V，V有类型。想获取数组第二个元素，直接获取。有本地方法，可帮助数据计算。计算向数据移动。

  移动数据成本大于移动计算。

**数据分片处理(Redis或其它技术)**

- 1.客户端自己实现取余等(太麻烦)
- 2.使用代理实现，让代理去计算(代理会出现单点问题)
- 2.redis分片(技术自身实现)

如何聊：

IO epoll
单线程 && IO threads
聊场景
从业务入手聊业务，公司有这个业务，那个业务，分别用Redis的这个那个点实现了它。

聊分布式、中间件、集群等

业务没用到这些怎么办？把促销，秒杀等整合进去可以不？使用这些可以把redis等技术糅合进去。