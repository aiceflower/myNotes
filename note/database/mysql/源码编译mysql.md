<center><h3>源码安装MySQL</h3></center>

#### 一、下载

以Mysql5.6.44版本为例:

打开<https://www.mysql.com/downloads/>找到Community (GPL) Downloads(社区版下载)并点击，点击MySQL Community Server (GPL)(社区版服务)，Looking for previous GA versions?（查找早期版本）按如下选择：

Select Version：5.6.44

Select Operating System：Source Code (源码下载)

然后下载Generic Linux (Architecture Independent), Compressed TAR Archive（不依赖于架构的，大概30多M）

#### 二、解压

```shell
tar -zxvf mysql-5.6.44.tar.gz
```

#### 三、添加用户和用户组

```shell
groupadd mysql
useradd -d /home/mysql -g mysql -m mysql
```

#### 四、安装cmake编译工具

```shell
yum install -y cmake gcc gcc-c++ ncurses-devel bison zlib libxml openssl
```

#### 五、创建目录

```shell
mkdir -p my3306/data
mkdir -p my3306/log/iblog
mkdir -p my3306/log/binlog
mkdir -p my3306/run
mkdir -p my3306/tmp
```

#### 六、编译

进入到mysql-5.6.44目录执行如下3个命令(注意修改下对应目录)

```shell
cmake \
-DCMAKE_INSTALL_PREFIX=my3306 \
-DINSTALL_DATADIR=my3306/data  \
-DDEFAULT_CHARSET=utf8 \
-DDEFAULT_COLLATION=utf8_general_ci \
-DEXTRA_CHARSETS=all \
-DWITH_SSL=yes \
-DWITH_EMBEDDED_SERVER=1 \
-DENABLED_LOCAL_INFILE=1 \
-DWITH_MYISAM_STORAGE_ENGINE=1 \
-DWITH_INNOBASE_STORAGE_ENGINE=1 \
-DWITH_ARCHIVE_STORAGE_ENGINE=1 \
-DWITH_BLACKHOLE_STORAGE_ENGINE=1 \
-DWITH_FEDERATED_STORAGE_ENGINE=1 \
-DWITH_PARTITION_STORAGE_ENGINE=1 \
-DMYSQL_UNIX_ADDR=my3306/run/mysql.sock \
-DMYSQL_TCP_PORT=3306 \
-DENABLED_LOCAL_INFILE=1 \
-DSYSCONFDIR=/etc \
-DWITH_READLINE=on

make #编译

make install #安装 
```

#### 七、mysql配置参数

在my3306下创建my.cnf加入如下内容

```
[client]
port=3306
socket=my3306/mysql.sock

[mysql]
pid_file=my3306/run/mysqld.pid

[mysqld]
autocommit=1
general_log=off
explicit_defaults_for_timestamp=true

# system
basedir=my3306
datadir=my3306/data
max_allowed_packet=1g
max_connections=3000
max_user_connections=2800
open_files_limit=65535
pid_file=my3306/run/mysqld.pid
port=3306
server_id=101
skip_name_resolve=ON
socket=my3306/run/mysql.sock
tmpdir=my3306/tmp

#binlog
log_bin=my3306/log/binlog/binlog
binlog_cache_size=32768
binlog_format=row
expire_logs_days=7
log_slave_updates=ON
max_binlog_cache_size=2147483648
max_binlog_size=524288000
sync_binlog=100

#logging
log_error=my3306/log/error.log
slow_query_log_file=my3306/log/slow.log
log_queries_not_using_indexes=0
slow_query_log=1
log_slave_updates=ON
log_slow_admin_statements=1
long_query_time=1

#relay
relay_log=my3306/log/relaylog
relay_log_index=my3306/log/relay.index
relay_log_info_file=my3306/log/relay-log.info

#slave
slave_load_tmpdir=my3306/tmp
slave_skip_errors=OFF


#innodb
innodb_data_home_dir=my3306/log/iblog
innodb_log_group_home_dir=my3306/log/iblog
innodb_adaptive_flushing=ON
innodb_adaptive_hash_index=ON
innodb_autoinc_lock_mode=1
innodb_buffer_pool_instances=8

#default
innodb_change_buffering=inserts
innodb_checksums=ON
innodb_buffer_pool_size= 128M
innodb_data_file_path=ibdata1:32M;ibdata2:16M:autoextend
innodb_doublewrite=ON
innodb_file_format=Barracuda
innodb_file_per_table=ON
innodb_flush_log_at_trx_commit=1
innodb_flush_method=O_DIRECT
innodb_io_capacity=1000
innodb_lock_wait_timeout=10
innodb_log_buffer_size=67108864
innodb_log_file_size=1048576000
innodb_log_files_in_group=4
innodb_max_dirty_pages_pct=60
innodb_open_files=60000
innodb_purge_threads=1
innodb_read_io_threads=4
innodb_stats_on_metadata=OFF
innodb_support_xa=ON
innodb_use_native_aio=OFF
innodb_write_io_threads=10

[mysqld_safe]
datadir=my3306/data
```

#### 八、授权

```shell
chown -R mysql:mysql my3306
chmod 755 -R my3306
```

#### 九、初始化MySQL脚本

```shell
su - mysql
./scripts/mysql_install_db --defaults-file=my3306/my.cnf --datadir=my3306/data/ --user=pillow
```

#### 十、启动

```shell
./bin/mysqld_safe --defaults-file=my3306/my.cnf --user=mysql &
```

使用mysqld_safe如果mysqld服务挂掉会再启

#### 十一、登陆

```mysql
mysql
#或
mysql -uroot -h127.0.0.1
mysql --socket=my3306/run/mysql.sock
```

#### 十二、启动多服务

拷贝my3306,修改配置文件my.cnf，然后再启动

```mysql
show variables like '%server_id%' #修改下server_id，每台服务器的server_id不能一样
```

