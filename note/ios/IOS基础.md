### 一、Obj-c基础

#### 1.OC介绍

##### 1.常识

- oc完全兼容C，在C的基础上增加了面向对象的语法
- oc是一门弱语言
- oc源文件后缀为.m,意为message，代表了消息机制
- NS前缀：NextStep -> Cocoa -> Foundation框架中，代表类是从NextStep来的
- @符号：将c串转为OC串、OC中关键字的前缀。
- 对象中有一个isa指针指向代码段中的类(类被第一次使用时加载到代码段中)
- nil与NULL等同，作为指针变量的值。建义C的指针用NULL，OC的用nil
- 指向nil的指针对象，运行方法不会报错，但方法不执行。
- 属性不能在申明的时候初始化
- 对象方法申明用 - 类方法申明用 +
- 返回值为 instancetyp 代表返回当前类的对象
- 输出中文字符unichar用%C
- static不能修饰方法和属性，可修饰局部变量(变成静态变量存储在常量区)
- 获取类在代码段中的地址：调试看对象的isa指针、类方法中的self、对象的class方法，类的class方法
- 对象方法与类方法可以重名
- 如何写私有方法：方法不写申明，只写实现
- 重写对象的description方法，相当于java的toString方法
- 代码段中类的信息以Class对象的形式存储，isa指针指向父类的类对象 Class cl = [NSObject class]; //定义的时候就是指针，所以不用指针接收，或调用对象的class方法
- 获取存储方法的SEL对象：SEL s = @selector(方法名)，调用对象的方法本质就是向该对象发送一条SEL消息。[obj performSelector:s];//手动发SEL消息
- @property type name;自动生成getter、setter方法的申明与实现。可用点语法调用。
- 编译检查(看指针所指的对象是否有该属性和方法，可通过强制类型转换骗过)，运行检查(去对象内部看是否真的有该属性和方法)
- OC指针可在不同类型间相互指，都是8个字节的数据类型
- 两个万能指针NSObject(编译检查) 与 id (无编译检查)
- 野指针：指针指向的对象已经被回收了
- 僵尸对象：对象已经被回收，但对象所占的空间还没被分配给其它对象。不允许访问 edit schema -> run -> diagnostics -> Zombie Objects 勾选
- 有参数的方法方法名是带:的
- 循环引用时，其中一边不使用#import引用，而使用@class 类名 引用，在实现中再引用其头文件
- new 调用 alloc:init alloc调用 allocWithZone

##### 2.常见语法说明

- \#import指定为#include的增强版，文件引入只包含一次
- Foundation框架中提供了一些基本功能，输入输出，数据类型等
- @autoreleasepool 自动释放池
- NSLog：输出包含时间，程序名，进程号，线程号，输出信息、自动换行、输出新增类型值。

##### 3.注意

- NSLog的第一个参数为NSString,故需要加@符号
- 输出或拼接OC字符串用 %@
- 子类可以继承父类的私有属性，在子类中无法直接访问
- 访问修饰符只能修饰属性
- 属性写在实现类中，可实现真私有，xcode也无法提示
- OC的构造方法为init方法，\[[Obj allock] init] 等价于 [Obj new]
- self只能在构造方法中赋值，只有以initWith开头的方法才是自定义构造方法
- id指针只能调用方法，不能使用点语法
- 结构体无法直接存储到集合中，可先存储到对象中再存储到集合中

##### 4.命令号编译

```shell
gcc -c main.m  #编译
gcc main.o -framework Foundation #链接，与使用的库相关联
```

##### 5.OC关键字

1.支持C的所有关键字

2.新增关键字

- @interface 申明一个类

- @implmentation 实现一个类

- @public 公有

  ...

  



#### 2.基本语法

##### 1.OC中的数据类型

a） OC中支持C中的所有数据类型

- 基本数据类型：int duble float ...
- 构造类型：数组 结构体 枚举
- 指针类型：int *p1 
- 空类型：void
- typedef自定义类型

b）新增数据类型

- BOOL：实际是signed char，存放条件表达式的结果YES(1)和NO(0)
- Boolean：unsigned char，存放条件表达式的结果true(1)和false(0)
- class：类
- id：万能指针 
- nil：与NULL差不多
- SEL：方法选择器 selector选择器 是一个类，用来存储方法
- block：代码段类型

##### 2.字符串

