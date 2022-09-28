package com.zwh.leetcode.easy;

import java.util.Arrays;
import java.util.HashMap;

public class 多数元素_169 {
    public static void main(String[] args) {
        int[] nums = new int[]{1,2,2,2,2,2,2,2,2,2,2,3, 2, 3,4,5,6};
//        System.out.println(majorityElement(nums));
        System.out.println(majorityElement1(nums));
    }

    /**
     * 执行用时: 13 ms
     * 内存消耗: 46.4 MB
     *
     * hint nnd刷了十几题了还是只会笨方法，啥时候能开窍
     */
    public static int majorityElement(int[] nums) {
        if (nums.length == 1) return nums[0];
        HashMap<Integer, Integer> map = new HashMap<>();

        for (int num : nums) {
            boolean b = map.containsKey(num);
            if (b) {
                int i = map.get(num) + 1;
                map.put(num, i);
                if (i > nums.length / 2) return num;
            } else {
                map.put(num, 1);
            }
        }
        return 0;
    }

    /**
     *
     */
    public static int majorityElement1(int[] nums) {
        Arrays.sort(nums);
        return nums[nums.length / 2];
    }

    /**
     * 摩尔投票法
     */
    public int majorityElement2(int[] nums) {
        int count = 0;
        Integer candidate = null;

        for (int num : nums) {
            if (count == 0) {
                candidate = num;
            }
            count += (num == candidate) ? 1 : -1;
        }

        return candidate;
    }
}
