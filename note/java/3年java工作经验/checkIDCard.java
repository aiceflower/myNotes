/**
 * 校验18位身份证是否为真,工具类
 * 1.身份证的每一位乘以{7,9,10,5,8,4,2,1,6,3,7,9,10,5,8,4,2}求和
 * 2.将1中的和模上11取余数
 * 3.以2中的值为索引取{'1','0','X','9','8','7','6','5','4','3','2'}中的值为身份证最后一位
 */
public class IDUtil{

	private IDUtil(){

	}

	public static void main(String[] args) {
		System.out.println(checkId("123..."));
	}

	//校验身份证是否合法
	public static boolean checkId(String id){
		if(isEmpty(id)){
			throw new NullPointerException("id card not be null.");
		}else if(id.length()!=18){
			throw new RuntimeException("illegal id card.");
		}
		return id.endsWith(getLastNum(id));
	}

	//用于对身份证前17位相乘取和
	private static final int[] base = {7,9,10,5,8,4,2,1,6,3,7,9,10,5,8,4,2};

	//用于获取身份证最后一位
	private static final char[] lastValues = {'1','0','X','9','8','7','6','5','4','3','2'};

	//取余基数
	private static final int MOD_BY = 11;

	//用于将char转换为int
	private static final int CHAR_TO_INT = 48;

	//获取身份证最后一位
	private static String getLastNum(String id){

		int[] tmp = splitId(id);

		int sum = getSum(tmp);

		char lastChar = lastValues[sum%11];

		return String.valueOf(lastChar);
	}

	//身份证前17位与base相乘求和
	private static int getSum(int[] tmp) {
		if(tmp==null||tmp.length!=base.length){
			throw new NullPointerException("id card length invalide.");
		}
		int sum = 0;
		for (int i = 0; i < tmp.length; i++) {
			sum += tmp[i] * base[i];
		}
		return sum;
	}

	//转换身份证前17位为int数组
	private static int[] splitId(String id) {
		char[] chars = id.toCharArray();
		int[] result = new int[chars.length-1];
		for (int i = 0; i < result.length; i++) {
			result[i] = chars[i] - CHAR_TO_INT;
		}
		return result;
	}

	//判断字符中是否为空
	private static boolean isEmpty(String str){
		return str == null || "".equals(str);
	}

}