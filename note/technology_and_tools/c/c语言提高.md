### 一、数组

#### 1.数组做函数参数退回问题

```c
void sort(int arr[8]);//arr使用时c当成指针使用
//退化
void sort(int *arr, int arr_len);
```

1.被实参和形参的数据类型不一样
2.形参中的数组C语言把它当作指针处理【这是c语言的特色，降低了形参的大小】
3.故需要把数组的首地址和长度传给被调用函数

#### 2.数组类型，指针数组变量，数组指针变量

##### 2.0数组类型

```c
int arr[] = {1,2,3,4,5};
```

##### 2.1指针数组变量

```c
char *arr[] = {"abc","def"};//数组中存放指针
//指针数组的自我结束能力 (在不知道数组个数的情况下，老程序员会人为的添加结束符)
char *keywords[] = {
  "while",
  "case",
  "static",
  "do",
  '\0'//这里 '\0' 0 NULL 是一个意思
}
for(int i = 0;keywords[i] != NULL;i++){
	printf("%s\n",keywords[i]);
}
```

##### 2.2 数组指针变量

```c
//法1  共有三种方法
typedef int(MyType)[5];//数据类型为 int[5];
Mytype myarr;//类型 定义变量  类似 int n;
MyType *p;//定义数组指针，指向数组的指针
p = &myarr; //指针指向数组
int arr[5];
p = &arr;//与上一样
printf("%d\n",(*p)[2]);
//法2
typedef int(*MyTypePoint)[5];
int arr[5];
MytypePoint mp;
mp = &arr;
(*my)[1] = 2;
//法3
int (*myP)[5];
int c[5];
myP = &c;
(*myP)[2] = 3; 
```

#### 3.多维数组

##### 3.1多维的本质

```c
int a[3][5];
//a+1的步长是 4 * 5 = 20 , 一维的长度
//多维数组名的本质，是一个数组指针
int (*pArr)[5];
pArr = a;
pArr[2][2] = 3;
// (a+i)代表整个第i行的地址 二级指针
// *(a+i) 第i行首元素的地址 一级指针
// *(a+i) + j ==> &a[i][j]
// *(*(a+i)+j) ==> a[i][j] 的值
```

##### 3.2二维数组做函数参数退化问题

```c
void f(int a[5]) --> void f(int a[])-->void f(int *a)
void g(int a[3][3])-->void g(int [][3])-->void g(int (*a)[3])
待价关系
一维数组 char a[3]    -->   char *
指针数组 char *a[3]   -->   char **a
二维数组 char a[3][3] -->   char(*a)[3]
```



### 二、零碎知识 

#### 1.为什么引入数据类型
0.数据类型的用途：是为了在编译器中方便表示现实生活中的人事物
1.数据类型的本质：是固定大小内存块的别名。
2.b和&b所代表的数据类型不一样
3.如何定义：1.数组类型 2.数组指针 3.数组类型和数组指针类型的关系

```c
	int a =;//给我分配4个字节内存
	int b[10];//告诉编译器，给我分配40个字节内存
	// b代表数组首元素的地址 b+1跳4个字节
	// &b代表整个数组的地址 &b+1 跳40个字节
```
#### 2.对数据类型起别名
```c
	typedef struct Test{
		ing age;
	}Test1;
	void main(){
		struct Test t1;//去掉struct报错
		Test1 t1;//这里不报错，这就是别名的区别
	}
```
#### 3.变量
1.变量的本质
	程序通过变量来申请和命名内存空间
	通过变量名访问内存空间，一段连续的内存空间的别名，是一个门牌号
	对内存可读写，通过变量往内存中读写，不是向变量读写数据
2.变量修改内存的几种方式

```c
	int a = 10;//直接修改
	*((int*)1245024) = 200;//间接修改==>通过内存地址，后面的数字是内存地址
	{
		char *p ;
		p = 1245024;
		*p = 300;//间接修改==>通过指针。把地址赋值给指针，通过*p间接修改内存内容。
	}
```
3.变量的三要素
	名称 大小 作用域
4.变量的生命周期

#### 4.void

1.void 字面意思是无类型，void * 则为无类型指针，void * 可以指向任意类型的数据

2.void *指针作为右值赋值给其它指针时需要强制类型转换，如 malloc