​	NSString存放OC字符串地址

​	"hello" c的字符串

​	@"hello" OC的字符串

字符串转换

```objective-c
char *p = "hello";
// C to OC
NSString *ssss = [NSString stringWithUTF8String:p];
```



##### 3.异常处理

```objective-c
//无法处理C语言的异常如除0，一些OC的异常也无法处理如给指向nil的指针的属性赋值
@try {
    ...
}  @catch (NSException *exception) {
    ...
} @finally {
    ...
}
//一般不用异常处理，以下解决
// if (p != nil){...} 判断是否为nil
//if (pi respondsToSelector:@selector(sayHello)){} 判断pi对象是否有sayHello方法
```
##### 4.访问修饰符

- @private 私有本类访问
- @protected 本类或子类，默认
- @package 当前框架中访问
- @public 公共的

##### 5.类型判断

- [obj responseToSelector:@selector(sayHi)]; 判断指针指向的对象是否有该方法
- [NSObject instancesRespondToSelector:@selector(sayHello)];//判断类是否有该方法f
- [obj isKindOfClass:[NSObject class]];判断是否是指定类型或其子类型的对象
- [obj isMemberOfClass:[NSObject class]];判断是否是指定类型的对象,不含子类
- [obj isSubclassOfClass:[NSObject class]];判断是否是指定类型的子类的对象

##### 6.内存中的五大区域

- 栈：局部变量，作用域执行完就回收
- 堆：OC对象，使用C申请的空间
- BSS段：未初始化的全局变量，静态变量，一旦初始化就回收，并转存到数据段中
- 数据段：已经初始化的全局变量，静态变量，程序结束回收
- 代码段：代码，程序结束时回收

注：栈、BSS段、数据段、代码段回收系统自动完成

##### 7.内存管理

​	主要管理堆中的内存，其它区域系统会自动管理。

操作引用计数器：

发送一条retain消息，计数器+1，发一条release，计数器-1，发送retainCount消息取计数器的值。对象回收时会自动调用对象的dealloc方法。

##### 8.@property参数

- atomic(线程安全，默认)、nonatomic(建议)与多线程相关
- assign(l默认，直接赋值)、retain(标准MRC内存管理代码，属性是oc对象使用，还需要在dealloc中手动释放，ARC下无效)与生成setter方法实现相关
- readonly、readwrite(默认)与生成只读只写相关
- getter、setter与生成getter setter方法名字相关，指定方法名称
- strong(强指针)、weak(弱指针，在循环引用中一方使用弱指针)
- copy 无论是MRC还是ARC下属性是NSString用copy，深拷贝

##### 9.block

​	block是一个数据类型

​	block代码段中可以访问外部的变量，但是不可以修改外部变量，外部变量加 __block则block内部可修改

​	block可作为函数参数

​	当block作为函数返回值的时候，必需用typedef 定义

```objective-c
//申明格式： 返回值类型 (^变量名称)(参数列表);
//初始化block变量 写一个符合申明的代码段赋值给block变量
//执行：变量名();
void (^myBlock1)();//申明时如果有参数可只写参数类型
myBlock1 = ^void(){//无返回值这个void可以省略，但申明处不可省，没有参数代码段括号可以省略
  NSLog(@"哈哈");
};//无返回值，无参
int (^myBlock2)(int num1) = ^int(int num){//申明与赋值写在一起
  NSLog(@"哈哈%d",num)；
    return 0;
};//有返回值，有参
//如果定义太长，可用typedef简化
// typedef 返回值类型 (^名称)(参数);
typedef void (^newType)();
newType block1;

```



