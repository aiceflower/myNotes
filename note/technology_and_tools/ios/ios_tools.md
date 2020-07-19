<center><h2>ios开发工具箱</h2></center>

### 一、xcode篇

#### 1.快捷键

- 代码提示 esc
- 代码上/下移动 alt+command+ [ / ]
- 定位到当前显示文件 command + shift + j
- 选中Navigator command + 1/2/3...
- command + control + up/down .h与.m文件跳转
- 返回至上一次光标位置：Control + Command + ←/→
- 左/右缩进 command+[ / ]
- 调试 F6单步调试、F7跳入，F8继续， 和Eclipse类似

#### 2.自定义快捷键

#### 3.自定义代码块

右键 --> Create Code Snippet

```objective-c
//nl
NSLog(@"%@",<#obj#>); //  <# 变量名 #> 
//nts
key>NSAppTransportSecurity</key>
  <dict>
  <key>NSAllowsArbitraryLoads</key>
  <true/>
</dict>
```



### 二、OC篇

#### 2.1 字符串

##### 2.1.1 OC字符串与C字符串转换

```objective-c
char *c = "hello";
NSString *c2oc = [NSString stringWithUTF8String:c];
const char *oc2c = c2oc.UTF8String;
```

##### 2.1.2 多行字符串

```objective-c
NSString *rows = @"row one"
                  "row two";
```

##### 2.1.3 重写toString

```objective-c
//重写 类的description方法  使用格式化字符串方法
return [NSString stringWithFormat:@"%@{name:%@}",[super description], self.name];
```

##### 2.1.4 OC中的StringBuilder

```objective-c
NSMutableString *nms = [NSMutableString string];//相当于 StringBuilder
[nms appendFormat:@"%d",1];
[nms appendString:@"abc"];
```

##### 2.1.5 NSString与NSData转换

```objective-c
NSData *data = [str dataUsingEncoding:NSUTF8StringEncoding];//字符串转为二进制
NSString *str = [[NSString alloc] initWithData:data encoding:NSUTF8StringEncoding];
```

#### 2.2 字典

##### 2.2.1 自定义对象转字典

```objective-c
NSDictionary *dic = [obj dictionaryWithValuesForKeys:@[@"name",@"size"];
```

#### 2.3 JSON

##### 2.3.1 json序列化与反序列化

```objective-c
//序列化 把对象转换成json数据 nd对象只能是 字典或数组 其它对象需要转换成这两者
NSData *data = [NSJSONSerialization dataWithJSONObject:nd options:0 error:&e];
//反序列化 把json数据转换为OC对象(只有数组和字典)
id json = [NSJSONSerialization JSONObjectWithData:data options:0 error:&error];
```

