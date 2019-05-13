### һ������

#### 1.��������

***���***��

Elasticsearch��һ������Lucene ��ʵʱ�ֲ�ʽ�����ͷ������� ������ȫ�������� �ṹ�������� ���� �����������԰ټƵķ������ϴ���PB��������� ��

*ES�봫ͳ��ϵ�����ݿ�Ա�*��

?	Relational DB -> Databases -> Tables -> Rows -> Columns
?	Elasticsearch -> Indices -> Types -> Documents -> Fields 

***��ͬ����***��

- ����(����)��һ�������൱�ڴ�ͳ���ݿ��е����ݿ⣬�����ĵ��洢�ĵط�
- ����(����)������һ���ĵ�����ʾ���ĵ��洢��index�У�����sql�е�insert��������������ĵ��Ѿ����ڻḲ�Ǿ��ĵ�
- �������������ƴ�ͳ���ݿ���Ϊĳһ�м�һ�����������ټ�����ES��ʹ�õ��������ﵽ��ͬĿ��

#### 2.����

***��java����***��

?	ͨ���ڵ�ͻ��˻���ͻ�����ES���н���

?	**�ڵ�ͻ���**��(9300�˿�)

?	�ڵ�ͻ�����Ϊһ�������ݽڵ���뵽���ؼ�Ⱥ�С����仰˵���������������κ����ݣ�������֪�������ڼ�Ⱥ�е��ĸ��ڵ��У����ҿ��԰�����ת������ȷ�Ľڵ㡣

?	**����ͻ���**��(9300�˿�)

?	�������Ĵ���ͻ��˿��Խ������͵�Զ�̼�Ⱥ�������������뼯Ⱥ�����������Խ�����ת������Ⱥ�е�һ���ڵ��ϡ�

*���������Խ���*��

?	ʹ�� RESTful API ͨ���˿� *9200* �� Elasticsearch ����ͨ��

### ������������

#### 1. ��

***��������***��put /������

***��������***��PUT /������/������/id data(Json ����)



#### 2.ɾ

***ɾ������***��DELETE /������

***ɾ������***��DELETE /������/������/id

#### 3.��

***��ѯ��������***��GET /_cat/indices?v

***��ѯĳ��������������***��GET /������/������/_search

***��������ѯ***��GET /������/������/_search?q=field1 : value1&q=ield2 : value2

***ȫ������***(����������������)��

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



#### 4.��

***�޸Ĳ����ֶ�***���������������ָ��·�ʽ��δ�г���

��ʽһ��

```javascript
PUT /������/������/id/_update?pretty #����ֻ����POST������PUT
{
  "doc": {
    "�ֶ���" : "��ֵ"  
  },

"detect_noop": false #true:ֻ��ԭ����source���µ�source���ڲ�ͬ���ֶ�����²Ż��ؽ�����, false:����������û�б仯�����ؽ���������һ�����ͨ��version��ֵ�ı仯������
}

```

��ʽ����

```javascript
POST /������/������/id/_update?pretty
{
  "script": {
     "inline": "ctx._source.last_name = 'Smith';ctx._source.age  -= params.count", #����ֶ��÷ֺ�
     "params": {
      "count": 3
    }
  }
}
```

***��������***��

```javascript
POST /������/������/_bulk #���������쳣����ִ��������������󷵻ظ���ִ�н��
{"update":{"_id":"1"}} #����
{"doc": { "�ֶ�": "ֵ" } }
{"delete":{"_id":"2"}}#ɾ��
```





### �����߼�����

#### 1.��ѯ

***���������ǲ�ѯ***��

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

***���������ǲ�ѯ***��

```json
{
  "query": {
    "bool": {
      "must": {
        "multi_match" : {
          "query":      "value1 value2 ...",#��������
          "type":       "best_fields",
          "fields":     [ "field" ],#�������ֶ�
          "operator":   "or" 
        }
      },
      "filter": {
        "bool": {
          "should": [#��ѯ����
            ...
          ]
        }
      }

    }

  }
}

```



***��ʱ��ۺϲ�ѯ***��

```json
{
  "size": 0,
  "aggs": {
    "�ۺ�����": {
      "date_histogram": {          //es�ṩ��ʱ�䴦������
        "field": "createDate",     //��Ҫ�ۺϵ��ֶ�����
        "interval": "hour",        //��Сʱgroup by
        "format": "yyyy-MM-dd HH", //����ֵ��ʽ����HH��д����Ȼ�����������硢����
        "min_doc_count": 0,        //��С�ĵ���
        "extended_bounds": {       //��ѯ�����Χ
          "min": "2016-12-19 01", //��ʽ��formatһ��
          "max": "2016-12-19 23"
        }
      }
    }
  },
  "query": { //��ѯ����
    ...
  	}
  }
}
```

