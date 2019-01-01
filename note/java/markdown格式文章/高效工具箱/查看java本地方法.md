我们知道在java中查看java源码时看到native方法在java层面上就到头的，如果还想继续往下看就需要看jdk是如何实现的，今天就分享一下如何查看java中native方法。
#### 1.先去http://hg.openjdk.java.net/下载openJDK源码
	如果不知道如何下载看[openJDK源码下载](https://www.jianshu.com/p/6fe47f6a1b2a)
#### 2.解压下载的openJDK源码
	解压后我们可以看到目录结构如下（这里的代码就是OpenJDK里的大部分类库的实现）。其中 
	src/
		share/       平台无关的实现
		classes/     平台无关的Java代码实现
		native/      平台无关的native代码实现（主要是C）
进入到相应的目录我们可以看到classes与native目录的结构就跟Java的包目录结构一样，两边是对应的。知道这个关系就能很快找到JDK类库中Java声明为native的方法在C里的实现函数。
如：
	java.lang.System类中的registerNatives方法，对应的就是src\share\native\java\lang目录下System.c文件中Java_java_lang_System_registerNatives方法了。我们很容易可以看出它的对应规则是Java_包名(其中.用_代替)_类名_方法名。
然后就可以尽情的在C的世界查看它是如何实现的了，朋友们赶快开启你的本地方法查看之旅吧。