```objective-c

//#include "AppDelegate.h" 完全兼容C , 也可使用 #import引入头文件
@interface Hello : NSObject //定义类
{
    int num;//私有属性
}
-(id)init;
-(void) sayHello; //公有属性
@end

@implementation Hello
-(instancetype)init
{
    self = [super init];
    if (self){
        num = 101;
    }
    return self;
}
-(void)sayHello{
    NSLog(@"Hello class");
    NSLog(@"Num is %d",num);
}
@end

@protocol PeopleDelegate <NSObject>
-(void)onAgeChanged: (int)age;
@end

//代码块
int (^max)(int , int );

//为了使用代码块执行方便，给其指定一个类型
typedef void (^SayHello) ();

@interface PeopleListener : NSObject<PeopleDelegate>

-(void)onAgeChanged:(int)age;

@end

@implementation PeopleListener

-(void)onAgeChanged:(int)age{
    NSLog(@"age changed %d",age);
}

@end


@interface People : NSObject{
    int _age;
    NSString *_name;
}
//工厂方法
+(People*) peopleWithAge:(int)age andName:(NSString*)name;
-(id)initWithAge:(int)age andName:(NSString*)name;
-(int)getAge;
-(NSString*)getName;
@property int age; //自动生成 get set方法
@end


@implementation People

//工厂方法创建对象
+(People *)peopleWithAge:(int)age andName:(NSString *)name{
    return [[People alloc] initWithAge:age andName:name];
}
/*使用该方式，待验证
+(id) peopleWithAge:(int)age andName:(NSString *)name{
		return [[self new] initWithAge:age andName:name];
}
*/
-(instancetype)initWithAge:(int)age andName:(NSString *)name{
    self = [super init];
    if (self){
        _age = age;
        _name = name;
    }
    return self;
}
-(void)setAge:(int)age{
    NSLog(@"setAge");
    _age = age;
}
-(int)age{
    NSLog(@"getAge");
    return _age;
}
-(NSString*)getName{
    return _name;
}
@end

@protocol IPeople <NSObject>

-(int)getAge;

-(NSString*)getName;

//delegate 添加
-(void)setAge:(int)age;

@end


@interface PeopleImpl : NSObject<IPeople>{
    int _age;
}
-(id)init;
-(int)getAge;
-(NSString*)getName;
-(void)setAge:(int)age;

@property id<PeopleDelegate> delegate;

@end


@implementation PeopleImpl

-(instancetype)init{
    self = [super init];
    if (self){
        self.delegate = nil;
    }
    return self;
}

-(int)getAge{
    return _age;
}
-(NSString*)getName{
    return @"hello interface";
}
-(void)setAge:(int)age{
    if (_age != age){
        if (self.delegate){
            [self.delegate onAgeChanged:age];
        }
    }
    _age = age;
}



@end

void platform(){
    //Object-C 函数调用
    NSString *str = [NSString stringWithFormat:@"Helo %d",100];//OC 函数调用
    NSLog(@"%@",str); // OC字符串以@打头标识，NSString 占位符是@
    
    //Object-C 创建类
    Hello *hello = [[Hello alloc] init];
    [hello sayHello];
    
    //Object-C 自动生成get set @property
    People *p = [[People alloc] init];
    p.age = 10;//会调用 setAge方法
    [p setAge: 12];//set 方法
    NSLog(@"age:%d",[p age]);//get 方法 p.age 会调用 age方法
    
    //Object-C 工厂方法创建对象
    People *p1 = [People peopleWithAge:20 andName:@"zhangSan"];
    NSLog(@"name:%@",[p1 getName]);
    
    //Object-C String
    NSString *s1 = @"create String";//直接
    //格式化
    NSString *s2 = [NSString stringWithFormat:@"number is %d",100];
    //通过网络获得字符串内容，同步加载
    NSString *s3 = [NSString stringWithContentsOfURL:[NSURL URLWithString:@"https://api.github.com/users/aiceflower"] encoding:NSUTF8StringEncoding error:nil];
    NSLog(@"%@",s3);
    NSMutableString *nms = [NSMutableString string];//相当于 StringBuilder
    //查看帮助文档  help-> Developer Documentation
    
    //Object-C 数组
    NSArray *ns = @[@"Hello",@"world"];//通过@创建数组
    NSArray *ns1 = [NSArray arrayWithObjects:@"z3",@"l4",nil];//通过工厂方法创建,nil做为数组结尾，否则找不到结尾
    //通过配置文件y创建数组
    NSArray *ns2 = [NSArray arrayWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"data" ofType: @"plist"]];//data.plist array string-zhangsan，string-lisi
    
    //Object-C 可变数组
    NSMutableArray *nsa = [[NSMutableArray alloc] init];
    
    for (int i = 0; i<10;i++){
        [nsa addObject:[NSString stringWithFormat:@"item : %d",i]];
    }
    
    //Object-c 字典 相当于java中的map，l不可变字典
    NSDictionary *nd = @{@"name":@"zhangsan",@"age":@"18"};
    NSLog(@"%@",nd);//
    NSLog(@"%@",[nd objectForKey:@"name"]);//key
    
    NSDictionary *nd1 = [NSDictionary dictionaryWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"dict" ofType:@"plist"]];//dict.plist Dictionary string-zhangsan number-18
    NSLog(@"%@",nd1);//
    
    //Object-c 可变字典
    NSMutableDictionary *nmd = [[NSMutableDictionary alloc] init];
    [nmd setObject:@"list" forKey:@"name"];
    [nmd setObject:@"29" forKey:@"age"];
    NSLog(@"%@",nmd);//
    NSLog(@"%@",[nmd objectForKey:@"name"]);
    [nmd writeToFile:@"~/other/to_file" atomically:true];//字典数据持久化
    
    //文件操作
    NSFileManager *nfm = [NSFileManager defaultManager];
    NSString *path = @"~/Desktop/other/abc.txt";
    BOOL flag = NO;
    BOOL exists = [nfm fileExistsAtPath:path isDirectory: &flag];//文件是否存在
    //flag == YES 是目录 否则为文件 存在的情况下
    [nfm isReadableFileAtPath:path];//是否可读，可写，可删除
    NSDictionary *attr = [nfm attributesOfItemAtPath:path error:nil];//获取文件属性信息
    NSLog(@"file size:%@", attr[NSFileSize]);//只获取部分属性 文件大小
    [nfm subpathsAtPath:path];//获取所有子目录
    [nfm contentsOfDirectoryAtPath:path error:nil];//只获取子目录
    NSString *content = @"我爱北京天安门";
    NSData *data = [content dataUsingEncoding:NSUTF8StringEncoding];//字符串转为二进制
    [nfm createFileAtPath:path contents:data attributes:nil];//创建文件，内容 属性为默认
    [nfm createDirectoryAtPath:path withIntermediateDirectories:NO attributes:nil error:nil];//创建目录 是否一路创建下去 默认属性
    [nfm copyItemAtPath:@"src/path" toPath:@"to/path" error:nil];//拷贝文件
    [nfm moveItemAtPath:@"src/path" toPath:@"to/path" error:nil];//移动文件，可重命名
    [nfm removeItemAtPath:@"path" error:nil];//删除文件，不会移动到垃圾桶
    
    //时间处理
    NSDate *date = [NSDate new];//得到当前系统的格林威志时间
    NSDateFormatter *format = [NSDateFormatter new];
    format.dateFormat =@"yyyy-MM-dd HH:mm:ss";
    NSString *formatDate = [format stringFromDate:date];//格式化时间
    NSDate *currentDate = [format dateFromString:@"2019-10-08 10:10:10"];//转换为时间
    
    [NSDate dateWithTimeIntervalSinceNow:8 * 60 * 60];//8小时以后的时间，往前用负
    
    double sub = [currentDate timeIntervalSinceDate:date];//时间差
    
    //日历
    NSCalendar *calendar = [NSCalendar currentCalendar];
    NSDateComponents *com = [calendar components:NSCalendarUnitYear|NSCalendarUnitMonth fromDate:date];
    NSLog(@"year:%lu,month:%lu",com.year,com.month);
    
    //Object-c block 代码块
    max = ^(int a, int b){
        return a > b ? a : b;
    };
    printf("max is %d\n",max(4,5));
    
    SayHello sh = ^(){//函数闭包，在函数内部也能写函数
        printf("hello obj-c code block \n");
    };
    
    sh();
    
    //Object-c 协议 接口 (创建 m Object-C File 选择协议)
    PeopleImpl *pp = [[PeopleImpl alloc] init];
    NSLog(@"%@",[pp getName]);
    
    //Object-c delegate 事件监听机制
    PeopleImpl *pi = [[PeopleImpl alloc] init];
    [pi setDelegate:[[PeopleListener alloc] init ]];
    [pi setAge:21];
    
    //Object-c 类型判断
    NSLog(@"%d",[pi isKindOfClass:[NSObject class]]);// 1-是，0-否
    
    //Object-c n异常处理
    @try {
        @throw [NSException exceptionWithName:@"My error." reason:nil userInfo:nil];
    } @catch (NSException *exception) {
        NSLog(@"%@",exception);
    } @finally {
        NSLog(@"run...finally");
    }
    
    [NSThread sleepForTimeInterval:10];//睡10秒
    
    //表示控件坐标,一个结构体
    CGPoint *dot1 = {10,20};//初始化方式1
    CGPoint dot2;//2
    dot2.x = 10;
    dot2.y = 20;
    CGPoint dot3 = {.x = 10,.y = 20};//3
    CGPoint dot4 = CGPointMake(10, 20);
    NSLog(@"x=%lf,y=%lf",dot4.x, dot1->y);
    
    //表示控件大小
    CGSize *size = {10,30};
    CGSize size1 = CGSizeMake(10, 30);
    size1.height=10;
    size1.width = 30;
    
    //表示 坐标与大小
    CGRect r;
    r.origin.x = 10;
    r.origin.y = 20;
    r.size.height =10;
    r.size.width = 30;
    
    r.origin = (CGPoint){10,20};//{}可以初始化数组与结构体，编译器SB了，故需要告诉它
    r.size = (CGSize){10,30};
    r = CGRectMake(10, 20, 10, 30);
    
    //CGPoint CGSize等都是结构体无法直接存储到集合中，可先将这些结构体存储到对象中，然后再存储到集合中
    
    //包装结构体的类
    NSValue *v1 = [NSValue valueWithCGPoint:*dot1];
    NSArray *array = @[v1,v1];//存入集合
    NSString *s4 = [s1 copy];//浅拷贝
    NSString *s5 = [s1 mutableCopy];//深拷贝
    //拷贝
    //NSString -> copy -> 浅拷贝
    //NSString -> mutableCopy -> 深拷贝
    //NSMutableString -> copy -> 深拷贝
    //NSMutableString -> mutableCopy -> 深拷贝
    
    //copy方法定义在NSObject对象中，某个类要实现拷贝要遵循 NSCopyings协议，实现copyWithZone方法(自己做深浅拷贝处理)
    //如果想深拷贝：创建新对象，将对象的属性值r复制，返回。如果想浅拷贝：直接返回self
    
    //如何实现单例(重写allocWithZone方法)，看Dog类  new 调用 alloc:init alloc调用 allocWithZone
    //单例规范：提供一个类方法，方法名称为：shared类名 或 default类名
    //单例应用：如ii游戏面板的宽高一直不变，而eh且很多地方用到
}

@interface Dog : NSObject
{
    @public
    float _weight;
}
-(void)run;
-(void)eat;
@end

@implementation Dog
//单例
+(instancetype) allocWithZone:(struct _NSZone *)zone{
    static id instance = nil;
    if (instance == nil){
        instance = [super allocWithZone:zone];// new 调用 alloc:init alloc调用 allocWithZone
    }
    return instance;
}
-(void) run{
    _weight -= 0.5;
    NSLog(@"体重为%fKg",_weight);
}
-(void) eat{
    _weight += 0.5;
    NSLog(@"体重为%fKg",_weight);
}
@end

int main(int argc, char * argv[]) {
    platform();//C方式调用函数
  	//使用匿名对象
  	[[Dog new] run];
  
  	Dog *d = [Dog new];
  	//调用对象方法的两种方式
    [d run];//1.
  	SEL s_run = @selector(run);
  	[d performSelector:s_run];//2
  	
    /*
    @autoreleasepool {
        return UIApplicationMain(argc, argv, nil, NSStringFromClass([AppDelegate class]));
    }
     */
}

```



