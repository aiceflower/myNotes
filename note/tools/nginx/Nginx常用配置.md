<h2>Nginx常用配置</h2>

# 前端必备 Nginx 配置

> Nginx (engine x) 是一个轻量级高性能的HTTP和反向代理服务器，同时也是一个通用 代理服务器 （TCP/UDP/IMAP/POP3/SMTP），最初由俄罗斯人Igor Sysoev编写。

## 基本命令

```
nginx -t             检查配置文件是否有语法错误
nginx -s reload       热加载，重新加载配置文件
nginx -s stop         快速关闭
nginx -s quit         等待工作进程处理完成后关闭
复制代码
```

搭建好nginx服务器并启动过后，我们先看nginx默认配置，再逐个介绍不同使用场景。

## 默认配置

> Nginx 安装目录下, 我们复制一份``nginx.conf`成```nginx.conf.default``作为配置文件备份，然后修改``nginx.conf``

```
# 工作进程的数量
worker_processes  1;
events {
    worker_connections  1024; # 每个工作进程连接数
}

http {
    include       mime.types;
    default_type  application/octet-stream;

    # 日志格式
    log_format  access  '$remote_addr - $remote_user [$time_local] $host "$request" '
                  '$status $body_bytes_sent "$http_referer" '
                  '"$http_user_agent" "$http_x_forwarded_for" "$clientip"';
    access_log  /srv/log/nginx/access.log  access; # 日志输出目录
    gzip  on;
    sendfile  on;

    # 链接超时时间，自动断开
    keepalive_timeout  60;

    # 虚拟主机
    server {
        listen       8080;
        server_name  localhost; # 浏览器访问域名

        charset utf-8;
        access_log  logs/localhost.access.log  access;

        # 路由
        location / {
            root   www; # 访问根目录
            index  index.html index.htm; # 入口文件
        }
    }

    # 引入其他的配置文件
    include servers/*;
}
```

## 搭建站点

> 在其他配置文件`servers`目录下，添加新建站点配置文件 xx.conf。

> 电脑 hosts 文件添加 127.0.0.1  xx_domian

```
# 虚拟主机
server {
    listen       8080;
    server_name  xx_domian; # 浏览器访问域名

    charset utf-8;
    access_log  logs/xx_domian.access.log  access;

    # 路由
    location / {
        root   www; # 访问根目录
        index  index.html index.htm; # 入口文件
    }
}
```

执行命令 nginx -s reload，成功后浏览器访问 xx_domian 就能看到你的页面

## 根据文件类型设置过期时间

```
location ~.*\.css$ {
    expires 1d;
    break;
}
location ~.*\.js$ {
    expires 1d;
    break;
}

location ~ .*\.(gif|jpg|jpeg|png|bmp|swf)$ {
    access_log off;
    expires 15d;    #保存15天
    break;
}
```



## 禁止文件缓存

开发环境经常改动代码，由于浏览器缓存需要强制刷新才能看到效果。这是我们可以禁止浏览器缓存提高效率

```
location ~* \.(js|css|png|jpg|gif)$ {
    add_header Cache-Control no-store;
}
```

## 防盗链

可以防止文件被其他网站调用

```
location ~* \.(gif|jpg|png)$ {
    # 只允许 192.168.0.1 请求资源
    valid_referers none blocked 192.168.0.1;
    if ($invalid_referer) {
       rewrite ^/ http://$host/logo.png;
    }
}
```



## 静态文件压缩

```
server {
    # 开启gzip 压缩
    gzip on;
    # 设置gzip所需的http协议最低版本 （HTTP/1.1, HTTP/1.0）
    gzip_http_version 1.1;
    # 设置压缩级别，压缩级别越高压缩时间越长  （1-9）
    gzip_comp_level 4;
    # 设置压缩的最小字节数， 页面Content-Length获取
    gzip_min_length 1000;
    # 设置压缩文件的类型  （text/html)
    gzip_types text/plain application/javascript text/css;
}
```



执行命令 nginx -s reload，成功后浏览器访问

## 指定定错误页面

