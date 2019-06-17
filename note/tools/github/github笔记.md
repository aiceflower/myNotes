20170325repository
这是我gitHub上的第一个资源库，从这一步开始我将逐渐成为java大牛
大家都说GitHub好，一直没有机会尝试，现在终于抽出一点时间来使用这个工具了


github笔记
一.常用命令
1. 克隆项目
git clone https://github.com/aiceflower/test.git
2.查看状态
git status
3.建立跟踪
git add 日记
4.提交
git commit -m 增加了一个文件
5.同步内容到github
git push 
6.强制推送本地的替换服务端, 所以三思后行。
git push -u origin -f
7.git分支
创建 加上-b创建并切换
git branch (-b) dev
切换
git checkout dev
查看
	git branck	
	二、常用设置		
修改配置内容.git/config
git config --global user.name [username]
git config --global user.email [email]
三、解决冲突
1.先pull
2.修改冲突
3.再次提交
四、回到过去未来
过去
1. git log 查看提交记录 复制 commit
2.git reset --hard commit标识
未来
1. git reflog
2. git reset --hard commiId
五、建立里程碑
进入github项目主页，点击release,填写相应内容后发布
六、分支开发
创建一个bug分支，进行相应的操作
再合并bug分支到master分支上
七、开发经验
1.多用客户端和工具，少用命令行，除非是在linux上直接开发
2.每次提交前diff下自己的代码，以免提交错误的代码
3.回家前，整理好自己的工作区
4.并行的项目使用分支开发
5.遇到冲突时，搞明白冲突的原因，不要随意丢弃别人的代码
6.产品发布后，记得打一个tag，方便将来拉分支修bug

八、git克隆部分文件或目录
$ mkdir druid
$ cd druid 			 
$ git init // 初始化空仓库
$ git remote add -f origin https://github.com/alibaba/druid.git // 关联远程地址
$ git config core.sparsecheckout true // 开启Sparse Checkout模式
$ echo "druid/doc" >> .git/info/sparse-checkout // 设置需Check Out的文件
$ git pull origin master // Check Out

总结
指定远程仓库
指定克隆模式: 稀疏克隆模式
指定克隆的文件夹(或者文件)
拉取远程文件
九、git补充
一.git 配置
1.命令
	git config
2.说明
	config 配置有system级别 global（用户级别） 和local（当前仓库）三个 设置先从system-》global-》local  底层配置会覆盖顶层配置 分别使用--system/global/local 可以定位到配置文件
3.查看
	git config --system(global/local) --list
4.设置
	git config --global user.name "z3"
	git config --global user.email johndoe@example.com
二、仓库
1.创建
	git init 把这个目录变成git可以管理的仓库
2.回退版本
	git reset  --hard 版本号
3.显示版本号
	git reflog 
4.将工作区文件撤销到暂存区
	git checkout  --  file
5.分支命令
查看分支：git branch
创建分支：git branch name
切换分支：git checkout name
创建+切换分支：git checkout –b name
合并某分支到当前分支：git merge name
删除分支：git branch –d name

三、gitee
1.关联本地创建项目到gitee
a)创建gitee仓库
b)本地项目下 git init ; git add . ; git commit -m 'init' ; git push
c)git remote add origin gitee地址
d)git pull origin master --allow-unrelated-histories  ; 保存退出
e)git push origin master 

![1560784811966](C:\Users\alonglamp\AppData\Roaming\Typora\typora-user-images\1560784811966.png)