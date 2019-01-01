同StringBuilder类类似，StringBuffer类也是用来构建动态String对象的，但与StringBuilder不同的是StringBuffer是线程安全的，StrinigBuilder是线程不安全的。

### 成员变量：
```
	private transient char[] toStringCache;
```
toStringCache变量用于缓存StringBuffer对象维护的字符数组，作用是保证线程操作StringBuffer对象时都能让toString方法表现出相同的结果。
### 构造方法：

#### 无参构造方法：
```
	public StringBuffer() {
        super(16);
    }
```
无参构造方法，调用了父类的构造方法，初始容量为16个字符.
#### String参数的构造方法：
```
	public StringBuffer(String str) {
        super(str.length() + 16);
        append(str);
    }
```
一个String参数的构造方法，初始容量为字符串长度加16个字符.
#### CharSequence参数构造方法：
```
	public StringBuffer(CharSequence seq) {
        this(seq.length() + 16);
        append(seq);
    }
```
CharSequence参数的构造方法，同String参数的构造方法类似。

### 其它方法：
#### writeObject:
```
    private synchronized void writeObject(java.io.ObjectOutputStream s)
        throws java.io.IOException {
        java.io.ObjectOutputStream.PutField fields = s.putFields();
        fields.put("value", value);
        fields.put("count", count);
        fields.put("shared", false);
        s.writeFields();
    }
```
在进行序列化的时候保存StringBuilder对象的状态到一个流中。
#### readObject：
```
    private void readObject(java.io.ObjectInputStream s)
        throws java.io.IOException, ClassNotFoundException {
        java.io.ObjectInputStream.GetField fields = s.readFields();
        value = (char[])fields.get("value", null);
        count = fields.get("count", 0);
    }
```
反序列化时从流中获取StringBuild对象序列化之前的状态。
#### toString:
``` 
    public synchronized String toString() {
        if (toStringCache == null) {
            toStringCache = Arrays.copyOfRange(value, 0, count);
        }
        return new String(toStringCache, true);
    }
```
StringBuffer的toString方法与StringBuilder的toString方法有一点区别。这里是通过toStringCache成员构造String对象然后返回的。因为这里创建String对象调用的是String类的String(char[] value, boolean share)构造方法，是共享字符数组的,以提高效率不清楚的同学可以看下之前的[String源码](https://www.jianshu.com/p/b3e9deacd155)。所以设计者通过toStringCache来保证每次调用toString方法时得到的String对象是不变所结果。试想一下如果没有使用toStringCache,而是直接共享了value,那么在调用toString方法后，再对StringBuffer进行操作的时候之前返回的String对象就改变了，违背了String对象不变的设计理念。

对于其它的方法都是调用父类的方法实现，但都加上了synchronized来保证线程安全的效果。这里拿一个函数举例说明。其它的方法实现看其父类[AbstractStringBuilder](https://www.jianshu.com/p/77e82f324144)实现.
#### append:
```
	public synchronized StringBuffer append(String str) {
        toStringCache = null;
        super.append(str);
        return this;
    }
```
使用synchronized关键字保证方法是线程安全的，多个线程执行该方法时，只有获取锁的对象能够执行该方法。toStringCache=null;目的是让其指向一块空内存，保证toString返回的对象不被StringBuffer的操作所影响。然后就是调用父类的方法实现。
