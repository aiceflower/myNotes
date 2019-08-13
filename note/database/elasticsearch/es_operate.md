### 一、概念

#### 1.基础概念

***简介***：

Elasticsearch是一个基于Lucene 的实时分布式搜索和分析引擎 ，用于全文搜索、 结构化搜索、 分析 ，可以在数以百计的服务器上处理PB级别的数据 。

*ES与传统关系性数据库对比*：

?	Relational DB -> Databases -> Tables -> Rows -> Columns
?	Elasticsearch -> Indices -> Types -> Documents -> Fields 

***不同索引***：

- 索引(名词)：一个索引相当于传统数据库中的数据库，它是文档存储的地方
- 索引(动词)：索引一个文档，表示把文档存储到index中，就像sql中的insert，但区别是如果文档已经存在会覆盖旧文档
- 倒排索引：类似传统数据库中为某一列加一个索引来加速检索，ES中使用倒排索引达到相同目的

***操作请求***：

?	新建、 索引和删除请求都是写(write)操作， 它们必须在主分片上成功完成才能复制到相关的
复制分片上。	

***映射***：

?	映射(mapping)机制用于进行字段类型确认，将字段映射为( string , number , booleans , date 等) 。

***分析***：

?	分析(analysis)机制用于进行全文文本(Full Text)的分词， 以建立供搜索用的反向索引。

?	当查询全文(\_all)时，会分析待查字符串，查询确定字段时不会分析该字符串(可自指定)。

#### 2.交互

***与java交互***：

?	通过节点客户端或传输客户端与ES进行交互

?	**节点客户端**：(9300端口)

?	节点客户端作为一个非数据节点加入到本地集群中。换句话说，它本身不保存任何数据，但是它知道数据在集群中的哪个节点中，并且可以把请求转发到正确的节点。

?	**传输客户端**：(9300端口)

?	轻量级的传输客户端可以将请求发送到远程集群。它本身不加入集群，但是它可以将请求转发到集群中的一个节点上。

*与其它语言交互*：

?	使用 RESTful API 通过端口 *9200* 和 Elasticsearch 进行通信

### 二、基础操作

#### 1. 增

***创建索引***：put /索引名

***添加数据***：PUT /索引名/类型名/id data(Json 数据)

***创建索引指定分析器***：

```json
{
    "mappings":{
        "tweet":{
            "properties":{
                "tweet":{
                    "type":"string",
                    "analyzer":"english"
                },
                "date":{
                    "type":"date"
                },
                "name":{
                    "type":"string"
                },
                "user_id":{
                    "type":"long"
                }
            }
        }
    }
}
```





#### 2.删

***删除索引***：DELETE /索引名

***删除数据***：DELETE /索引名/类型名/id

#### 3.查

***查询所有索引***：GET /_cat/indices?v

***查询某类型下所有数据***：GET /索引名/类型名/_search

***简单条件查询***：GET /索引名/类型名/_search?q=field1 : value1&q=ield2 : value2(value前加"+"为精确匹配)

?	若没有指定字段， 查询字符串搜索（ 即q=xxx） 使用 _all 字段搜索 ，ES把所有字段字符串字段值连接起来放在一个大字段\_all中,进行搜索。 

***在以 g 或 u 开头的索引的所有类型中搜索*** ：/g*,u*/_search 

在所有文档的user***中查询***：/\_all/user/_search 

***全文索引***(返回相关性最大结果集)：

```json
GET /index/type/_search
{
    "query" : {
        "match" : {
            "about" : "rock climbing"
        }
    }
}
```

***多文档查询***：

```json
POST /_mget
{
    "docs" : [
        {
            "_index" : "website",
            "_type" : "blog",
            "_id" : 2
        },
        {
            "_index" : "website",
            "_type" : "pageviews",
            "_id" : 1,
            "_source": "views"
        }
    ]
}
```



#### 4.改

***修改部分字段***：（还有其它几种更新方式，未列出）

方式一：

