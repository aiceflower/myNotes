---

---

## 疯狂Java_突破程序员基本功的16课

### 第一课 数组与内存控制

#### 1.数组的初始化

- 数组用于存放多个类型相同的变量

- Java的数组是静态的，即当数组被初始化后该数组的长度是不可变的,初始化完成内存空间分配即结束

- Java的数组必须初始化后才能使用(注意区分对数组初始化和对数组变量初始化)。初始化就是为数组对象的元素分配内存空间，并为每个数组元素指定初始值

- Java的数组变量是一种引用类型的变量，指向堆内存中的数组对象

  

##### 数组初始化的两种方式

1. 静态初始化：初始化时由程序员显式指定每个数组元素的初始值，由系统决定数组长度

2. 动态初始化：初始化时程序员指定数组长度，由系统为数组元素分配初始值

   

```Java
	public static void main(String[] args) {
        //静态初始化
        // 方式1
        String[] type1 = new String[]{"刘备","关羽","张飞"};
        //方式2
        String[] type2 = {"刘备","关羽","张飞"};
        //动态初始化
        String[] type3 = new String[5];
    }
```

堆栈图

![数组初始化](img\数组初始化堆栈图.png)

##### 对象数组的初始化

```Java
	public static void main(String[] args) {
        //定义数组变量
        Person[] persons;
        //执行动态初始化
        persons = new Person[2];
        //创建person实例
        Person zhangsan = new Person("张三");
        Person lisi = new Person("李四");
        //给数组元素赋值
        persons[0] = zhangsan;
        persons[1] = lisi;
    }
```

堆栈图

![数组对象初始化](img\数组对象初始化.png)

#### 2.数组的使用

当数组变量指向一个有效的数组对象生，程序就可以通过该变量来访问数组对象。Java不允许直接访问堆内存中的数据，只能通过引用变量来访问数组。

##### 1.数组元素就是变量

在已有数据类型之后增加方括号，就会产生一个新的数组类型

- int -> int[] ：int[]类型数组
- Person -> Person[] ：Person[]类型数组
- int[] -> int [] [] ：int[] 类型数组 

无论哪种类型的数组，其数组元素相当于一个普通变量，当使用索引来使用数组元素时，将该数组元素当作普通变量使用即可，数组元素与普通变量的区别在于普通变量存放在栈中，数组元素存放在堆中。

##### 2.没有多维数组

在已有数据类型之后增加方括号，就会产生一个新的数组类型。反过来，将数组类型最后的方括号去掉就得到了数组元素类型。对于int[] [] 类型的数组，其数组元素就相当于int[] 类型的变量。所谓2维数组就是数组元素为1维数组的数组，3维数组是数组元素为2维数组的数组 …… N维数组是数组元素为N-1维数组的数组。

```Java
    //这是一个一维数组吗
	Object[] o1 = new Object[2];
	//加上如下操作后，o1扩展为2维数组
    Object[] o2 = new Object[3];
    Object[] o3 = new Object[3];
    o1[0] = o2;
    o1[1] = o3;
```

o1看似是一维数组，扩展为2维的数组，甚至可以再往下扩展下去。所以要理解多维数组与1维数组之前的本质关系。

### 第二课 对象与内存控制

#### 1.内存管理

Java内存管理分为两个方法：内存分配和内在回收。内存分配特指创建Java对象时JVM为该对象在堆内存中分配的内在空间；内存回收是指当该对象失去引用，变成垃圾时，JVM的垃圾回收机制自动清理该对象，并回收该对象所占用的内存。

#### 2.实例变量与类变量

##### 变量的分类

Java中的变量大体可以分为成员变量和局部变量。局部变量有以下三类：

- 形参: 在方法签名中定义，由方法调用者赋值，随方法结束而消亡
- 方法内的变量：在方法内部定义的变量，在方法内对其显示初始化，从初始化完成开始生效，随方法结束而消亡
- 代码块内的变量：在代码块中定义的变量，在代码内对其显示初始化，从初始化完成开始生效，随代码块结束而消亡

