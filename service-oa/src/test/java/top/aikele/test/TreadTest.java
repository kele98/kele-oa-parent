package top.aikele.test;


import java.util.HashMap;

public class TreadTest   {
    public static void main(String[] args) {

    }
    public int[] twoSum(int[] nums, int target) {
        HashMap<Integer,Integer> table = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            int temp = target - nums[i];
            Integer index = table.get(temp);
            if(index==null){
                table.put(nums[i],i);
            }else{
              return new int[] {index,i};
            }
        }
        return null;
    }
}
