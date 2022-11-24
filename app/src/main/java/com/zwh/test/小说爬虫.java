package com.zwh.test;

import static java.util.regex.Pattern.compile;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
//import org.testng.annotations.Test;

public class 小说爬虫 implements Runnable {

    //    private static String record_file = "D:\\\\text";
    private static String record_file = "D:\\\\text\\\\大乘期才有修仙系统";
    private static String record_file_zhangShu_url = "";

    FileWriter fw = null;
    //    String url = "https://www.ctgf.net";
//    String url = "http://www.biquge.tv";
//    String url = "https://www.bbiquge.net/book/115619/";
    String url = "https://www.biquke.vip";
//    String selectUrl = "https://www.bbiquge.net/book/12684/index_16.html"; // 从红月
//    String selectUrl = "https://www.bbiquge.net/book/132737/"; // 朕
//    String selectUrl = "https://www.bbiquge.net/book/115619/index_7.html"; // 黎明之剑
//    String selectUrl = "https://www.biquke.vip/book/2108"; // 黎明之剑
    String selectUrl = "https://www.biquke.vip/book/12588"; // 大乘期才有修仙系统
//    String selectUrl = "https://www.1biqug.cc/66/66591/"; // 机武风暴

    String fileName = ""; //小说分类
    String fileName2 = "";//小说名
    String fileName_ZhangShu = ""; //小说第几章
    String fileName_ZhangShu_NeiRong = "";  //小说内容


//    @Test

//    public void jsoup() throws Exception {
//        /** 优势：由于Document是java中自带的解析器，兼容性强
//         缺点：由于Document是一次性加载文档信息，如果文档太大，加载耗时长，不太适用**/
//        //解析url地址，                                                                  笔趣阁，玄幻魔法
//        Document doc___0 = Jsoup.parse(new URL(url), 10000); // 解析网页 得到文档对象
////    	Document doc___3 =  Jsoup.parse(new URL(url+e___2.attr("href")).openStream(), "GBK", url+e___2.attr("href"));
//
//        //使用标签选择器
////    	String title = document.getElementsByTag("title").first().text();
////    	System.out.println("-------title------"+title);
///****************创建----首页-----目录*******************/
//        /***使用 属性，属性值 -----》  获取值***/
////    	<li><a href="/magic/">玄幻魔法</a></li>   -----属性，属性值
////		  String title3 = document.getElementsByAttributeValue("href","/magic/").first().text();
////        System.out.println("-------title3------" + title3);
////    	  System.out.println( "target-_blank" );
//        // 根据属性名和属性值来查询DOM
//        //通过选择器查找所有博客链接DOM
//        Elements targetElements = doc___0.select("#wrapper .nav a");// id选择器下的--》class选择器---》a标签
//        for (Element e___0 : targetElements) {
//            fileName = e___0.text();  // //小说分类
////           	System.out.println("------------"+fileName);
//            if ("首页".equals(fileName) || "榜单".equals(fileName)
//                    || "全部小说".equals(fileName) || "搜索".equals(fileName)
//                    || "书架".equals(fileName)) {
//                //跳出循环，循环下一个
//                continue;
//            }
//            /*创建文件夹*/  /****************创建目录*******************/
////           		System.out.println("------2222222------"+fileName);
//            File file = new File(record_file + File.separator + File.separator + fileName);
//            if (!file.exists()) {//如果文件夹不存在
//                file.mkdir();//创建文件夹
//            }
//            /* 给文件夹写入数据，*/
//
////	        	 System.out.println("---获取-----href--的值--："+e.attr("href"));
////	        	 System.out.println("---url--："+url+e___0.attr("href"));
//            Document doc___1 = Jsoup.parse(new URL(url + e___0.attr("href")), 10000); // 解析网页 得到文档对象
//            Elements Elements_log = doc___1.select("#newscontent .l ul li .s2 a");// id选择器下的--》class选择器---》a标签
//            for (Element e___1 : Elements_log) {
//                fileName2 = "";  //小说名
//                fileName2 = e___1.text().toString();
//                System.out.println("小说名：" + fileName2);
//                if (!fileName2.equals("从红月开始")) continue;
////	                	System.out.println("--------fileName2-----小说名-----"+fileName2);
//                System.out.println("----111----" + record_file + File.separator + File.separator
//                        + fileName + File.separator + File.separator
//                        + fileName2);
//                File file2 = new File(record_file + File.separator + fileName + File.separator + fileName2);
//                if (!file2.exists()) {//如果文件夹不存在
//                    file2.mkdir();//创建文件夹
//                }
//
////                downLoad(e___1);
//            }
//
//        }
//
//    }