3.开发中的 void **

```c
int init(void **handle);//别人提供的接口

void *handle;
init(&handle);//自己调用 具体handle中定义了什么结构体，调用者不知道，被开发者封装起来了

```

#### 5.const

```c
const int a;
int const a ;//这两个一样，代表一个常整形数
const char *c;//c是一个指向常整形数的指针，内存数据不能修改
char * const c;char buf[10];//常指针，指针变量不能修改，内存可以修改
const char * const e;//均不能修改
看const是在*左边(修饰的是内存)还是右边(修饰的是指针变量)
c语言中的const是冒牌货：
const int a  = 10;
a = 15;//报错
int *p = &a;
*p = 20;//此时a变为10
```

#### 6.scanf

```c
scanf("%n",&num);//先检查缓冲区有没有数据，如果有数据则取出，如果没有数据则让用户输入， 如果所拿的数据是整型或实型会忽略空白，如果是字符型则不会忽略
scanf("%n",&num); // 如果用户输入 1 2 则不会第二次等待
rewind(stdin);//将缓冲区中数据全部清空
```

#### 7.栈方向

```c
//测试栈开口方向，不管向上还是向下，要先定一个方向轴
int a ;
int b;
printf("&a:%d,&b:%d\n",&a,&b);//b < a 说明开栈开口向下
//对于 char buf[10]; 的方向不管栈开口向上还是向下 buf[1]的地址都比buf[0]的大
```

#### 8.函数指针与回调函数

```c
//定义函数指针
int (*fun)(int data);
typedef int (*myfun)(int data);

int plusFive(int d){
    return d + 5;
}
void testFun(int n, int (*fun)(int)){
    printf("%d\n",fun(n));
}

void test(){
    fun = plusFive;
    myfun m = plusFive;
    printf("a:%d\n",m(1));
    printf("b:%d\n",(*m)(1));//这两种调用方式都可以
    for (int i = 0; i < 5; i++) {
        testFun(i, m);
    }
}
```

#### 9.字符串函数

```c
strcpy(s1, s2);//复制字符串 s2 到字符串 s1。
strcat(s1, s2);//连接字符串 s2 到字符串 s1 的末尾。
strlen(s1);//返回字符串 s1 的长度。
strcmp(s1, s2);//如果 s1 和 s2 是相同的，则返回 0；如果 s1<s2 则返回小于 0；如果 s1>s2 则返回大于 0。
strchr(s1, ch);//返回一个指针，指向字符串 s1 中字符 ch 的第一次出现的位置。
strstr(s1, s2);//返回一个指针，指向字符串 s1 中字符串 s2 的第一次出现的位置。
```



#### 99.练习题

```c
//1.这两个参数占几个字节，为什么
void test1(int a[3],int b[3][3]);

//2.如果第一次等待输入的时候 输入了 1 2 第二次还会等待吗？为什么
void test2(){
    int n;
    scanf("%d",&n);
    scanf("%d",&n);
}
//3.如果p的首地址为0x0001,则以下程序输出什么，为什么？
void test3(){
    char p[10];
    printf("%d,%d",p+1,&p+1);
}
//4.说下为什么引入数据类型，数据类型的本质是什么，变量的本质是什么？

//5.以下程序的输出结果是什么为什么？
void test5(){
    char c[] = "abcd";
    printf("%d,%d\n",strlen(c),sizeof(c));
}
//6.以下程序能否编译通过，如果能通过输出结果是什么，如果不能，为什么？
void test6(){
    char buf[5] = "abcde";
    char *p = buf;
    p++;
    //buf++;
    printf("%c,%c",*p,*buf);
}
//7.画出以下程序内存四区图
void test7(){//buf 与指针的区别
    char buf[] = "abcd";
    char *p = "efgh";
}
//8.以下程序运行报错吗，为什么？ //野指针 指针变量与指针指向的内存空间
void test8(){
    char *p = (char *)malloc(sizeof(char *) * 10);
    if (p != NULL){
        free(p);
    }
    
    if (p != NULL){
        free(p);
    }
}
//9.以下程序输出结果是什么，为什么
void test9(){
    char *p = "abcde";
    *(p+1) = 'f';
    printf("%s\n",p);
}
//10.以下程序a的值能不能被修改，以下程序能否通过编译，如果可以输出什么，如果不能为什么？
void test10(){
    int const a = 10;
    char * const buf = "abc";
    //buf++;
    printf("%s\n",buf);
}
//11.定义一个指针数组变量 和 一个数组指针变量

```



