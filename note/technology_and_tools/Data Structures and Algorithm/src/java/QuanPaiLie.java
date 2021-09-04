import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class QuanPaiLie{
    /**
     * 题目描述：
     * @param args
     */
    public static void main(String[] args){
        int[] nums = new int[]{1,2,3};
        List<Integer> item = new LinkedList<>();
        List<List<Integer>> res = new LinkedList<>();
        generator(0, nums, item, res);
        System.out.println(res.size());
    }

    private static void generator(int i, int[] nums, List<Integer> item, List<List<Integer>> res) {
        if (i >= nums.length){
            return;
        }
        item.add(nums[i]);
        res.add(item);
        generator(i+1, nums, item, res);
        item.remove(i);
        generator(i+1, nums, item, res);
    }

    
}