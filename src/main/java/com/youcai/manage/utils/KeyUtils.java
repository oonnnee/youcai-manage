package com.youcai.manage.utils;

/**
 * 生成主键工具类
 */
public class KeyUtils {

    /**
     * @return 4位随机数+当前时间毫秒数后6位
     */
    public static synchronized String generate() {

        String time = String.valueOf(System.currentTimeMillis()).substring(7, 13);
        String random = String.valueOf((int) (Math.random()*9000)+1000);

        return random+time;
    }

    public static void main(String[] args ){
        System.out.println(generate());
    }
}
