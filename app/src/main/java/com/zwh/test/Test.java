package com.zwh.test;

import java.util.ArrayList;

public class Test {
    public static void main(String[] args) {
//        System.out.println("6e400001b5a3f393e0a9e50e24dc".length());
//        int[] nums = new int[]{2, 2, 1};
//        System.out.println(singleNumber(nums));
        new 小说爬虫().run();
//        testList();
    }
    public static int singleNumber(int[] nums) {
        int ans = 0;
        for (int num : nums) {
            ans ^= num;
        }

        return ans;
    }

    public static void testList(){
        A a = new A();
        a.setText("aaaa");

        ArrayList<A> lista = new ArrayList<>();
        lista.add(a);
        a=null;
        System.out.println(lista.get(0).text);

//        ArrayList<A> listb = new ArrayList<>();
//        listb.addAll(lista);
//
//        lista.clear();
//        System.out.println(listb.get(0).text);
    }

    static class A{
        String text;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }


}