***���ؾۺ�***��

```json
{  
  "size": 0,
  "aggs": {  
    "�ۺ�����": {#��һ�ξۺϣ���Сʱ��ͳ��
      ...
      },
      "aggs": { #�Ӿۺ�
        "distinct_colors" : {#ȥ�ؾۺ�
        "cardinality" : {
          "field" : "client.keyword"
        }
      }
      }
    }
    
  },  
  "query": { #��ѯ����
   	...
  }  
}
```

#### 2.���򣬷�ҳ������ֵ

```js
{
  "query": { "match_all": {} },
  "from": 0, #��ҳ
  "size": 20,
  "_source": ["account_number","age","address"], #���Ʒ����ֶ�
  "sort": [ #����
    {
      "account_number": {
        "order": "asc"
      }
    }
  ]
}
```

### �ġ��������

#### 1.ע������

- match��֧�ֶ�������ѯ

### �塢����

#### 1.�����ֹ���

�޸�ʱָ���汾��ʵ���ֹ���

PUT /����/����/id?version=�汾��&version_type=external

**ע**����ָ��version_type=externalʱ�汾����ES�еİ汾���޸ĳɹ�����ָ��ʱ�汾��ES�а汾һ�²����޸ĳɹ�

### �������ĸ���

#### 1.����

***��������***(forward index)��

?	ͨ��idȥ��ѯ�ؼ��ʣ����ֵĴ����Լ����ֵ�λ���б���Ҳ��ͨ��keyȥ��value��

?	����������Ҫɨ���������е��������ݣ��ҳ����а�������ؼ��ʵ��ĵ����ٸ��ݴ��ģ�ͽ��д�֣��ų����κ���ָ��û������������޴��޷�����ʵʱ���ش�������

***��������***(inverted index)��	

?	ͨ���ؼ���ȥ��ѯid��ÿ���ؼ��ʶ�Ӧһϵ���ļ�����Щ�ļ��ж���������ؼ��ʡ�Ҳ��ͨ��valueȥ��ѯkey��

#### 2.��Ⱥ����ԭ��

***ȥ���Ļ�***��

?	û�����Ľڵ㣬���Ƕ��ڼ�Ⱥ�ⲿ��˵�ģ����ⲿ������������κ�һ���ڵ�ͨ�š�

***�ڵ�***��

?	һ���ڵ�**(node)**����һ��Elasticsearchʵ����

***�ֲ�ʽ***��

?	����ܶ������еļ��������Ϊ��㣩��ɡ�����������ü�������������Ϣ���ݣ��Ӷ�ʵ�ֹ�ͬ�ġ�Ŀ��������񡱡�

***��Ⱥ*** ��

?	һ����Ⱥ**(cluster)**��һ�������ڵ���ɣ����Ǿ�����ͬ�� cluster.name �� ����Эͬ������ �������ݺ͸��ء��������µĽڵ����ɾ��һ���ڵ�ʱ�� ��Ⱥ�ͻ��֪����ƽ�����ݡ�	es���Զ�����ͬһ��������ͬcluster.name�Ľڵ� �Զ���ɼ�Ⱥ��

?	��Ⱥ��һ���ڵ�ᱻѡ��Ϊ���ڵ�**(master)** �������������Ⱥ��״̬���ڵ�ķ��ֺ�ɾ����������������Ƭ��״̬��������״̬������Ⱥ�����ڵ�ҵ��󣬻�������ڵ���ѡ��һ���ڵ���Ϊ���ڵ㡣

***��Ⱥ����*** ��

��Ⱥ����������״̬(GET /_cluster/health )�� 

- green��������Ҫ��Ƭ�͸��Ʒ�Ƭ������ 
- yellow��������Ҫ��Ƭ���ã� ���������и��Ʒ�Ƭ������ 
- red ���������е���Ҫ��Ƭ������ 



***��Ƭ***��

?	��һ��Luceneʵ���� ��������������һ���������������� ��һ����С����**��**������Ԫ ������ֻ�Ǳ������������������ݵ�һ���֡�  ����ֻ��һ������ָ��һ��������Ƭ**(shards)**���߼������ռ� ��