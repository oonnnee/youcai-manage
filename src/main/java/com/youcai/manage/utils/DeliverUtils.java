package com.youcai.manage.utils;


import com.youcai.manage.enums.DeliverEnum;

public class DeliverUtils {

    private static String randomString(){
        return UUIDUtils.randomUUID().substring(0, 13);
    }

    public static String getStateDelivering(){
        return DeliverEnum.DELIVERING.getState();
    }

    public static String getStateReceive(){
        return DeliverEnum.RECEIVE.getState();
    }

}
