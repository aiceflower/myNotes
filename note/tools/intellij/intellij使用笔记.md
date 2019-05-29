### 一、intellij使用技巧
#### 0.工具激活
http://idea.lanyus.com/
####1.代码自动补全tab
sout System.out.println();
psvm main方法
inn直接就是if（xxx ！= null）
iter就可以调出遍历(相当于foreach)

#### 2.设置快捷键风格
	settings-->keymap
#### 3.设置主题风格	
	settings-->editor-->color Scheme
#### 4.显示行号
	settings-->editor-->general-->appearance
#### 5.设置默认编码
	settings-->editor-->fileEncodings
#### 6.管理插件
	settings-->plugings
#### 7.设置代码提示不区分大小写
	settings-->输入sensitive   case sensitive completion 修改为NONE
#### 8.书写json或正则
	alt+enter(写一个变量，在双引号里面),选择第一个(inject language..)然后enter，输入json/reg，然后再重复一遍
#### 9.查看文件修改历史
	右键->local 	History-->show History(可查看选中内容历史)
#### 10.idea使用debug
	f8 下一步
	f7 进入方法
	alt+shift+f7 强制进入方法，可以是内置方法
	shift+f8 退出方法
	f9/shift+f8 跳转到下一个断点
	alt+f9 跳转到光标处
	alt+f8 弹出表达式计算窗口
#### 11.IDEA内存优化
	因机器本身的配置而配置：
	\IntelliJ IDEA 8\bin\idea.exe.vmoptions  
	-----------------------------------------  
	-Xms64m  
	-Xmx256m  
	-XX:MaxPermSize=92m  
	-ea  
	-server  
	-Dsun.awt.keepWorkingSetOnMinimize=true

#### 12.idea 添加本地jar包依赖
	File –> Project Structure ->Moudules –> Dependencies -> + jars or...

#### 13.创建资源目录
	1.先创建一个普通目录
	2.打开project strcture (ctrl+alt+shift+s)
	3.选择modules 对应的项目 sources 刚创建的目录，右键 Resources		

#### 14.Toolbar 和 Tool Buttons
	view -> Toolbar / Tool Buttons
#### 15.清除缓存和索引
	File -> Invalidate Caches  //会清除掉缓存，如果需要文件历史修改记录请备份LocalHistory
	也可直接删除 ~/.IntelliJIdea2018.2\system\ 目录
#### 16.编译
	Build -> Build Project //对选定的目标（Project 或 Module）进行编译，但只编译有修改过的文件
	Build -> Recompile //强制编译某文件
#### 17.编译
	settings -> Build, Executation -> Compiler ->　Build process heap size  //设置编译内存大小
	settings -> Build, Executation -> Compiler ->　Excludes 排除一些文件不进行编译
#### 18.修改快捷键
	settings -> key map -> Main Menu -> Code -> Completion -> Basic 修改为ctrl+, (默认为ctrl+space)

### 二、快捷键
#### 其它：
ctrl+shift+enter 快速补全末尾分号
<font color=#e00>shift+enter</font>(ctrl+alt+enter) 重起下一行(上一行)
ctrl+alt+o 快速优化引用包
ctrl+alt+t 快速生成if,try等
ctrl+shift+c复制类的全路径	
alt+shift+上下键: 当前行与上下行交换位置
ctrl+shift+v 选择粘贴历史粘贴
Shift + 左键单击 在打开的文件名上按此快捷键，可以关闭当前打开文件 
Shift + 滚轮前后滚动	当前文件的横向滚动轴滚动
Ctrl + Shift + Space	智能代码提示
Ctrl + Shift + F12	编辑器最大化
<font color=#e00>ctrl + alt + F12</font> 打开资源管理器

#### 查找：
Ctrl + F 在当前文件进行文本查找 配置F3/Shift F3 下一个
ctrl+shift+f 查找当前项目包含指定内容
Ctrl + E 打开最近访问文件列表
Ctrl + Shift + E	显示最近修改的文件列表的弹出层
ctrl+n 查找类
ctrl+shift+n 查找文件
Alt + F7	查找光标所在的方法 / 变量 / 类被调用的地方(同Ctrl + Alt + H类似)
Ctrl＋F7可以查询当前元素在当前文件中的引用，然后按F3可以选择
CTRL+ALT+B  找所有的子类
CTRL+SHIFT+B  找变量的类
Shift + Shift	搜索所有文件
ctrl+shift+alt+n 查找所有类中的方法及字段
Ctrl + Shift + A 搜索一切



