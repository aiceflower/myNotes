import java.util.HashMap;
import java.util.Map;

import util.TreeNode;

public class Rob{
    /**
     * 题目描述：一个小偷，在晚上抢劫整条街道上的人，同一天晚上如果相邻的两家被盗会报警，问小偷一晚上最多能偷到多少
     * {1,2,3,1} 4
     * {2,7,9.3.1} 12
     * @param args
     */
    public static void main(String[] args){
        int[] data = new int[]{1,2,3,1};
        int[] data1 = new int[]{2,7,9,3,1};

        //递归
        System.out.println(maxMoney(data, data.length - 1));
        System.out.println(maxMoney1(data1, data1.length - 1));

        //动态规划
        System.out.println(getMoney2(data));
        System.out.println(getMoney2(data1));

        //一圈
        System.out.println(getMoney3(data));
        System.out.println(getMoney3(data1));
        System.out.println("end.");

        //一棵树
        getMoney4();
    }

    /**
     * 递归：当前节点可以取到的最大值
     * @param data 数据
     * @param index 当前节点下村
     * @return
     */
    public static int maxMoney(int[] data, int index){
        if (index < 0){
            return 0;
        }
        if (index == 0){
            return data[0];
        }
        //当前最大有两种情况，取当前，则还需要取 index - 2, 不取当前只能取 index - 1
        return Math.max(maxMoney(data, index - 1), maxMoney(data, index - 2) + data[index]);
    }

    private static Map<Integer, Integer> cache = new HashMap<>();
    /**
     * 递归 + 备忘录
     * @param data
     * @param index
     * @return
     */
    public static int maxMoney1(int[] data, int index){
        if (index < 0){
            return 0;
        }
        if (index == 0){
            return data[0];
        }
        if (cache.containsKey(index)){
            return cache.get(index);
        }
        int max = Math.max(maxMoney(data, index - 1), maxMoney(data, index - 2) + data[index]);
        cache.put(index, max);
        return max;
    }

    /**
     * 动态规划
     */
    public static int getMoney2(int[] data){
        if (data == null) return 0;
        int len = data.length;
        if (len == 1) return data[0];
        if (len == 2) return Math.max(data[0], data[1]);
        
        //int[] dp = new int [len];
        //只与前两个相关，可以压缩
        int pre = data[0];
        int next = Math.max(data[0], data[1]);
        for(int i = 2; i < len; i++){
            int max = Math.max(pre + data[i], next);
            pre = next;
            next = max;
        }
        return next;
    }

    /**
     * 升级版本：打劫一圈房屋，即首尾相连接，同时取首尾则会报警
     * 思路：还是之前的求解方法，取不要首，或不要尾的大者
     */
    
     public static int getMoney3(int [] data){
         if (data == null){
             return 0;
         }

        return Math.max(di(data, 0, data.length - 2), di(data, 1, data.length - 1)); 
     }
     public static int di(int[] data, int start, int end){
        //长度判断 TODO
        int pre = data[start];
        int next = Math.max(data[start], data[start + 1]);
        for(int i = start + 2; i < end; i++){
            int max = Math.max(pre + data[i], next);
            pre = next;
            next = max;
        }
        return next;
     }

     //再次升级，房屋只有一个入口，类似树的结构,且一个房子只与一个房子相连接，相连的在同一天被抢会报警
     public static void getMoney4(){
         TreeNode t5 = new TreeNode(1, null, null);
         TreeNode t4 = new TreeNode(3, null, null);
         TreeNode t3 = new TreeNode(3, null, t5);
         TreeNode t2 = new TreeNode(2, null, t4);
         TreeNode t1 = new TreeNode(3, t2, t3);
         int[] r = dfs(t1);
         System.out.println(Math.max(r[0], r[1]));
     }
     public static int[] dfs(TreeNode root){
         if (root == null){
             return new int[]{0, 0};
         }
         int[] res = new int[2];
         //0-选择当前， 1-不选择
         int[] l = dfs(root.left);
         int[] r = dfs(root.right);
         res[0] = l[1] +  r[1] + root.val;
         res[1] = Math.max(l[0], l[1]) + Math.max(r[0], r[1]);
         return res;
     }
}