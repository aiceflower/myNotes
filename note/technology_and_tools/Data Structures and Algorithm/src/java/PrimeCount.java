import java.util.Arrays;

public class PrimeCount{
    /**
     * 寻找 n 以内素数的个数
     * @param args
     */
    public static void main(String[] args){
        System.out.println(findPrimeCount(100));
        System.out.println(eishai(1000));
    }

    /**
     * 暴力搜索
     */
    private static int findPrimeCount(int n) {
        int count = 0;
        for(int i = 2; i <= n; i++){
            if (isPrime(i)) count++;
        }
        return count;
    }

    /**
     * 埃筛法，从2开始标记，所有与2相乘的数为合数 2 * i ，再 从 n 开始
     * @param n
     * @return
     */
    private static int eishai(int n){
        int count = 0;
        boolean[] primes = new boolean[n];
        for(int i = 2; i < n; i++){
            if (!primes[i]){
                count++;
                //为什么不从2开始，2-i 之前的已经标记了
                for(int j = i * i; j < n; j += i){//j += i 相当于 2 * i  3 * i = (2 * i) + i
                    primes[j] = true;
                }
            }
        }
        return count;
    }

    private static boolean isPrime(int n){
        if (n < 2){
            return true;
        }
        for(int i = 2; i * i <= n; i++){
            if (n % i == 0){
                return false;
            }
        }
        return true;
    }
}