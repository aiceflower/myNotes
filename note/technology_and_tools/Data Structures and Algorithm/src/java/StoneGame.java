import java.util.Arrays;
import java.util.Collections;

public class StoneGame{
    /**
     * 题目描述：给定一个非负整数数组,元素代表石子个数,两个玩家依次从数组的两端拿取石子，直到所有石子拿完游戏结束。
     * 求两个玩家的最高得分，假设两个玩家都绝顶聪明。
     * 预测赢家，石子游戏
     * 如果总和为奇数(能决出胜负), 且数组个数为偶数，则先手有必赢策略！！！
     * @param args
     */
    public static void main(String[] args){
        int[] data = {3, 9, 1, 2};
        int max = recursion(data, 0, data.length - 1);
        int sum = Arrays.stream(data).sum();
        System.out.println(sum);
        System.out.println(max > sum - max);

        //方法2
        System.out.println(recursion1(data, 0, data.length - 1));
        System.out.println(dynamicPrograming(data));
    }

    /**
     * 递归方式求解
     */
    private static int recursion(int[] data, int l, int r) {
        if (l == r){
            return data[l];
        }
        if (r - l == 1){
            return Math.max(data[l], data[r]);
        }
        //考虑我拿了之后，对手会怎么拿，对手会拿对他最有得的，故取较小值
        int left = data[l] + Math.min(recursion(data, l + 2, r ), recursion(data, l + 1, r - 1));
        int right = data[r] + Math.min(recursion(data, l + 1, r - 1), recursion(data, l, r - 2));
        return Math.max(left, right);
    }

    private static int recursion1(int[] data, int l, int r) {
        if (l == r){
            return data[l];
        }

        //我比你多的减掉你比我多的，最后就剩下我比你多的
        int left = data[l] - recursion1(data, l + 1, r ); // 我拿左边
        int right = data[r] - recursion1(data, l, r - 1); // 我拿右边
        return Math.max(left, right);//计算大的
    }
    
    //先手能否赢(我拿的减去对手拿的大于0)
    private static boolean canWin(int[] data){
        int len = data.length;
        int[] dp = new int[len];
        for(int i = 0; i < len; i++){
            dp[i] = data[i];
        }
        for(int i = len - 2; i >=0; i--){
            for(int j = i + 1; j < len; j++){
                dp[j] = Math.max(data[i] - dp[j], data[j] - dp[j - 1]);
            }
        }
        return dp[len - 1] >= 0;
    }

    //动态规划
    public static int dynamicPrograming(int[] data){
        int len = data.length;
        //dp[i][j] 代表从i...j 中先手和后手能得到的最大分
        int[][][] dp = new int[len][len][2];

        //0-first，1-second
        for(int i = 0; i < len; i++){
            //i == j 先手得分，后手0分
            dp[i][i][0] = data[i];
            dp[i][i][1] = 0;
        }
        //斜着遍历
        for(int j = 1; j <= len; j++){
            for(int i = 0; i < len - j; i++){
                int x = i;
                int y = i + j;
                System.out.print(x + "-" + y + "    "); 
                //先手
                int left = data[x] + dp[x+1][y][1];
                int right = data[y] + dp[x][y-1][1];
                if (left > right){
                    dp[x][y][0] = left;
                    dp[x][y][1] = dp[x+1][y][0];
                }else{
                    dp[x][y][0] = right;
                    dp[x][y][1] = dp[i][j-1][0];
                }
            }
            System.out.println();
        }
        
        for(int j = 0; j < len; j++){
            for(int i = 0; i < len; i++){
                System.out.print("(" + dp[j][i][0] + "," + dp[j][i][1] + ")" + "    ");
            }
            System.out.println();
        } 
        return dp[0][len-1][0] - dp[0][len - 1][1];
    }
    
}