```
# 根据状态码，返回对于的错误页面
error_page 500 502 503 504 /50x.html;
location = /50x.html {
    root /source/error_page;
}
```

执行命令 nginx -s reload，成功后浏览器访问



## 跨域问题

> #### 跨域的定义
>
> 同源策略限制了从同一个源加载的文档或脚本如何与来自另一个源的资源进行交互。这是一个用于隔离潜在恶意文件的重要安全机制。通常不允许不同源间的读操作。
>
> #### 同源的定义
>
> 如果两个页面的协议，端口（如果有指定）和域名都相同，则两个页面具有相同的源。



#### nginx解决跨域的原理

例如：

- 前端server域名为：`http://xx_domain`
- 后端server域名为：`https://github.com`

现在`http://xx_domain`对`https://github.com`发起请求一定会出现跨域。

不过只需要启动一个nginx服务器，将`server_name`设置为`xx_domain`,然后设置相应的location以拦截前端需要跨域的请求，最后将请求代理回`github.com`。如下面的配置：

```
## 配置反向代理的参数
server {
    listen    8080;
    server_name xx_domain

    ## 1. 用户访问 http://xx_domain，则反向代理到 https://github.com
    location / {
        proxy_pass  https://github.com;
        proxy_redirect     off;
        proxy_set_header   Host             $host;        # 传递域名
        proxy_set_header   X-Real-IP        $remote_addr; # 传递ip
        proxy_set_header   X-Scheme         $scheme;      # 传递协议
        proxy_set_header   X-Forwarded-For  $proxy_add_x_forwarded_for;
    }
}
```



### 后端

## 防盗链

```
location ~* \.(gif|jpg|png)$ {
    # 只允许 192.168.0.1 请求资源
    valid_referers none blocked 192.168.0.1;
    if ($invalid_referer) {
       rewrite ^/ http://$host/logo.png;
    }
}复制代码
```



## 根据文件类型设置过期时间

```
location ~.*\.css$ {
    expires 1d;
    break;
}
location ~.*\.js$ {
    expires 1d;
    break;
}

location ~ .*\.(gif|jpg|jpeg|png|bmp|swf)$ {
    access_log off;
    expires 15d;    #保存15天
    break;
}

```

## 静态资源访问

```
http {
    # 这个将为打开文件指定缓存，默认是没有启用的，max 指定缓存数量，
    # 建议和打开文件数一致，inactive 是指经过多长时间文件没被请求后删除缓存。
    open_file_cache max=204800 inactive=20s;

    # open_file_cache 指令中的inactive 参数时间内文件的最少使用次数，
    # 如果超过这个数字，文件描述符一直是在缓存中打开的，如上例，如果有一个
    # 文件在inactive 时间内一次没被使用，它将被移除。
    open_file_cache_min_uses 1;

    # 这个是指多长时间检查一次缓存的有效信息
    open_file_cache_valid 30s;

    # 默认情况下，Nginx的gzip压缩是关闭的， gzip压缩功能就是可以让你节省不
    # 少带宽，但是会增加服务器CPU的开销哦，Nginx默认只对text/html进行压缩 ，
    # 如果要对html之外的内容进行压缩传输，我们需要手动来设置。
    gzip on;
    gzip_min_length 1k;
    gzip_buffers 4 16k;
    gzip_http_version 1.0;
    gzip_comp_level 2;
    gzip_types text/plain application/x-javascript text/css application/xml;


    server {
        listen       80;
        server_name www.test.com;
        charset utf-8;
        root   /data/www.test.com;
        index  index.html index.htm;
    }
}
```



## 日志配置

### 日志字段说明