### 三、内存四区
#### 1.四区结构
	1.堆区
		malloc/new/free/delete 操作系统管理
		一般由程序员分配与释放，程序员不释放，程序结束时可能由操作系统回收。
	2.栈区
		程序局部变量
		由编译器自动分配释放，存放函数的参数值，局部变量的值等
		栈的开口方向，一般向下。根据栈的特点可以通过程序验证开口方向
		char buf[64];//不管是开口向上，还是开口向下 buf+1 都比buf大
	3.全局区
		常量和全局变量，操作系统管理
		字符常量与其它常量的存储位置，程序结束后操作系统释放
	4.代码区
		操作系统管理。
	
		存放函数体的二进制代码
#### 2.示例
```c
void main(){
  int a;//告诉编译器给我分配内存
  a = 10;//cpu里面执行
}	

char *getNUm(){
		char buf[10];//临时变量存放在栈区，方法结束后释放，注意这里不是堆区
		strcpy(buf,"12345");
		return buf;
	}
```
#### 3.函数的调用

​	函数执行后被调用的函数分配的内存，栈中的内存释放，堆中的内存和全局区的内存可以被原函数继续使用。

### 四、指针

指针学习路线：

- 内存四区模型和函数调用模型
- 函数内元素
  - 深入理解数据类型和变量的本质，即内存属性
  - 一级指针内存布局图 (int *, char *)
  - 二级指针内存布局图(int \*\*, char \*\*)
- 函数间元素
  - 主调函数分配内存，还是被调函数分配内存
  - 主调函数如何使用被调函数分配的内存（关键点：指针做函数参数）

#### 1.指针理解
	0.指针做输入在主调函数分配内存，指针做输出在被调函数分配内存
	1.理解指针的的关键是内存，没有内存哪里来的指针
	2.指针指向谁就把谁的地址赋给指针
	3.c语言可以在栈上和堆上分配内存
	4.char buf[64];//不管是开口向上，还是开口向下 buf+1 都比buf大
	5.指针也是一种数据类型						
		指针的数据类型代表它向指向的内存空间的数据类型，指针步长根据所指向内存空间的数据类型来定
		指针是一种变量，占有内存空间，用来保存内存地址。4个字节。
		*p 在申明时，* 表示所申明的变量为指针
		*p 在使用时，* 表示操作指针向指向的内存空间的值
		*p 在左相当于往内存空间写值  *p = 10;
		*p 在右相当于从内存空间取值  int a = *P;
		*就像一把钥匙，通过一个地址(&a),去修改变量所指的内存空间
		不断的给指针赋值，相当于不断改变指针的指向,改变p只是改变了指针变量，不会改变内存p++ 和内存无关
		保证指针指向的内存可以被修改
		char *p = "abcde";//指向常量区，数据不能修改 *(p+2) = 'f';//报错
	6.指针做函数参数(主调函数分配内存，被调函数判断为NULL返回不处理)
		指针做函数参数时，多级指针
			站在编译器的角度，只需要分配4个字节的内存
			当我们使用时才关心指针指向的是一维还是二维
			指针做函数参数用n级指针改变n-1级指针的值
	7.避免出现野指针
		{
			int *p = NULL;
			p = "abcde";
			if( p != NULL){
				free(p);
				p = NULL://要写这句话，要不然会出现野指针，指针指向的内存空间释放了，但是指针的值没有变，还是指向那块无用的空间。
			}
		}
		避免：定义指针的时候指针赋值为NULL,释放内存的时候，指针赋值为NULL。
	8.通过改变指针遍历数组
		{
			char *p1 = NULL:
			p1 = (char *)malloc(100);
			strcpy(p1,"abcdef");
			char *p2 = NULL;
			for(int i = 0;i<10;i++){
				p1 = p2 +i;
				printf("%d",*p1);
			}
		}
	9.间接赋值的工程意义
		函数调用时，形参传给实参，用实参取地址，传给形参，在被调用函数中用*p，来改变实参，把运算结果给传出来。
	10.间接赋值的三个条件
		1.定义一个变量(实参)，定义一个变量(形参)
		2.建立关联，把实参取地址传递给形参
		3.通过形参间接修改实参的值
	11.不要把局部申请的内存传递到外部，方法执行完会回收。 *
	12.字符串复制精简写法  while(*to++ = *from++);