局部变量的作用时间很短，它们都被存储在方法的栈内存中。

类体内定义的变量称为成员变量，使用static修饰的称为静态变量或类变量，无static修饰的变量称为实例变量。

static从Java的角度来看就是将实例成员变成类成员。只能修饰在类中定义的成员部分，不能修饰外部类，局部变量，局部内部类。

##### 非法向前引用

表面上看Java类里定义成员变量时没有先后顺序，但实际上Java要求定义成员变量时必须采用合法的向前引用。

```Java
public class ErrorDef {
    //两个实例变量，报错，非法向前引用
    int num1 = num2 +2;
    int num2 = 10;
    //两个类变量，报错，非法向前引用
    static int num3 = num4+ 1;
    static int num4 = 10;
    //实例变量调用静态变量，正常,类变量初始化先于实例变量
    int num5 = num6 +1;
    static int num6 = 10;
    //当然静态变量调用实例变量肯定报错
}
```

##### 实例变量的初始化时机

实例变量属于对象本身，每次创建对象时都要为实例变量分配内存空间并执行初始化。

从语法角度程序可以在3个地方对实例变量执行初始化：

- 定义实例变量时指定初始值
- 非静态代码块中对实例变量指定初始值
- 构造器中对实例变量指定初始值

以上几种代码执行顺序1和2先于构造器，但1和2的顺序与定义顺序相同

```Java
class Person{
    public Person(){
        name = "c";
    }
    {
        name = "b";
    }
    private String name = "a";
    @Override
    public String toString() {
        return "Person{" + "name='" + name + '\'' + '}';
    }
}
```

对于以上代码创建对象后，打印对象name值为"c",实际上代码编译之后将代码赋值语句平移到构造器中，结果如下

```Java
class Person{
    private String name;
    public Person(){
        //这两句按定义顺序平移到构造器中
        name = "b";
        name = "a";
        name = "c";
    }
    @Override
    public String toString() {
        return "Person{" + "name='" + name + '\'' + '}';
    }
}
```

可使用javap -c 查看类编译后情况：

![javap看初始化顺序](img\javap看成员变量初始化执行顺序.png)

可以看到经编译器处理后，初始化块消失了，定义name变量时也没有初始化，为name指定初始值的语句都被提到了构造器中。且顺序与定义顺序相同，总是位于构造器的所有语句之前。

##### 类变量的初始化时机

类变量属于类本身，只有程序初始化该Java类时，才会为该类的变量分配内存空间，并执行初始化。每个JVM对一个Java类只初始化一次，即只会为类分配一次内存空间。

从语法角度程序可以在2个地方对实例变量执行初始化：

- 定义类变量时指定初始值
- 静态代码块中对类例变量指定初始值

以上两种地位平等，按定义顺序执行。

```Java
class Person{
    static {
        name = "b";
    }
    static String name = "a";
    @Override
    public String toString() {
        return "Person{" + "name='" + name + '\'' + '}';
    }
}
```

初始化完成后name的值为"a",使用javap看编译器处理后结果：

![1550741732262](img\javap看类变量初始化执行顺序.png)

定义name变量时也没有初始化，为name指定初始值的语句都被提到静态块中。且顺序与定义顺序相同。

下面程序更清楚的表现了类变量的初始化过程。

```Java
class Price {
    final static Price INSTANCE = new Price(2.8);
    static double initPrice = 20;
    double currentPrice;
    public Price(double pic){
        currentPrice = initPrice - pic;
    }
    public static void main(String[] args) {
        System.out.println(Price.INSTANCE.currentPrice);//1
        Price b = new Price(2.8);
        System.out.println(b.currentPrice);//2
    }
}
```

代码很简单，上面1，2两行都都访问实例的currentPrice变量，而且程序都是通过调用new Price(2.8)来创建实例，表面上看程序输出两个Price的currentPrice都应该是17.2，但实际是输出-2.8和17.2.

如果单从代码层面上来看这个问题很难得到正确答案，下面将从内存的角度分析。

第一次调用Price类是程序对Price类进行初始化，初始化分为两个阶段：

