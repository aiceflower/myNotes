import java.util.Arrays;

public class BubblyTower{
    /**
     * 题目描述：香槟塔
     * 把杯子摆成金字塔形状，一层1个，二层2个，三层3个。。。
     * 从最上面一个杯子，倒洒，酒满之后，会将多余的部分，平分流入到压在下面的两个杯子，最后流在地上。
     * 现在倒了非负整数杯后，问第 i，j 个杯子中的香槟量 (n, i, j)
     * @param args
     */
    public static void main(String[] args){
        System.out.println(getNum(5, 2, 0));
        System.out.println("end.");
    }

    public static double getNum(int pored, int row, int glass){
        //第 i，j 个杯子中香槟的量
        double[][] dp = new double[pored][pored];
        dp[0][0] = pored;
        out: for(int i = 0; i < pored; i++){
            for(int j = 0; j <= i; j++){
                double d = (dp[i][j] - 1) / 2;
                if (d > 0){
                    dp[i+1][j] += d;
                    dp[i+1][j+1] += d;
                }else{
                    break out;
                }
            }
        }
        for(int i = 0; i < pored; i++){
            System.out.println(Arrays.toString(dp[i]));
        }
        return dp[row][glass] > 1 ? 1 : dp[row][glass];
    }

    
}