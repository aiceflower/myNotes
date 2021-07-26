import java.util.LinkedList;
import java.util.Queue;

public class GroupMerge{
    /**
     * 题目描述：有 n 个城市一些彼此相连,另一些没有相连,如 a->b, b->c 则 a->c
     * 省份：一些直接或间接相连的城市, 省内没有城市与其它省的城市相连
     * 给定矩阵nxn，data[i][j] = 1 表示第 i 个城市与第 j 个城市相连，为0表示不相连
     * {{1,1,0}, {1,1,0}, {0,0,1}} data[0,1] = 1 代表第0个城市与第1个城市相连
     * 求：省份的个数
     * @param args
     */
    public static void main(String[] args){
        int[][] data1 = new int[][]{{1,1,0}, {1,1,0}, {0,0,1}};
        int[][] data2 = new int[][]{{1,0,0}, {0 ,1,0}, {0,0,1}};
        //深度优先
        System.out.println(getProvince(data1));
        System.out.println(getProvince(data2));

        //
        System.out.println(bfs(data1));
        System.out.println(bfs(data2));
    }

    
    private static int bfs(int[][] citys) {
        int province = 0;
        Queue<Integer> q = new LinkedList<>();
        int cityCount = citys.length;
        boolean[] visited = new boolean[cityCount];

        for(int i = 0; i < cityCount; i++){
            //没访问过才访问
            if (!visited[i]){
                q.offer(i);//先入队
                //处理队中元素
                while(!q.isEmpty()){
                    int k = q.poll();
                    visited[k] = true;
                    for(int j = 0; j < cityCount; j++){
                        if (!visited[j] && citys[i][j] == 1){
                            q.offer(j);
                        }
                    }
                }
                //把所有与 i 相连的找完再增加省份
                province++;
            }
        }
        return province;
    }


    public static int getProvince(int[][] citysConnected){
        int citys = citysConnected.length;
        int province = 0;
        boolean[] visited = new boolean[citys];

        for (int i = 0; i < citys; i++){
            if (!visited[i]){
                bfs(i, visited, citysConnected);
                province++;
            }
        }
        return province;
    }


    //深度优先
    private static void bfs(int i, boolean[] visited, int[][] citysConnected) {
        int citys = citysConnected.length;
        for (int j = 0; j < citys; j++){
            //判断 j 是否与 i 相连接，如果 j 已经相连接，则不需要再继续了
            if (citysConnected[i][j] == 1 && !visited[j]){
                visited[j] = true;
                bfs(j, visited, citysConnected);
            }
        }
    }
}