```json
PUT /索引名/类型名/id/_updatepretty #更新只能用POST不能用PUT
{
  "doc": {
    "字段名" : "新值"  
  },

"detect_noop": false #true:只有原来的source和新的source存在不同的字段情况下才会重建索引, false:不管内容有没有变化都会重建索引，这一点可以通过version的值的变化来发现
}

```

方式二：

```json
POST /索引名/类型名/id/_updatepretty
{
  "script": {
     "inline": "ctx._source.last_name = 'Smith';ctx._source.age  -= params.count", #多个字段用分号
     "params": {
      "count": 3
    }
  }
}
```

***批量操作***：

```json
POST /索引名/类型名/_bulk #遇到操作异常继续执行其它操作，最后返回各自执行结果
{"update":{"_id":"1"}} #action=更新
{"doc": { "字段": "值" } }#request body
{"delete":{"_id":"2"}}#删除
```

注：action与request body配套，action可取create、index、update、delete



### 三、高级操作

#### 1.查询

***单条件过虑查询***：

```json
GET /[index][/type]/_search
{
  "query" : {
        "bool" : {
            "filter" : {
                "term": {
                  "FIELD": "VALUE"
                }    
            },
            "must" : {
                "match" : {
                    "gs" : "cmuclick"
                }
            }
        }
    }
}
```

***多条件过虑查询***：

```json
{
  "query": {
    "bool": {
      "must": {
        "multi_match" : {
          "query":      "value1 value2 ...",#过虑条件
          "type":       "best_fields",
          "fields":     [ "field" ],#待过虑字段
          "operator":   "or" 
        }
      },
      "filter": {
        "bool": {
          "should": [#查询条件
            ...
          ]
        }
      }

    }

  }
}

```



***按时间聚合查询***：

```json
{
  "size": 0,
  "aggs": {
    "聚合名称": {
      "date_histogram": {          //es提供的时间处理函数
        "field": "createDate",     //需要聚合的字段名字
        "interval": "hour",        //按小时group by
        "format": "yyyy-MM-dd HH", //返回值格式化，HH大写，不然不能区分上午、下午
        "min_doc_count": 0,        //最小文档数
        "extended_bounds": {       //查询结果范围
          "min": "2016-12-19 01", //格式与format一致
          "max": "2016-12-19 23"
        }
      }
    }
  },
  "query": { //查询条件
    ...
  	}
  }
}
```

***多重聚合***：

```json
{  
  "size": 0,
  "aggs": {  
    "聚合名称": {#第一次聚合，按小时等统计
      ...
      },
      "aggs": { #子聚合
        "distinct_colors" : {#去重聚合
        "cardinality" : {
          "field" : "client.keyword"
        }
      }
      }
    }
    
  },  
  "query": { #查询条件
   	...
  }  
}
```

#### 2.排序，分页，返回值

```js
{
  "query": { "match_all": {} },
  "from": 0, #分页
  "size": 20,
  "_source": ["account_number","age","address"], #控制返回字段
  "sort": [ #排序
    {
      "account_number": {
        "order": "asc"
      }
    }
  ]
}
```

#### 3.测试分析器

```json
GET /_analyze
{
  "analyzer": "standard",
  "text": ["Text to analyze"]
}
```

#### 4.查询字段映射

```js
GET /索引名/_mapping/类型名
```

#### 5.设置映射

```json
POST /索引名/类型名
{
   "data": {
      "mappings": {
        "age": {
          "type": "integer",
          "index": "not_analyzed"#指定索引方式
        },
        "name": {
          "type": "keyword"
        }
      }
   }
}
```

注：**index**字段含意

- analyzed ：先分析，再索引，即用全文索引
- not_analyzed ：不分析，直接索引
- no：不索引，即搜索不到

### 四、特殊操作

#### 1.注意事项

- match不支持多条件查询

#### 2.代码片段

- 查询所有索引信息： /_cat/indices?v[&h=health,status,index]
- 查询集群健康： /_cluster/health

#### 3.请求参数

- pretty ：美化输出