| 字段                                | 说明                                                         |      |
| ----------------------------------- | ------------------------------------------------------------ | ---- |
| remote_addr 和 http_x_forwarded_for | 客户端 IP 地址                                               |      |
| remote_user                         | 客户端用户名称                                               |      |
| request                             | 请求的 URI 和 HTTP 协议                                      |      |
| status                              | 请求状态                                                     |      |
| body_bytes_sent                     | 返回给客户端的字节数，不包括响应头的大小                     |      |
| bytes_sent                          | 返回给客户端总字节数                                         |      |
| connection                          | 连接的序列号                                                 |      |
| connection_requests                 | 当前同一个 TCP 连接的的请求数量                              |      |
| msec                                | 日志写入时间。单位为秒，精度是毫秒                           |      |
| pipe                                | 如果请求是通过HTTP流水线(pipelined)发送，pipe值为“p”，否则为“.” |      |
| http_referer                        | 记录从哪个页面链接访问过来的                                 |      |
| http_user_agent                     | 记录客户端浏览器相关信息                                     |      |
| request_length                      | 请求的长度（包括请求行，请求头和请求正文）                   |      |
| time_iso8601                        | ISO8601标准格式下的本地时间                                  |      |
| time_local                          | 记录访问时间与时区                                           |      |



### access_log 访问日志

```
http {
    log_format  access  '$remote_addr - $remote_user [$time_local] $host "$request" '
                  '$status $body_bytes_sent "$http_referer" '
                  '"$http_user_agent" "$http_x_forwarded_for" "$clientip"';
    access_log  /srv/log/nginx/talk-fun.access.log  access;
}
```



### error_log 日志

```
error_log  /srv/log/nginx/nginx_error.log  error;
# error_log /dev/null; # 真正的关闭错误日志
http {
    # ...
}
```



### 日志切割

```
# 和apache不同的是，nginx没有apache一样的工具做切割，需要编写脚本实现。# 在/usr/local/sbin下写脚本


#!/bin/bash
dd=$(date -d '-1 day' +%F)[ -d /tmp/nginx_log ] || mkdir /tmp/nginx_log
mv /tmp/nginx_access.log /tmp/nginx_log/$dd.log
/etc/init.d/nginx reload > /dev/null
```



## 反向代理

```
http {
    include mime.types;
    server_tokens off;

    ## 配置反向代理的参数
    server {
        listen    8080;

        ## 1. 用户访问 http://ip:port，则反向代理到 https://github.com
        location / {
            proxy_pass  https://github.com;
            proxy_redirect     off;
            proxy_set_header   Host             $host;
            proxy_set_header   X-Real-IP        $remote_addr;
            proxy_set_header   X-Forwarded-For  $proxy_add_x_forwarded_for;
        }

        ## 2.用户访问 http://ip:port/README.md，则反向代理到
        ##   https://github.com/zibinli/blog/blob/master/README.md
        location /README.md {
            proxy_set_header  X-Real-IP  $remote_addr;
            proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
            proxy_pass https://github.com/zibinli/blog/blob/master/README.md;
        }
    }
}
```



## 禁止指定user_agent

```
#虚拟主机的配置文件里加入：

if ($http_user_agent ~* 'baidu|360|sohu') #禁止useragent为baidu、360和sohu，~*表示不区分大小写匹配
{
   return 403;
}

location /  和  location  ~ /  优先级是不一样的。 
结合这个文章研究一下吧 http://blog.itpub.net/27181165/viewspace-777202/
curl -A "baidu" -x127.0.0.1:80 www.test.com/forum.php -I    该命令指定百度为user_agent,返回403
```

## nginx访问控制

```
# 可以设置一些配置禁止一些ip的访问

deny 127.0.0.1;     #全局定义限制，location里的是局部定义的。如果两者冲突，以location这种精确地优先，

location ~ .*admin\.php$ {
    #auth_basic "cct auth";
    #auth_basic_user_file /usr/local/nginx/conf/.htpasswd;

    allow 127.0.0.1;  只允许127.0.0.1的访问，其他均拒绝
    deny all;

    include fastcgi_params;
    fastcgi_pass unix:/tmp/www.sock;
    fastcgi_index index.php;
    fastcgi_param SCRIPT_FILENAME /data/www$fastcgi_script_name;
}
```



## 负载均衡

```
http {
    upstream test.net {
        ip_hash;
        server 192.168.10.13:80;
        server 192.168.10.14:80  down;
        server 192.168.10.15:8009  max_fails=3  fail_timeout=20s;
        server 192.168.10.16:8080;
    }
    server {
        location / {
            proxy_pass  http://test.net;
        }
    }
}
```