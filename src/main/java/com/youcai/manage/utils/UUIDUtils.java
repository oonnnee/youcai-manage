package com.youcai.manage.utils;

import java.util.UUID;

public class UUIDUtils {

    public static String randomUUID() {
        return UUID.randomUUID().toString();
    }

    public static void main(String[] args){
        System.out.println(randomUUID().substring(0, 13));
    }
}