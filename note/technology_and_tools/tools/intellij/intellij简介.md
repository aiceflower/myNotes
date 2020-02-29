### IntelliJ IDEA介绍
#### 一、简单说明

IntelliJ IDEA 是 JetBrains 公司下的产品，主要用于支持 Java、Scala、Groovy 等语言的开发工具，同时具备支持目前主流的技术和框架，擅长于企业应用、移动应用和 Web 应用的开发。

#### 二、使用教程地址

https://www.javazhiyin.com/idea
maven配置：
https://www.javazhiyin.com/23661.html
推荐插件：
https://www.javazhiyin.com/23864.html

#### 三、核心文件和目录
安装目录: ../IntelliJ IDEA xxx
	bin/ 启动和配置
		idea.exe			//32位启动
		idea.exe.vmoptions	//32位虚拟机配置
			idea.config.path=${user.home}/.IntelliJIdea/config  //指定idea配置文件路径
			idea.system.path=${user.home}/.IntelliJIdea/system  //指定idea系统文件路径
			idea.cycle.buffer.size=1024		//控制控制台输出缓存, 全部显示，禁用idea.cycle.buffer.size=disabled
			注：建议自定义配置目录，保证个人开发习惯，和在服务器上备份
		idea.properties		//idea配置，不分32还是64
		idea64.exe			//64位启动
		idea64.exe.vmoptions//64位虚拟机配置
	plugins/ 插件
	注：
		推荐使用 IDEA 自带菜单中的 Help -> Edit Custom VM Options 和 Help -> Edit Custom Properties 来进行参数个性化配置！
		这种方式产生的文件在 ~/.IntelliJIdea2018.2\config下
项目文件的历史更改记录:
	~/.IntelliJIdea2018.2\system\LocalHistory
	
#### 四、建议
	1.自定义配置目录
		idea.exe.vmoptions中idea.config.path
	2.推荐使用 IDEA 自带菜单中的 Help -> Edit Custom VM Options 和 Help -> Edit Custom Properties 来进行参数个性化配置！
	
#### 五、相关概念
	1.idea中没有像eclipse一样有工作空间的概念，idea中一个项目打开一个窗口
	2.idea中Project是最顶级别，一个Project由多个Model组成
	3.idea中可以对Project和Model单独设置JDK Project Structure -> Project/MOdel -> Language Level
	
	