#### 选择：
Ctrl + W 递进式选择代码块。可选中光标所在的单词或段落，连续按会在原有选中的基础上再扩展选中范围
Ctrl + O	选择可重写的方法
Ctrl + I	选择可继承的方法
<font color=#e00>Alt + Shift + 左键双击</font> 多地方选择


#### 删除:
ctrl+y 删除光标所在行或选中行

#### 复制:
ctrl+d 复制光标所在行 或 复制选择内容，并把复制内容插入光标位置下面

#### 跳转:
ctrl+g 跳转到指定行:列 10:10
ctrl+u 跳转到父类方法
ctrl+b 查看或跳转使用当前变量或方法的去处
Ctrl + F3	调转到所选中的词的下一个引用位置
Ctrl + 1,2,3...9 跳转到书签 (可通过ctrl+f11设置书签,shift+f11显示书签，Ctrl + Shift + 1,2,3...9	快速设置书签 )
ctrl+alt+左右 回到上一次停留的位置方	
ctrl+shift+backspace 跳转到上次编辑的地	
Ctrl + Alt + F7 跳转到方法变量调用地方

#### 插入：
Ctrl + J 插入自定义动态代码模板 
Alt + Insert(fn+enter相当于) 代码自动生成，如生成对象的 set / get 方法，构造函数，toString() 等
Ctrl + Shift + T	对当前类生成单元测试类

#### 显示：
<font color=#e00>Ctrl + H</font> 显示当前类的层次结构(ctrl+alt+u查看图解)
ctrl+p 显示方法参数
Ctrl +/- 展开代码/折叠代码
Ctrl + F1 在光标所在的错误代码处显示错误信息 
<font color=#e00>Ctrl + F12</font>	显示当前文件方法
Ctrl + , 基础代码补全 (默认为ctrl+space)
Ctrl + 光标定位	按 Ctrl 不要松开，会显示光标所在的类信息摘要
Alt + F1	显示当前文件选择目标弹出层，弹出层中有很多目标可以进行选择
Alt + Enter	IntelliJ IDEA 根据光标所在问题，提供快速修复选择
ctrl+f12 显示类结构 （alt+7左边列出类结构）
Alt + 1,2,3...9	显示对应数值的选项卡，其中 1 是 Project 用得最多
alt+f12 打开终端
<font color=#e00>Ctrl + Shift + I</font>	显示代码定义
Ctrl + Shift + F7	高亮显示所有该选中文本，按Esc高亮消失
Ctrl + Shift + Alt + S	打开当前项目设置 

#### 注释：
Ctrl + /	注释光标所在行代码
Ctrl + Shift + /	代码块注释 /**/

#### 重构：
<font color=#e00>shift+f6</font> 重构方法或变量名
<font color=#e00>ctrl+alt+v</font>：提取为局部变量
<font color=#e00>ctrl+alt+f</font>：提取为成员变量
ctrl+alt+c	提取常量
ctrl+r 当前文件替换内容
<font color=#e00>ctrl+shift+r</font> 当前项目替换内容
<font color=#e00>Ctrl + Shift + U</font>	大小写切换
ctrl+alt+I 代码缩进
<font color=#e00>ctrl+alt+L</font> 格式化代码
Ctrl + Shift + 上/下 交换上下行
F4	编辑源  可用于跳转


### 三、代码注释
#### 1.类注释
	preference-->Editor-->File and Code Template-->files-->Class
	/**
	 * 
	 * 开发公司：grgits.com
	 * 版权：grgits.com
	 *
	 * @description ${DESCRIPTION}  
	 *
	 * @author hjfu5 
	 *
	 * @email  hjfu5@grgbanking.com
	 *
	 * @time ${DATE} ${TIME} 
	 *
	 * @version 1.0 
	 * 
	 */
#### 2.方法注释