    public void downLoad() throws Exception {
//        Document doc___2 = Jsoup.parse(new URL(e___1.attr("href")), 10000);
        Document doc___2 = Jsoup.parse(new URL(selectUrl), 10000);
//        Elements Elements_DiJiZhang = doc___2.select("#list dl dd a");// id选择器下的--》class选择器---》a标签
        Elements Elements_DiJiZhang = doc___2.select("dd");// id选择器下的--》class选择器---》a标签
        for (Element e___2 : Elements_DiJiZhang) {
            fileName_ZhangShu = "";  //小说第几章
            fileName_ZhangShu = e___2.text().toString().replace("?", "？");
            record_file_zhangShu_url = "";
            record_file_zhangShu_url = (record_file + File.separator + File.separator
                    + fileName + File.separator + File.separator + fileName2
                    + File.separator + File.separator + fileName_ZhangShu + ".txt").toString();//.replace("\\","/")
            File file3 = new File(record_file_zhangShu_url);
            ///如果这张内容不存在，就创建文件夹，并写入内容 ，
            ///-------否则，就不创建跳出循环，循环下一个

//            System.out.println("-------fileName_ZhangShu-----------" + fileName_ZhangShu);

            if (!file3.exists()) {
                file3.createNewFile();

                System.out.println("-------fileName_ZhangShu-----------" + fileName_ZhangShu);
                //添加--小说--张数--标题
                fw = new FileWriter(record_file_zhangShu_url, true);
                BufferedWriter buWriter = new BufferedWriter(fw);
                buWriter.write(fileName_ZhangShu);
                buWriter.newLine();//换行

                String href = getHref(e___2.toString());
//                Document doc___3 = Jsoup.parse(new URL(url + e___2.attr("href")), 10000);
//                String url = doc.select("a.sushi-restaurant").first().attr("abs:href");
                Document doc___3 = Jsoup.parse(new URL(url + href), 10000);
                Elements Elements_DiJiZhang_NeiRong = doc___3.select("#content");// id选择器下的--》class选择器---》a标签
                for (Element e___3 : Elements_DiJiZhang_NeiRong) {
//				                		  buWriter.write("\r\n\r\n");   //换行
//				                		  buWriter.newLine();//换行
                    fileName_ZhangShu_NeiRong = ""; //小说内容
                    //换行，一个 ‘，’换一次行
                    fileName_ZhangShu_NeiRong = e___3.text().toString().replace("。”", "。”" + "\r\n");
//				                	 	  fileName_ZhangShu_NeiRong= e___3.text().toString();
//				                	 	  System.out.println("-----------fileName_ZhangShu_NeiRong------------"+fileName_ZhangShu_NeiRong);

                    buWriter.write(fileName_ZhangShu_NeiRong);

                }
                buWriter.close();
                fw.close();

            } else {  //如果这张内容存在,就跳出循环，循环下一个
                continue;
            }
        }
    }

    public String getHref(String content) {
        Pattern pattern_a = compile("<a[^>]*href=(\\\"([^\\\"]*)\\\"|\\'([^\\']*)\\'|([^\\\\s>]*))[^>]*>(.*?)</a>");
        Matcher matcher_a = pattern_a.matcher(content);
        matcher_a.find(2);
        return matcher_a.group(2);
//        System.out.println("网站连接");
//        while (matcher_a.find()) {
//            for (int i = 0; i < matcher_a.groupCount(); i++) {
//                System.out.println("网址："+matcher_a.group(2));
//            }
//        }
    }


    public void run() {
        try {
//            jsoup();
            File file = new File(record_file);
            if (!file.exists()){
                file.mkdirs();
            }
            downLoad();
        } catch (Exception e) {
            System.out.println(e.toString());
//            try {
//                Thread.sleep(3000);
//            } catch (InterruptedException interruptedException) {
//                System.out.println("线程关闭");
//            }
//            run();
        }
        System.out.println("结束");

    }

}