​	1.系统为Price的两个类变量分配内存空间

​	2.按初始化代码的顺序对类变量进行初始化

第一阶段，系统为INSTANCE和initPrice两个变量分配内存空间此时INSTANCE和initPrice的默认值分别为null和0.0。接着初始化进入第二阶段，程序按顺序依次为INSTANCE和initPrice赋值，此时initPrice的值为0，因此赋值结果currentPrice为-2.8。接着程序再次将initPrice赋值为20,但此时对INSTANCE的currentPrice已经不起作用了。

再次创建Price实例时initPrice为20，currentPrice为20-pic。

##### 访问子类对象的实例变量

子类的方法可以访问父类的实例变量，因为子类继承父类就会获得父类的成员变量和方法，但父类方法不能访问子类的实例变量，因为父类根本无从知道它将被哪个类继承，它的子类将会增加怎样的成员变量。但在极端情况下，可能出现父类访问子类变量的情况。

```Java
class Base{
    int i = 22;
    public Base(){
        //1:System.out.println(this.getClass());
        //3:System.out.println(this.i);
        this.display();
    }
    public void display(){
        //2:System.out.println(this.getClass());
        System.out.println(i);
    }
}
class Person extends Base{
    int i = 2;
    public Person(){
        i = 222;
    }
    public void display(){
        System.out.println(this.i);
    }
    public static void main(String[] args) {
        new Person();
    }
}
```

上面程序的main方法里只有一行代码: new Person();这行代码将会调用Person里的构造器，由于Person继承自Base，因此系统会自动调用Base中无参的构造器来执行初始化。在Base的无参构造器中只是简单的调用了this.display()方法来输出实例变量i的值。那么这个程序将输出多少呢？2? 22? 222? 都不是，运行程序会发现输出结果为0.

当程序调用new Person()时系统会为Person对象分配内在空间，定义变量i并赋初值0，接下来程序执行Person的构造器之前，先调用Base的构造器，此时经编译器处理后该构造器包含如下代码。

```Java
i = 22;
this.display();
```

因此程序将Base中定义的变量赋值为22，再调用this.display()方法。此处有一个关键this代表的是谁，this在构造器中时，this代表正在初始化的Java对象。从源码来看此时this位于Base构造器中，但这些代码实际放在Person()构造器中执行(Person构造器隐式调用了Base构造器)。由此可见此时的this代表的是Person对象。可以将1处代码打开，执行发现this代表的确实是Person。

可以看到编译时类型和运行时类型不同，这时通过该变量访问对象的实例变量时，该实例变量的值由声明该变量的类型决定，打开3发现输出值为22，但通过该变量访问对象的实例方法时，该方法行为将由它实际所引用的对象来决定，因此this.display()执行的是Person中的display方法，打开2会发现并无输出。而此时Person中的i还没有任何赋值因此输出0.

为了避免这种情况发生，在书写程序的时候应尽量避免在父类构造器中调用被子类重写的方法。

为了理解编译时和运行时类型不同，访问实例变量和实例方法也不同，我们再看一个例子：

```Java
class Base{
    int i = 22;
    public Base(){}
    public void display(){
        System.out.println(i);
    }
}
class Person extends Base{
    int i = 2;
    public Person(){
        i = 222;
    }
    public void display(){
        System.out.println(this.i);
    }
    public static void main(String[] args) {
        Base b = new Person();
        System.out.println(b.i);
        b.display();
    }
}
```

代码基本上还是刚才的代码，只是在main方法中使用父类引用指向了子类对象。这时调用b.i输出的是Base中i的值，调用b.display()调用的是Person中的display方法，调用方法大家都明白，这是java中的多态，需要注意的是调用变量的区别。

总结一句话就是：通过变量访问它所引用的对象的实例变量时，该实例变量的值取决于声明该变量时类型，当通过变量访问它所引用的对象的实例方法时，该方法取决于它所实际引用的对象的类型。

##### 内存中子类实例