##### 3.类与对象

###### 1.面向对象

​	面向对象相较于面向过程，后期维护与修改更方便

###### 2.类

​	类：一群具有相同特征或行为的事物的统称，抽象的。本质是我们自定义的一个数据类型(在内存中开辟空间的一个模板)

描述类：凡是有xxx，会xxx的东东称为 xxx类。

设计类：三要素：类名、有何特征、有何行为。

​	对象：现实中一个具体的存在。

先有类还是先有对象？

​	从现实的角度先有对象后有类，从代码的角度先有类后有对象。

```objective-c
//类
@interface Person{
  NSString *_name;
  int _age;
  - (void)run{...}
  - (void)eatWith:(NSString *)foodName :(类型2)参数2{...}//方法名eat:、方法无返回值、参数类型NSString,参数名foodName
  - (int)sumWith:(int)n1 and:(int)n2{...}//多个参数命名，方法名sumWith: and:
}
//定义对象 类名 *对象名 = [类名 new];
//使用属性(public) 对象名->属性名 或 (*对象名).属性名
//使用方法 [对象名 方法名] [对象名 方法名:参数1 :参数2]
```







### 二、Swift基础

#### 1.基本语法

##### 变量，常量

```swift
//定义一个变量
var a = 1

//定义一个常量
let b = 3

//定义可选变量
var myName:String? = "pillow"
```

