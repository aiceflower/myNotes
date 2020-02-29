1.下载源码

[点我下载Tomcat源码](http://mirror.navercorp.com/apache/tomcat/tomcat-8/v8.5.41/src/apache-tomcat-8.5.41-src.zip)

2.将源码导入idea并创建pom.xml加入如下内容

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">

    <modelVersion>4.0.0</modelVersion>
    <groupId>org.apache.tomcat</groupId>
    <artifactId>Tomcat8.0</artifactId>
    <name>Tomcat8.0</name>
    <version>8.0</version>

    <build>
        <finalName>Tomcat8.0</finalName>
        <sourceDirectory>java</sourceDirectory>
        <!--<testSourceDirectory>test</testSourceDirectory>-->
        <resources>
            <resource>
                <directory>java</directory>
            </resource>
        </resources>
        <!--<testResources>-->
            <!--<testResource>-->
                <!--<directory>test</directory>-->
            <!--</testResource>-->
        <!--</testResources>-->
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.1</version>
                <configuration>
                    <encoding>UTF-8</encoding>
                    <source>1.8</source>
                    <target>1.8</target>
                </configuration>
            </plugin>
        </plugins>
    </build>

    <dependencies>
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.12</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.easymock</groupId>
            <artifactId>easymock</artifactId>
            <version>3.4</version>
        </dependency>
        <dependency>
            <groupId>ant</groupId>
            <artifactId>ant</artifactId>
            <version>1.7.0</version>
        </dependency>
        <dependency>
            <groupId>wsdl4j</groupId>
            <artifactId>wsdl4j</artifactId>
            <version>1.6.2</version>
        </dependency>
        <dependency>
            <groupId>javax.xml</groupId>
            <artifactId>jaxrpc</artifactId>
            <version>1.1</version>
        </dependency>
        <dependency>
            <groupId>org.eclipse.jdt.core.compiler</groupId>
            <artifactId>ecj</artifactId>
            <version>4.5.1</version>
        </dependency>
    </dependencies>
</project>
```

3.安装ant

[点我下载Ant](http://mirror.navercorp.com/apache//ant/binaries/apache-ant-1.9.14-bin.zip) 并配置环境变量

4.进入tomcat目录执行ant命令，输出文件到output目录下

5.复制output/build目录下的文件到catalina-home(新键，同tomcat一个目录下)

6.BootStrap启动main函数，指定如下参数

```shell
-Dcatalina.home="~/catalina-home"
```