对于上术例子，内存中存储Person对象时存储了几个i变量呢，答案是两个。因为只创建了一个Person对象，b只是引用了Person对象，此时调用b.i与通过display获取的i输出的是两个不同的值，由此可见，在创建Person对象时JVM为Person中的i申明了两个变量，一个存放父类中变量i的值，一个存放Person中变量i的值。即当程序创建一个子类对象时系统不仅为该类中定义的变量分配内存，也会为其父类中定义的所有实例变量分配内存，即使子类定义了与父类同名的实例变量。

#### 3.final修饰符

#####  final的概念

- final可以修饰变量，被fianl修饰的变量被赋初值后，不能对它重新赋值
- final可以修饰方法，被final修饰的方法不能被重写
- final可以修饰类，被final修饰的类不能派生子类

##### final修饰的变量

被final修饰的实例变量必须显式指定初始值，而且只能在如下三个位置指定初始值：

- 定义final实例变量时指定初始值
- 在非静态代码块中为final实例变量指定初始值
- 在构造器中为final实例变量指定初始值

对于普通实例变量，系统可以对它执行默认的初始化，但对于final的实例变量必须由程序员显式指定初始值。

被final修饰的类变量，同样必须显式指定初始值，且只能在两个地方指定初始值：

- 定义final类变量时指定初始值
- 在静态代码块中为final类变量指定初始值

final的另一个功能：

```Java
class Price {
    final static Price INSTANCE = new Price(2.8);
    final static double initPrice = 20;
    double currentPrice;
    public Price(double pic){
        currentPrice = initPrice - pic;
    }
    public static void main(String[] args) {
        System.out.println(Price.INSTANCE.currentPrice);
        Price b = new Price(2.8);
        System.out.println(b.currentPrice);
    }
}
```

上述代码与之前的代码相比只是对initPrice变量增加了一个final修饰符，执行程序会发现，现在两次输出都是17.2, 很明显这是增加了final的缘故，难道final修饰符改变了程序的初始化过程。实际上如果定义final类变量时指定了初始值，而且该初始值在编译时就被确定下来，系统将不会在静态代码块中对该类变量赋值，而是在类定义中直接使用该初始值代替该final变量。(可用javap工具检测)，系统会将这个变量当成"宏变量"处理，也就是说所有出现该变量的地方系统将直接把它当作对应的值处理。

即final修饰符的一个重要的作用就是定义 “宏变量” ，当定义final变量时就为该变量指定了初始值，并且该初始值在编译时就确定下来，那这个final变量本质上就是一个 "宏变量" ，编译器会把程序中所有用到该变量的地方直接替换成该变量的值。

除了为final变量赋直接量外，如果被赋值的表达式只是基本的算术运算，字符串连接，没有访问普通变量，调用方法Java编译器同样会把这种final变量当成"宏变量"处理。

```Java
//宏变量
final int a = 5 + 2;
final double b = 1.2 / 3;
final String s1 = "Hello" + "World";
final String s2 = "Hello" + 99;
//调用了方法，无法在编译期定下来，不是宏变量
final String s3 = "Hello" + String.valueOf(99);
```

加深对final修饰符的印象

```Java
public static void main(String[] args) {
    String s1 = "HelloWorld";
    String s2 = "Hello" + "World";
    String str1 = "Hello";
    String str2 = "World";
    String s3 = str1 + str2;
    System.out.println(s1 == s2);
    System.out.println(s1 == s3);
}
```

上述代码s1为直接量"HelloWorld"，s2为两个字符串连接操作，在编译期就能确定值为"HelloWorld"所以系统会让s2直接指向字符串缓存池中的"HelloWorld",由此可见s1 == s2 为true。

对于s3，它的值由str1和str2进行连接后运算得到，由于这两个只是普通变量，编译器不会执行宏替换，因此无法在编译时确定s3的值,不会让s3指向缓存池中的"HelloWorld",因此s1 == s3为false。

为了让s1 == s3 为true也很简单，只要让编译器对str1和str2执行宏替换，这样编译器在编译阶段就可以确定str3的值，就会让s3指向缓存池中的"HelloWorld",即：