##### 数据类型

```swift
var str = "world" //自动类型识别
var s:String = "hello" //指定类型

//字符串连接
print(str + "." + s)
//s = s + 100 //报错
s = "\(s)\(100)"//引用变量方式拼接
```

##### 数组

```swift
//初始化数组
var arr1 = [Int](repeating:1,count:3)
var arr2:[Int] = [2,3,4]
var arr3 = [1,2,3]
var arr4 = [String]()

//访问数组
print(arr3[2])

//修改数组,使用append或 +=在数组末尾添加元素
arr3.append(4)
arr3 += [5]
print(arr3)

arr4 += ["zhang3"]
arr4 += ["li4"]
arr4 += ["wang5"]

//遍历数组
for i in arr4{
    print(i)
}

//带下标
for (idx,item) in arr4.enumerated(){
    print("num:\(idx),item:\(item)")
}

//合并数组
var arr5 = arr2 + arr3;
print(arr5)

//f数组元素个数
print("arr5大小为:\(arr5.count)")

//是否为空
print("arr5是否为空:\(arr5.isEmpty)")
```

##### 字典

```swift
//创建字典
var dict1 = [Int: String]() //keyType:valueType
var dict2 = ["name":"zhang3","age":"22"]
dict2["sex"] = "male"

//访问字典
print(dict2["name"])

//修改字典,key存在则修改，不w存在则添加
dict2.updateValue("张三", forKey: "name")
dict2["name"] = "z3"

//移除key-value
dict2.removeValue(forKey: "sex")
dict2["age"] = nil
print(dict2)

//遍历
for (k,v) in dict2{
    print("k:\(k)--v:\(v)")
}

//转换为数组
print(dict2.keys)
print(dict2.values)

//count和空
print("\(dict2.count)---\(dict2.isEmpty)")
```