- _source：只返回 _source中的字段

- op_type=create 、_create：创建文档

- routing=value：指定数据作用分片路由值【见路由】

- retry_on_conflict：设置失败重试次数

- timeout：指定超时时间

  超时返回目前顺利返回结果 ，关闭连接，后台不会停止其它分片执行=，使用超时只是业务需求。 



### 五、其它

#### 1.并发乐观锁

?	修改时指定版本可实现乐观锁

PUT /索引/类型/idversion=版本号&version_type=external

**注**：当指定version_type=external时版本高于ES中的版本可修改成功，不指定时版本与ES中版本一致才能修改成功

#### 2.分词器

?	中文 ik_smart

### 六、核心概念

#### 1.索引

***正向索引***(forward index)：

?	通过id去查询关键词，出现的次数以及出现的位置列表，也即通过key去找value。

?	这种索引需要扫描索引库中的所有数据，找出所有包含待查关键词的文档，再根据打分模型进行打分，排出名次后呈现给用户。但数据量巨大无法满足实时返回待查结果。

***倒排索引***(inverted index)：	

?	通过关键词去查询id，每个关键词对应一系列文件，主些文件中都出现这个关键词。也即通过value去查询key。

#### 2.集群工作原理

***去中心化***：

?	没有中心节点，这是对于集群外部来说的，从外部看来你可以与任何一个节点通信。

***节点***：

?	一个节点**(node)**就是一个Elasticsearch实例。

***分布式***：

?	多个能独立运行的计算机（称为结点）组成。各个结点利用计算机网络进行信息传递，从而实现共同的“目标或者任务”。

***集群*** ：

?	一个集群**(cluster)**由一个或多个节点组成，它们具有相同的 cluster.name ， 它们协同工作， 分享数据和负载。当加入新的节点或者删除一个节点时， 集群就会感知到并平衡数据。	es会自动发现同一个网段相同cluster.name的节点 自动组成集群。

?	集群中一个节点会被选举为主节点**(master)** ，它负责管理集群的状态（节点的发现和删除），包括管理分片的状态，副本的状态。当集群中主节点挂掉后，会从其它节点中选举一个节点作为主节点。

***集群健康*** ：

集群健康有三种状态(GET /_cluster/health )： 

- green：所有主要分片和复制分片都可用 
- yellow：所有主要分片可用， 但不是所有复制分片都可用 
- red ：不是所有的主要分片都可用 



***分片***：

?	是一个Lucene实例， 并且它本身就是一个完整的搜索引擎 ，一个最小级别**“**工作单元 “，它只是保存了索引中所有数据的一部分。  索引是一个存储关联数据的地方，实际上它只是一个用来指向一个或多个分片**(shards)**的逻辑命名空间 。

?	文档存储在分片中，然后分片分配到集群中的节点上，对集群扩容或缩小时，ES会自动在节点间迁移分片，全集群保持平衡。

***主分片和副分片***：

?	分片可以是主分片(primary shard)或者是复制分片(replica shard)，每个文档属于一个单独的主分片，索引创建完成主分片就确定了。复制分片是主分片的一个副本，提供读请求，其数量可随时调整。

```js
{
"settings" : {
"number_of_shards" : 3,#主分片个数
"number_of_replicas" : 1#副分片个数
}
```

#### 3.路由

***文档分片***(确定文档在哪一个分片)：

```
shard = hash(routing) % number_of_primary_shards  
routing 值是一个任意字符串， 它默认是 _id 但也可以自定义(通过routing指定)。
```

注：这也解释了为什么主分片的数量只能在创建索引时定义且不能修改，数量改变，那么路由就失效，文档也就找不到了

#### 4.bulk区别于mget中json数组的奇怪格式

?	批量中每个引用的文档属于不同的分片，每个操作需要被转发到对应的分片和节点上。  解析JSON为数组(可能非常大)，占用更多的资源，使用换行一行一行解析数据，分配到对应分片处理，无冗余复制，没有多余数据结构，使用最小的内存处理。