```Java
public static void main(String[] args) {
    String s1 = "HelloWorld";
    String s2 = "Hello" + "World";
    final String str1 = "Hello";
    final String str2 = "World";
    String s3 = str1 + str2;
    System.out.println(s1 == s2);
    System.out.println(s1 == s3);
}
```

对于实例变量初始化的地方有3个，但对于final修饰的实例变量，只有在定义该变量的时候指定初始值，才会有 ”宏变量“ 的效果，在代码块和构造器中为final实例变量指定初始值则无此效果。对于类变量也是如此只有在定义类变量时指定初始值才会有”宏变量“的效果，在静态代码块中指定初始值则无此效果。

##### 内部类中的局部变量

java中规定，如果程序需要在局部内部类中使用局部变量，那么这个局部变量必须是final修饰的。其原因为：对于普通局部变量而言，它的作用域就是停留在方法内，当方法执行结束，局部变量随之消失，但内部类则可能产生隐式的”闭包“，从而使得局部变量脱离经所在的方法继续存在。

```Java
public void test(){
    final String str = "Hello";
    System.out.println(System.identityHashCode(str));
    new Thread(() -> {
        System.out.println(System.identityHashCode(str));
        try {
            Thread.sleep(20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }).start();//1
}
```

上述代码，正常情况下，当程序执行完1代码后，该方法的生命周期就结束了,局部变量str的作用域也随之结束。但只要新线程里的方法没有执行完，匿名内部类的实例的生命周期就没有结束，将一直可以访问str局部变量的值，这就是内部类会扩大局部变量作用域的例子。

由于内部类可能扩大局部变量的作用域，如果再加上这个被内部类访问的局部变量同有使用final修饰，也就是说该变量的值可以随意改变，那将引起极大的混乱，因此java编译器要求所有被内部类访问的局部变量必须使用final修饰。

### 第三课 常见Java集合的实现细节

#### 1.Set和Map

Set用于存储一组无序，不可重复的元素集合，Map则代表由多个键(key) - 值(value)对组成的集合。表面上看它们之间相似性很少，但实际上有很多关系，可以说Map是Set的扩展。

Set集合与Map集合对应关系：

![Set集合与Map集合对应关系](img\Set与Map对比图.png 'Set集合与Map集合对应关系')

可以看到这些接口的类名如此相似，绝不是偶然现象。考察下Map的key可以发现 ，这些key具有一个特征，所有的key不能重复，key之间没有顺序。也就是说将Map的所有key集中起来，那这些key就组成了一个Set集合，所以，发现Map集合提供了如下方法来返回所有key组成的Set集合。

```Java
Set<K> keySet();
```

由此可见，Map集合的所有key且有Set集合的特征，只要把Map的所有key集中起来，那它就是一个Set，这实现了从Map到Set的转换。其实还可以实现从Set到Map的扩展 -- 对于Map而言，相当于每个元素都是key-value的Set集合。

对于一个Map，它的本质是一个关联数组，其实可以改为用一个Set集合来保存它们，反正上面的关联数组中key-value有严格的对应关系，那么干脆将key-value捆绑在一起对待。

![](img\Map转换为Set.png)

为了把Set转换成Map，可以考虑新增定义一个SimpleEntry类，该类代表一个key-value对，当Set集合元素都是SimpleEntry对象时，该Set集合就能被当成Map使用。

下面示范了如何将一个Set扩展成Map：

