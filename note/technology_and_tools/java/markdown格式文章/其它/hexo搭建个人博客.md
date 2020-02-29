###  hexo-github搭建个人博客



市面上已经有很多的博客网站了，像CSDN、博客园、简书等等，这些博客平台固然很好，在这些平台上可以直接写文章，发表，并且在各大搜索引擎上都能搜索到。但总感觉不自由，凸显不出来你的个性，俗话说金窝银窝不如自己的狗窝，本文就教大家搭建自己的博客窝。

#### 本文特点：

- 采用hexo静态博客框架，主题丰富
- 学习成本低，快速搭建
- 无须购买服务器，域名直接在github page平台上托管我们的博客
- 自动部署，代码提交到github上后自动部署，无须手动
- 好玩有趣的插件



#### 操作系统

理论上用什么操作系统都可以windows、mac、linux均可，本人采用linux，不过也不是买的linux服务器，本地使用VM搭建的linux，想搭建的可参考[VMware安装最新版CentOS7图文教程](https://blog.csdn.net/q2158798/article/details/80550626).

#### 安装Node.js

[下载Node.js](https://nodejs.org/zh-cn/download/)，根据不同的操作系统下载对应每户的Node.js，我下载的是linux64位的

![](https://raw.githubusercontent.com/aiceflower/assets/master/img/hexo/node%E4%B8%8B%E8%BD%BD.png)

下载完成后是.tar.xz为后缀的文件，可使用tar -Jxf xxx.tar.xz解压该文件

 创建以下两个软连接，这样可以在任何目录下执行node和

ln -s  ~/node-v10.15.2-linux-x64/bin/node /usr/local/bin/

ln -s ~/node-v10.15.2-linux-x64/bin/npm /usr/local/bin/

当然也可以linux系统中直接安装node.js，执行如下命令:

```shell
yum install node
```

安装完成后执行如下命令检查安装情况

```shell
node -v
npm -v
```



#### <span id="env_3">安装Git</span>

windows：到git官网上下载,[Download git](https://gitforwindows.org/),下载后会有一个Git Bash的命令行工具，以后就用这个工具来使用git。

linux：对linux来说实在是太简单了，因为最早的git就是在linux上编写的，只需要一行代码

```shell
 yum install git
```



#### <span id="env_4">安装Hexo</span>

把node和git安装好后就可以安装hexo了，首先创建一个目录，该目录用于存入hexo相关的文件，以后你写的博客内容也存放于这个目录中，cd到这个目录中，执行如下命令

```shell
#安装hexo
npm install -g hexo-cli
#检查hexo是否安装成功
hexo -v
```

安装完成后，在该目录下执行如下命令来对hexo进行初始化

```shell
hexo init
```

hexo初始化完成后包含如下相关目录：

_config.yml  db.json  node_modules  package.json  package-lock.json  scaffolds  source  themes

其中_config.yml用于存入hexo相关配置，source中存放的是博客文章，以后写的文章都要存放到这个目录下，themes中存放的是主题

启动hexo

```shell
#可使用-p port 指定启动端口，默认是4000端口
hexo s &
```

访问hexo,打开浏览器输入http://127.0.0.1:4000就可以看到博客页面了，如果访问不到，请检查ip、端口、防火墙

![](https://raw.githubusercontent.com/aiceflower/assets/master/img/hexo/hexo_init_page.png)

这样个人博客就搭建完成了，是不是很简单。

不过这才完成了一部分，如果你想要做成跟我一样的页面风格，或是选择其它风格就需要更换博客的主题。

你可以到hexo的官网[寻找主题](https://hexo.io/themes/),找到自己想要的风格后，点击主题名称会跳转到对应的github，将其克隆到上面的themes目录下就可以了，克隆命令:

```shell
git clone https://github.com/shenliyang/hexo-theme-snippet.git
```

然后打开_config.yml 文件，找到theme (hexo默认主题是theme: landscape)修改成你刚才克隆的主题名称，然后重新启动hexo就可以了。

如果hexo官网的主题你都不喜欢，也可以github网站搜索hexo选择一些高star的主题，当然你了解了hexo后也可自行修改，打造自己的主题。

#### 将hexo部署到github上

1.在github上创建自己的仓库，仓库名称为you_github_name.github.io,仓库必须为public，如果你愿意花钱也可以设置成私有

2.生成ssh-keygen添加到github

ssh-keygen用于提高代码的时候不用手动输入github用户名密码

```shell
ssh-keygen -t rsa -C "your_email"
```

执行上术命令后会,在用户家目录下生成.ssh目录复制id_rsa.pub中的内容到github上

登陆你的github,右上角找到settings，然后点击 SSH and GPG keys ,New SSH key

![](https://raw.githubusercontent.com/aiceflower/assets/master/img/hexo/github_ssh_key.png)

查看是否配置成功

```shell
ssh -T git@github.com
```

配置成功后，你还需要配置:

```shell
git config --global user.name "your_github_name"// 你的github用户名，非昵称
git config --global user.email  "your_github_email"// 填写你的github注册邮箱
```



2.安装deploy-git

```shell
npm install hexo-deployer-git --save
```

3.修改_config.yml

```yaml
deploy:
  type: git
  repo: https://github.com/you_github_name/you_github_name.github.io.git
  branch: master
```

4.部署

```shell
hexo c
hexo g
hexo d
```

部署后会在目录中生成public目录，deployer-git会把该目录下的静态资源提交到你之前创建的github仓库中。

5.设置GitHub Pages

github pages的原理是，将你提交到仓库的静态资源，使用github提供的域名进行访问。

进入你创建的your_github_name.github.io仓库，点击右边的settings,下拉到到GitHub Pages,选择master分支，保存，并随便选择一个主题。

![](https://raw.githubusercontent.com/aiceflower/assets/master/img/hexo/git_hub_pages.png)

设置完成后，会在GitHub Pages下面出现 Your site is published at https://xxx.github.io/

这时候你就可以通过https://xxx.github.io/,访问你的博客了。

6.hexo常用命令



#### 博主主题安装与配置

这里提供我自己博客主题安装与配置，如果你选择了其它的主题，配置也是类似的。

下载主题:

主题github：https://github.com/shenliyang/hexo-theme-snippet

```shell
themes]# git clone https://github.com/shenliyang/hexo-theme-snippet.git snippet
```

修改配置：

```yml
主配置：
# 网站title
title: 
#网站作者
author: aiceflower
#网站语言,在主题的languages目录下可查看支持的语言类型
language: zh-CN
#主题
theme: snippet

主题配置:
#一般的配置在snippet的README都能看到，这里主要说些常用的和容易忽略的配置。

#修改网站图标
#我用的是雪花图标，在https://www.iconfont.cn搜索自己喜欢的图标，下载并命名为favicon.ico ，替换掉
#themes/snippet/source 目录下的favicon.ico

#网站宣传语
branding: 你的宣传语
#宣传语修改后，在网页审查元素仍能看到 "从未如此简单有趣"，虽然被隐藏了，但还是感觉不爽，找到snippet/layout/_partial目录下的header.ejs 文件删除里面的"从未如此简单有趣"就可以了

# 各个小工具的设置

#搜索 (需要安装插件:npm i hexo-generator-json-content@2.2.0 -S)
jsonContent:
  searchLocal: true // 是否启用本地搜索
  searchGoogle: false //是否启用谷歌搜索
  posts:
    title: true
    text: true
    content: true
    categories: false
    tags: false
 #网站公告，支持 html 和 纯文本
 notification: |-
 	<p>公告</p>
 # 社交设置
 social:
  - name: Github
    icon: git
    href: //github.com/aiceflower
 # 文章分类设置
cate_config:
   show_count: true #显示数字
   show_current: true #高亮当前
 # 文章归档
 arch_config:
   type: 'monthly'
   format: 'YYYY年MM月'
   show_count: true
   order: -1
 # 标签云设置
 tagcloud:
  tag3d: false // 是否启用3D标签云
  textColour: '#444' // 字体颜色
  outlineMethod: 'block' // 选中模式(outline|classic|block|colour|size|none)
  outlineColour: '#FFDAB9' // 选中模式的颜色
  interval: 30 // 动画帧之间的时间间隔，值越大，转动幅度越大
  freezeActive: true // 选中的标签是否继续滚动
  frontSelect: true // 不选标签云后部的标签
  reverse: true // 是否反向触发
  wheelZoom: false // 是否启用鼠标滚轮
 # 友链{@链接名称：链接地址{@links:[,,,]}}
links:
  - Hexo官网: https://hexo.io/zh-cn/
 #自定义小工具
 1.widgets下增加一条 new_widgets
 2.layout/_widget下增加一个new_widgets.ejs文件
 注意名称对应
 
 #头部背景图片
 banner:
 	img: /img/header.jpg
 # 文章摘要
 # 文章摘要{@摘要显示优先级：自定义摘要 > 自动截取摘要 }
 # 自定义摘要范围{@<!--more-->:截取more之前的内容为摘要}
 # 自动截取摘要{@excerptLength:自动截取文章前多少个字为摘要，不配置默认：120字}
excerptLength: 120

#文章列表缩略图,在以下图片中随机选取
defaultImgs:
  - http://www.example.jpg //远程图片链接示例
  - /img/default-1.jpg //本地图片链接示例
#文章目录(关闭后阅读文章时显示widgets，右边的小工具)
toc: true

# 文章内声明{@declaration: {enable:是否开启,title:声明标题,tip:提示内容}}
declaration:
  enable: true
  title: '转载声明'
  tip: |-
      商业转载请联系作者获得授权,非商业转载请注明出处 © <a href="" target="_blank">Snippet</a>
      
## 文章打赏{@reward: {alipay:支付宝打赏,wepay:微信打赏,tip:打赏提示语; 链接都为空,关闭打赏功能}}
reward:
  alipay: ''
  wepay: ''
  tip: Maybe you could buy me a cup of coffee. 

#评论
# Valine评论 
valine:
  enable: true
  appId: 
  appKey:
  placeholder: 说点什么吧
  notify: false // 邮件通知
  verify: false // 验证码
  avatar: mm // avatar头像
  meta: nick,mail // 输入框内容，可选值nick,mail,link
  pageSize: 10
1.到LeanCloud官网(https://leancloud.cn/)注册一个账号
2.随便创建一个应用
3.进入应用-->设置-->应用key(这里有appId和appKey)
4.设置-->安全中心-->Web安全哉名中输入你的博客域名-->保存

#文章Front-matter设置(是文件最上方以 --- 分隔的区域)
title: 		标题
date: 		时间
categories: 分类 ['分类1','分类2'] 
tags: 		标签 ['标签1','标签2']    
comments: 	是否开启评论 true 
img:        自定义缩略图
```



#### 自动部署

本文使用Travis进行自动部署，对个人而言，就是让你的代码在提交到远程(这里是GitHub)，立即自动编译，自动化测试、自动部署等。不需要在担心更换电脑时，还要从新部署环境的问题，只要你能向远程推送文章，其他的事情就都可以交给Travis CI处理就ok了。

1.[注册travis账号](https://www.travis-ci.org)关联你的github就可以了

2.打开你需要自动部署的仓库(travis的settings会自动列出你github中所有的仓库，找到待部署的仓库，把右边的开关键拨到打开的状态。)

3.为 Travis CI 配置登录权限，Travis部署时需要将代码提交到github上所以需要github授权

首先去GitHub给账户添加一个Personal Access Token。打开GitHub，在 *Settings* 页面中找到 *Developer settings* ，打开 *Personal access tokens* 标签页，点击新建一个token。为这个Token取个名字，然后勾选 *repo* 这一栏，点击保存。如果不出意外，保存后屏幕上会显示一行字符串——这就是服务器为你生成的口令。

4.我们在Travis CI中把口令设置为一个环境变量打开刚才Travis CI中blog仓库的设置页面，在环境变量一栏把 *access_token* 作为一个value填写进去，然后给它取个好听的名字，点击添加。

5.拷贝文件:将主题目录下 gulpfile.js、travis.sh、.travis.yml拷贝到项目根目录下, Travis 要求项目的根目录下面，必须有一个.travis.yml文件。这是配置文件，指定了 Travis 的行为。该文件必须保存在 Github 仓库里面，一旦代码仓库有新的 Commit，Travis 就会去找这个文件，执行里面的命令。

6.修改配置，修改.travis.yml

```yaml
before_script:
  - git config user.name "YOUR_NAME"
  - git config user.email "YOUR_EMAIL"
  - sed -i'' "s~https://github.com/<yourname>/<reponame>.git~https://${GITHUB_ACCESS_TOKEN}@github.com/<yourname>/<reopname>.git~" _config.yml
script:
  - hexo g
  - hexo d
```

before_script 指的是在执行操作前要运行的命令。

- 这里我们指定了 git 的一些配置
- 然后使用sed命令将hexo的配置文件中的部署仓库的链接替换成了access_token形式，其中前面的那一串是你在`_config.yml`中的链接，后面的那一串是token形式的链接。 `${GITHUB_ACCESS_TOKEN}` 就是我们刚才为access_token配置的环境变量中的变量名。

然后提交到github上就会自动部署了，可以去travis网站查看部署日志。

7.自动部署时遇到的一些问题

1)access_token变量设置出错

```log
The previous command failed, possibly due to a malformed secure environment variable.
Please be sure to escape special characters such as ' ' and '$'.
For more information, see https://docs.travis-ci.com/user/encryption-keys.
```

解决: 取变量名称时不要使用横杠，单绰号，美元符号等特殊字符

2)WARN  No layout: index.html

解决: 这是Travis没有把主题从你的github仓库中下载下来，去主题目录下看，点不进去，这是github为了保护原作者，你在.travis.yml中写上克隆主题的命令，或是将对应主题中.git目录删除

3)如果使用gulp报less不是当前所支持的版本

解决: 将package.json文件中gulp的版本改为3.9.1，如果是4.0以上版本会报这个错

4)部署后favicon.ico图标不显示

解决：更换一下favicon.ico图标路径，放到img目录下, 引用 favicon: img/favicon.ico



#### 插件安装

<strong>页面点击出现爱心效果:</strong>

主题目录下 source/js/里面 新建一个love.js,复制下面的代码进去

```js
!function(e,t,a){function n(){c(".heart{width: 10px;height: 10px;position: fixed;background: #f00;transform: rotate(45deg);-webkit-transform: rotate(45deg);-moz-transform: rotate(45deg);}.heart:after,.heart:before{content: '';width: inherit;height: inherit;background: inherit;border-radius: 50%;-webkit-border-radius: 50%;-moz-border-radius: 50%;position: fixed;}.heart:after{top: -5px;}.heart:before{left: -5px;}"),o(),r()}function r(){for(var e=0;e<d.length;e++)d[e].alpha<=0?(t.body.removeChild(d[e].el),d.splice(e,1)):(d[e].y--,d[e].scale+=.004,d[e].alpha-=.013,d[e].el.style.cssText="left:"+d[e].x+"px;top:"+d[e].y+"px;opacity:"+d[e].alpha+";transform:scale("+d[e].scale+","+d[e].scale+") rotate(45deg);background:"+d[e].color+";z-index:99999");requestAnimationFrame(r)}function o(){var t="function"==typeof e.onclick&&e.onclick;e.onclick=function(e){t&&t(),i(e)}}function i(e){var a=t.createElement("div");a.className="heart",d.push({el:a,x:e.clientX-5,y:e.clientY-5,scale:1,alpha:1,color:s()}),t.body.appendChild(a)}function c(e){var a=t.createElement("style");a.type="text/css";try{a.appendChild(t.createTextNode(e))}catch(t){a.styleSheet.cssText=e}t.getElementsByTagName("head")[0].appendChild(a)}function s(){return"rgb("+~~(255*Math.random())+","+~~(255*Math.random())+","+~~(255*Math.random())+")"}var d=[];e.requestAnimationFrame=function(){return e.requestAnimationFrame||e.webkitRequestAnimationFrame||e.mozRequestAnimationFrame||e.oRequestAnimationFrame||e.msRequestAnimationFrame||function(e){setTimeout(e,1e3/60)}}(),n()}(window,document);


```

然后打开主题目录下 layout/layout.ejs，文件末尾添加如下代码:

```html
<!-- 页面点击小红心 -->
<script type="text/javascript" src="/js/love.js"></script>
```



<strong>点击出现文字效果:</strong>

主题目录下 source/js/里面 新建一个text.js,复制下面的代码进去

```javascript
<script type="text/javascript">
/* 鼠标特效 */
var a_idx = 0;
jQuery(document).ready(function($) {
    $("body").click(function(e) {
        var a = new Array("❤富强❤","❤民主❤","❤文明❤","❤和谐❤","❤自由❤","❤平等❤","❤公正❤","❤法治❤","❤爱国❤","❤敬业❤","❤诚信❤","❤友善❤");
        var $i = $("<span></span>").text(a[a_idx]);
        a_idx = (a_idx + 1) % a.length;
        var x = e.pageX,
        y = e.pageY;
        $i.css({
            "z-index": 999999999999999999999999999999999999999999999999999999999999999999999,
            "top": y - 20,
            "left": x,
            "position": "absolute",
            "font-weight": "bold",
            "color": "rgb("+~~(255*Math.random())+","+~~(255*Math.random())+","+~~(255*Math.random())+")"
        });
        $("body").append($i);
        $i.animate({
            "top": y - 180,
            "opacity": 0
        },
        1500,
        function() {
            $i.remove();
        });
    });
});
</script>
```

然后打开主题目录下 layout/layout.ejs，文件末尾添加如下代码:

以上代码用到了jquery，需要[下载jquery](https://raw.githubusercontent.com/aiceflower/assets/master/assets/js/jquery/jquery.min.js)，保存到source/js/目录下

```html
<script src="/js/jquery.min.js"></script>
<!-- 页面点击文字效果 -->
<script type="text/javascript" src="/js/src/text.js"></script>
```



<strong>添加卡通人物：</strong>

我在逛别人博客的时候偶然发现右下角居然有一个萌萌的卡通人物，还能根据你鼠标位置摇头，瞬间被吸引到了，赶紧也给自己博客添加一个吧。

输入如下命令获取 live2d ：

```npm
npm install --save hexo-helper-live2d  
```

输入以下命令，下载相应的模型，将 packagename 更换成模型名称即可，更多模型选择请[点击此处](https://github.com/xiazeyu/live2d-widget-models)，各个模型的预览请[访问原作者的博客](https://huaji8.top/post/live2d-plugin-2.0/)

```npm
npm install packagename
```

打开主题目录下的 _config.yml 文件，添加如下代码：

```yml
live2d:
	enable: true
	scriptFrom: local
	model: 
		use: live2d-widget-model-haruto #模型选择
	display: 
		position: right  #模型位置
		width: 150       #模型宽度
		height: 300      #模型高度
	mobile: 
		show: false      #是否在手机端显示

```

<strong>文章添加置顶效果</strong>

安装插件:

```shell
npm uninstall hexo-generator-index --save
npm install hexo-generator-index-pin-top --save
```

修改themes/snippet/layout/index.ejs,找到 class="post-title",在下面添加如下代码：

```ejs
<% if (post.top >= 10000) { %> <%# 这里可以改变判断条件 %>
    <i class="fa fa-thumb-tack"></i>
    <font color=7D26CD>置顶</font>
    <span class="post-meta-divider">|</span>
<% } %>
```

最后在写文章时，在`Front-matter`中加上top: number,number数值大于10000即可，如果为其它数字则默认按数字大小降序排列。