package com.zwh.leetcode;

import java.util.ArrayList;

public class 统计数组中峰和谷的数量_2210 {
    public static void main(String[] args) {
//        System.out.println(countHillValley1(nums));
    }

    /**
     * 执行用时: 1 ms
     * 内存消耗: 39.9 MB
     */
    public static int countHillValley(int[] nums) {
        int count = 0;
        int len = nums.length;
        ArrayList<Integer> list = new ArrayList<>();
        list.add(nums[0]);
        for (int i = 1; i < len; i++) {
            if (nums[i] != nums[i - 1]) {
                list.add(nums[i]);
            }
        }
        int size = list.size();
        for (int i = 1; i < size - 1; i++) {
            int last = list.get(i - 1);
            int cur = list.get(i);
            if (cur < last) {
                int next = list.get(i + 1);
                if (cur < next) {
                    count++;
                }
            }
            if (cur > last) {
                int next = list.get(i + 1);
                if (cur > next) {
                    count++;
                }
            }

        }
        return count;
    }

    //这题关键是相邻的数可能是相等的，这样需要跨越几个值才能判断出峰谷
//所以我的想法是记录下之前的状态，是上升还是下降，其中相等不改变状态
//遍历数组，当发现值变大/变小后，和之前的状态比较，不一致代表刚刚经过了谷/峰
//
//执行用时： 0 ms , 在所有 Java 提交中击败了 100.00% 的用户
// 内存消耗： 39.4 MB , 在所有 Java 提交中击败了 62.62% 的用户
//  hint 此方法 只能统计峰谷个数，无法具体峰谷位置
    public static int countHillValley1(int[] nums) {
        int statue = 0, n = 0;//状态和计数
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 1; i < nums.length; i++) {//遍历一遍数组
            if (nums[i - 1] < nums[i]) {    //现在上升
                if (statue == -1) {     //之前下降
                    ++n;        //是谷
                    list.add(nums[i]);
                }
                statue = 1;       //更改之前状态
            } else if (nums[i - 1] > nums[i]) { //现在下降
                if (statue == 1) {  //之前上升
                    ++n;          //是峰
                    list.add(nums[i]);
                }
                statue = -1;        //更改之前状态
            }
        }
        System.out.println(list.toString());
        return n;
    }


    /**
     * 执行用时：0 ms,在所有 Java 提交中击败了100.00%的用户内存消耗：39.5 MB,
     * -------------在所有 Java 提交中击败了51.04%的用户
     * <p>
     * hint 与countHillValley1方法相比能定位峰谷位置
     *
     * @return
     */
    public static ArrayList<Double> countHillValley2(double[] nums) {
        long sta = System.currentTimeMillis();
        int res = 0;   // 峰与谷的数量
        int n = nums.length;
        ArrayList<Double> list = new ArrayList<>();
        for (int i = 1; i < n - 1; ++i) {
            if (nums[i] == nums[i - 1]) {
                // 去重
                continue;
            }
            int left = 0;   // 左边可能的不相等邻居对应状态
            for (int j = i - 1; j >= 0; --j) {
                if (nums[j] > nums[i]) {
                    left = 1;
                    break;
                } else if (nums[j] < nums[i]) {
                    left = -1;
                    break;
                }
            }
            int right = 0;   // 右边可能的不相等邻居对应状态
            for (int j = i + 1; j < n; ++j) {
                if (nums[j] > nums[i]) {
                    right = 1;
                    break;
                } else if (nums[j] < nums[i]) {
                    right = -1;
                    break;
                }
            }
            if (left == right && left != 0) {
                // 此时下标 i 为峰或谷的一部分
                ++res;
                list.add(nums[i]);
            }
        }
        System.out.println("countHillValley2执行耗时:" + (System.currentTimeMillis() - sta));
        return list;
    }
}