#### 2.指针操作
```c
1.数组 字符串
	1)C语言的字符串是以0结尾的字符串
	2)C语言中没有字符串类型，用字符数组模拟字符串
	3)字符串的内存分配，可以在堆，栈，全局区 
2.字符数组初始化
	1)
		//指定长度
		char buf[10] ={'a','b','c','d'};//后面的buf[4]-buf[9] 置0
		//char buf[2] ={'a','b','c','d'};//个数大于内存大小，报错
		//不指定长度，编译器会自动帮程序员求元素个数
		char buf1[] = {'a','b','c','d'} //不是一个以0结尾的字符串，只是一个字符数组
	2)用字符串初始化字符数组
		char buf[] = "abcd";//作为字符数组是5个字节，作为字符串是4个字节
		//strlen()求字符长度,不包括0
		//sizeof()内存块大小。求数组这个数据类型(固定大小内存块的别名)的大小。
	3)通过数组下标和指针操作数组
		int i = 0;
		char *p = NULL;
		char buf5[128] = "abcdef";
		for(i=0;i<strlen(buf5);i++){
			printf("%c",buf5[i]);
		}//通过数组下标 
		p = buf5;//buf5代表数组首元素的地址
		for(i=0;i<strlen(buf5);i++){
			printf("%c",*(p+i));
		}//通过指针
3.[]的本质
	1.[]和*p是一样的，只不过是符合程序员的阅读习惯 buf5[i] ==>buf5[0+i] ==>*(buf5+i)
	2.buf5是一个指针，只读的常量，buf5是一个常量指针，为什么设置成常量，buf是放在栈中的，方便析构的时候找到内存的地址，如果不是常量改变了就找不到了，无法析构了
	3.buf(数组首地址)和指针的区别
		buf[20] = "abcd";//字符串常量初始化字符数组，这种方式可以修改内存
		在栈区buf[0] = a;buf[4] = '\0';内存分配在栈上
		*p = "efgh";// p是内存地址，指向常量字符串，这种方式不能通过指针修改内存
		//别眨眼，区别来了
		char buf[] = "abcd"; //首先 "abcd"在全局区，然后buf在栈上分配内存，然后buf[0] = 'a',buf[1] = 'b'... 有两份
		char *p; // 指针存放的是某地址，指向某块内存空间
	4.推演 
		a[i] --> a[0+i] -> *(a+i)//a放到0，把中括号换小括号，在外面加*
4.理解递归
	1.参数的入栈模型
	2.函数的嵌套调用
5.步长
	int c[10] = {0};//编译时已经确定每个元素为0
	//对于一维数组c规定
	//1.c(数组名)是数组首元素地址 c+1步长为4
	//2.&c是整个数组地址 &c+1步长为10*4
	//3.数组首元素地址跟数组的地址值相等
6.多维数组的本质
	int a[3][5];//a+1的步长 5*4 = 20字节 *
	//多维数组的本质是一个数组指针，步长是一维的长度
	//a+i代表整个第i行的地址 二级指针
	//*(a+i)代表第i行首元素地址 一级指针
	//*(a+i) +j ==>&a[i][j]
	//*(*(a+i)+j) ==> a[i][j] 元素值
7.中括号推演
	a[i][j]  --> a[0+i][j] --> *(a+i)[j] -> *(a+i)[0+j] -> *(*(a+i)+j)
8.二维数组做函数参数的退化
	void f(int a[5])==> void f(int a[]) ==> void f(int *a)
	void f(int a[3][3])==> void f(int a[][3]) ==> void f(int (*a)[3])
	等价关系：
		数组参数 				等效的指针参数
		一维数组：char a[30]	指针 char *a
		指针数组：char *a[30]	指针的指针 char **a  //**
		二维数组：char a[10][30] 数组的指针 char (*a)[30] //*
```

#### 3.二级指针