```Java
public class SimpleEntry<K,V> implements Map.Entry<K,V>, Serializable {
    private final K key;
    private V value;
    public SimpleEntry(K key, V value){
        this.key = key;
        this.value = value;
    }
    public SimpleEntry(Map.Entry<K,V> entry){
        this.key = entry.getKey();
        this.value = entry.getValue();
    }
    @Override
    public K getKey() {
        return key;
    }
    @Override
    public V getValue() {
        return value;
    }
    @Override
    public V setValue(V value) {
        V oldValue = this.value;
        this.value = value;
        return oldValue;
    }
    /**
     * 根据key比较两个SimpleEntry是否相等
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if(obj == this){
            return Boolean.TRUE;
        }
        if( obj.getClass() == SimpleEntry.class){
            SimpleEntry entry = (SimpleEntry)obj;
            return this.key.equals(entry.getKey());
        }
        return Boolean.FALSE;
    }
    /**
     * 根据key计算hashCode
     * @return
     */
    @Override
    public int hashCode() {
        return key == null ? 0 : key.hashCode();
    }
    @Override
    public String toString() {
        return key + "=" + value;
    }
}
//继承HashSet实现一个Map
public class Set2Map<K,V> extends HashSet<SimpleEntry<K,V>> {
    /**
     * 清空所有的key-value对
     */
    @Override
    public void clear(){
        super.clear();
    }
    /**
     * 判断是否包含某个key
     * @param key
     * @return
     */
    public boolean containsKey(Object key) {
        return super.contains(new SimpleEntry<>(key, null));
    }
    /**
     * 判断是否包含某个value
     * @param value
     * @return
     */
    boolean containsValue(Object value){
        for (SimpleEntry<K, V> entry : this) {
            if(entry.getValue().equals(value)){
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }
    /**
     * 根据key获取value
     * @param k
     * @return
     */
    public V get(Object k){
        for (SimpleEntry<K, V> entry : this) {
            if(entry.getKey().equals(k)){
                return entry.getValue();
            }
        }
        return null;
    }
    /**
     * 将指定的key-value放入集合
     * @param key
     * @param value
     * @return
     */
    public V put(K key, V value){
        add(new SimpleEntry<>(key, value));
        return value;
    }
    /**
     * 将另一个Map的元素放入该Map
     * @param m
     */
    public void putAll(Map<? extends K, ? extends V> m){
        for (Map.Entry<? extends K, ? extends V> entry : m.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }
    /**
     * 根据key删除指定key-value对
     * @param key
     * @return
     */
    public V removeEntry(Object key){
        for (Iterator<SimpleEntry<K, V>> it = this.iterator();it.hasNext();){
            SimpleEntry<K, V> next = it.next();
            if(next.getValue().equals(key)){
                V value = next.getValue();
                it.remove();
                return value;
            }
        }
        return null;
    }
    /**
     * 获取Map中有多少key-value对
     * @return
     */
    @Override
    public int size(){
        return super.size();
    }
}
```

以上代码定义了一个SimpleEntry<K, V>当一个Set集合元素全是SimpleEntry<K, V>对象时，该Set就变成了一个Map<K, V>集合。

集合存储：

虽然集合号称存储的是Java对象，但实际上并不会真正将Java对象放入集合中，而只是在集合中保留这些对象的引用而已。

```Java
class Person{
    private String name;
    private Integer age;
    public Person(String name, Integer age) {
        this.name = name;
        this.age = age;
    }
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public Integer getAge() {return age;}
    public void setAge(Integer age) {this.age = age;}
    @Override
    public String toString() {
        return "Person{name=" + name + ", age=" + age + "}";
    }
}
public class JTest {
    public static void main(String[] args) {
        List<Person> list = new ArrayList<>();
        Person p1 = new Person("z3",18);
        list.add(p1);
        for (Person person : list) {
            System.out.println(person);
        }
        p1.setAge(28);
        for (Person person : list) {
            System.out.println(person);
        }

    }
}
```

上述代码可以证明，修改了p1的age属性后，list集合并未作改动，但随后打印的结果确是修改后的结果。

HashMap的集合存储结构:

![](img\HashMap存储结构.png)

HashMap,采用hash算法决定元素在集合中的位置，根据key计算出hash，来确定该元素在数组中的位置，如果不同的key计算出来的hash值相同，则这些hash值相同的key-value对构成一个链表，存储于该数组元素中。这就是人们所熟知的HashMap是数组加链表的结构。

查看HashMap的源码会发现，table对应的是Node数组，而Node则是一个链表。

```Java
transient Node<K,V>[] table;
static class Node<K,V> implements Map.Entry<K,V> {
    final int hash;
    final K key;
    V value;
    Node<K,V> next;
}
```

