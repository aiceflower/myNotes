
public class MapToArray{

	public static void main(String[] args) {
        Map<String,String> map = new HashMap<>();
        map.put("a","1");
        map.put("b","2");
        map.put("c","3");
        map.put("d","4");
        //存放键值的对象
        NameValuePair[] params = new NameValuePair[map.size()];
        //可并发的Integer
        AtomicInteger index = new AtomicInteger(0);
        map.forEach((key,value) -> {
            params[index.getAndIncrement()] = new NameValuePair(key,value);
        });
        System.out.println(Arrays.toString(params));
    }
}