```
preference-->Live Template-->+-->Edit variables
	*
 	* @describe: $DESC$
 	* @author: hjfu5
 	* @time: $CREAT_DATE​$ $CREAT_TIME​$
    $PARAM​$
    $RETURN​$
    */

   PARAM:
   groovyScript("def result=''; def params=\"${_1}\".replaceAll('[\\\\[|\\\\]|\\\\s]', '').split(',').toList(); for(i = 0; i < params.size(); i++) {result+= ((i == 0)?'':'\\t ') + '* @param ' + params[i] + ((i < params.size() - 1) ? '\\n\\b' : '')}; return result.trim()", methodParameters())
   RETURN:
   groovyScript("def returnType = \"${_1}\"; def result = '* @return:' + returnType; return result;", methodReturnType())
```




### 四、常用 设置 
#### 1.提示区分大小写
	settings->Editor->General->Code Completion->Match Case(取消勾选)
#### 2.包自动导入优化
	settings->Editor->General->Auto Import->Add unambiguous.../Optimize imports...(勾选这两个)
#### 3.代码分屏
	打开文件名上右键->Split...
#### 4.ctrl+d修改
	在key map中删除ctrl+d 然后搜索duplicate Entire Lines 设置为ctrl+d //默认是复制选中到结尾，设置后复制选中行
#### 5.显示内存使用
	settings->Appearance&Behavior->Appearance(勾选Show memory indicator)
#### 6.鼠标滚动调整字体大小
	settings->Editor->General(勾选Change font size ...)
#### 7.显示行号和方法线
	settings->Editor->General->Appearance(Show line numbers/Show method separators)
#### 8.修改新键项目时默认配置
	file->other settings ->settings for new projects中修改相应配置
#### 9.设置打开时选择项目
	settings->Appearance&Behavior->System Settings->Reopen last project on startup(取勾)
#### 10.设置打开的文件 Tab 个数/显示在多行
	settings->Editor->General->Editor Tabs->Tab limit/Show tab in one row(取勾)
#### 11.配置可生成序列id
	1.ctrl+shift+a搜索seriaVersionUID
	2.实现Serializable后光标停在类名上，按alt+enter
#### 12.设置默认浏览器
	settings->tools->web browsers
#### 13.点到其它位置时指定窗口自动隐藏
	窗口右键->pinned mode
#### 14.添加文件到收藏夹
	文件tab右键->add to favorites
#### 15.设置自定义折叠区域
	选中->ctrl+alt+t->region..endregion comments
#### 16.restful http 客户端
	Tools ->　http client -> test restful web service
#### 17.设置自定义todo
	alt+6打开todo窗口->漏斗状->Edit Filters
#### 18.导入其它主题(如sublime)
	去http://www.riaway.com下载自己喜欢的主题
	file->import settings 重启
#### 19.idea无法输入中文输入法
	1、关掉idea后在idea的安装路径下把jre64文件夹删掉，或者重命名也行
	2、升级jdk版本至jdk 8u45以上
	3、把Java安装路径下的jre文件拷贝到IDEA的安装目录下并重命名为jre64；
	4、把对应版本的jdk/lib的tools.jar拷贝到jre64/lib下； 
	5、重启idea
#### 20.idea开启热加载
	1.、CTRL + SHIFT + A --> 查找make project automatically --> 选中
	2.CTRL + SHIFT + A --> 查找Registry --> 找到并勾选compiler.automake.allow.when.app.running
	3.添加maven依赖
	<dependency>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-devtools</artifactId>
		<optional>true</optional>
	</dependency>
	4.以上三步可使用热加载，如未成功可使用以下解决
		<build>
			<plugins>
				<plugin>
					<groupId>org.springframework.boot</groupId>
					<artifactId>spring-boot-maven-plugin</artifactId>
					<configuration>
						<fork>true</fork>//该配置必须
					</configuration>
				</plugin>
			</plugins>
		</build>
	或禁用缓存Chrome，开发者模式-->NetWork --> Disable Cache(while DevTools is open)
#### 21.idea 中自动注入Autowried报错
	File-Project Structure 页面 Facets下删掉 Spring
#### 22.取消idea输出行数控制
	在idea.properties中添加如下内容
	idea.cycle.buffer.size=disabled
#### 23.idea tomcat部署项目
	Run/Debug Configurations --> add --> Tomcat Server --> Local
#### 24.idea通过数据库生成实体类
	view-->Tool Window-->Database-->+-->Data Source-->mysql-->填写信息(下载对应驱动)-->对应表右键Spripted Extensions-->POJO.clj

#### 25.idea使用lombok

```java
1.导入lombok
2.idea设置
settings -> build -> compiler -> annotation processors -> enable annotation processing
```