```c
//二级指针三种情况，打印，排序，画内存四区图
{
	//1.指针数组，数组中的每个元素是指针
	int *myarr[] = {"aaaa","bbbb","cccc"};
	//2.二维数组
	char p[3][4] = {"aaaa","bbbb","cccc"};
	//3.手工二维内存 [封装成指针做函数参数，申请内存]，内存结构同第一种类似，所以打印和排序可以和第一种共用
	char **p1 = (char **)malloc(3 * sizeof(char *));
	int i = 0;
	for(i=0;i<3;i++){
		p1[i] = (char *)malloc(10*sizeof(char *));//char buf[10];
    sprintf(pl[i], "%d%d%d",i+1,i+1,i+1);
	}
}
//-------------------------
int printArr(char **arr,int len){
    if(arr == NULL){
        return -1;
    }
    for(int i = 0;i<len;i++){
        printf("%s\n",*(arr+i));
    }
    return 0;
}

int printArr1(char arr[10][30],int len){
    if(arr == NULL){
        return -1;
    }
    for(int i = 0;i<len;i++){
        printf("%s\n",*(arr+i));
    }
    return 0;
}
int sortArr(char **arr, int len){
    if (arr == NULL){
        return -1;
    }
    int i,j;
    for (i=0; i<len-1; i++) {
        for(j = i+1;j<len;j++){
            if(strcmp(*(arr+i), *(arr+j))>0){
                char *tmp = *(arr+i);
                *(arr+i) = *(arr+j);
                *(arr+j) = tmp;
            }
        }
    }
    return 0;
}
int sortArr1(char arr[10][30], int len){
    if (arr == NULL){
        return -1;
    }
    int i,j;
    for (i=0; i<len-1; i++) {
        for(j = i+1;j<len;j++){
            if(strcmp(*(arr+i), *(arr+j))>0){
                char *tmp = (char *)malloc(30);
                strcpy(tmp, *(arr+i));
                strcpy(*(arr+i), *(arr+j));
                strcpy(*(arr+j), tmp);
            }
        }
    }
    return 0;
}
void getNum(char ***tmp){
    char **p;
    int num3 = 5;
    int i;
    p = (char **)malloc(sizeof(char *) * num3);
    for(i = 0;i< num3;i++){
        *(p+i) = (char *)malloc(sizeof(char) * 100);
        
        sprintf(*(p+i), "%d%d%d",i+1,i+1,i+1);
    }
    *tmp = p;
}
```

### 五、结构体

#### 1.结构体基本操作

```c
typedef struct User{
	char name[64];
	int age;
  // char *alias;
}user ,u1 = {"bb",15};//定义了一个数据类型 可以定义的时候直接初始化
struct User u1 = {"aaa",18};//初始化
user u2;

u2.age = 13;// u2.age 是寻址操作，计算age相对于u2大变量的偏移量

user *u3;
u3 = &u2;
u3 -> age; //同样 -> 也是计算，在cpu中进行，没有操作内存
//结构体变量的引用 .
//结构变量的指针针 ->
```

#### 2.结构体做函数参数

```c
void sCopy(user u1, user u2){//这种复制无效，类似两个int变量复制
 	u1 = u2; 
}
void sCody1(user *u1, user *u2){//这种有效，画出两种的内存图
  *u1 = *u2;
}
user u[3]; //在栈上分配内存
user *u3 = (user *) malloc(sizeof(user) * 3); //在堆上分配内存

int createUser(user **u,int len){
  user *tmp = NULL:
  tmp = (user *) malloc(sizeof(user) * len);
  // tmp->alias = (char *) malloc(64);
  if (tmp == NULL){return -1;}
  *u = tmp;
  return 0; 
}

//注意，结构体中如果有指针类型需要显示分配内存
```

### 六、数据结构

#### 1.堆

**概念：**

​	堆通常是一个可以被看做一棵树的数组对象，堆中某个节点的值总是不大于或不小于其父节点的值，堆总是一棵完全二叉树。

#### 2.C++中的堆实现

​	堆在c++中的实现为优先队列。

```c++
#include <queue>
priority_queue<int> max_heap; //大顶堆
priority_queue<int, vector<int>, less<int>> max_heap;//与上面等效

priority_queue<int, vector<int>, greater<int>> min_heap;//小项堆

```



​	