##### 流程控制

```swift
for index in 0...100{//for循环0-100
    if index % 2 == 0 {//判断
        print("item:\(index)")
    }
}

var j = 0
while j < arr5.count{//while循环
    print(arr5[j])
    j += 1
}

myName = nil
if let name:String = myName {
    print(name)
}
```

##### 函数

```swift
func sayHello(name:String) {
    print(name)
}
var myFun = sayHello

myFun("good hello")

func getNum() -> (String,Int){
    return ("zhangsan",18)
}

let myNum = getNum()
```

##### 对象

```swift
//类
class Hi: NSObject {//继承
    override init() {//初始化方法，重写需要加 override
        print("init...")
    }
    func sayHi() {
        print("hi pillow")
    }
  
  	//静态方法，类方法
  	class func doSomething(){
        print("类方法...");
    }
}
var hi = Hi()
hi.sayHi()
Hi.doSomething() //调用类方法


//动态扩展
extension Hi{
  //类功能的动态扩展，所有子类也可拥有扩展方法  
  func sayHaha(){
    print("Haha...")
  }
}
hi.sayHaha()//调用扩展方法


//接口
protocol People {
    func getType() -> String
}

class Mail: People {
    func getType() -> String {
        return "mail";
    }
}

var mail = Mail()
print(mail.getType())


//命名空间
class com{//通过类
    class jfu {
    }
}
//以扩展的方法在命名空间下添加类，可写在不同文件中
extension com.jfu{
    class Animal{
        func eat(){
            print("吃东西...")
        }
    }
}

var an = com.jfu.Animal()
an.eat()

```

#### 2.IOS各方式间调用

##### i) Swift调用Obj-C

```swift
//1.创建swift项目
//2.创建Cocoa Touch Class文件
//3.创建Obj-c文件，将其头文件引入到 xxx-Bridging-Header.h文件中
//4.在其它swift文件中以swift语法正常调用Obj-C对象
```

##### ii) Obj-C调用Swift

```swift
//1.创建obj-c项目
//2.创建Cocoa Touch Class文件，选Swift语言，创建提示的头文件
//3.在使用的地方引入 工程名-Swift.h文件 然后使用Obj-c语法调用
```

