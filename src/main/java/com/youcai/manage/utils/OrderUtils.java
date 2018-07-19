package com.youcai.manage.utils;

import com.youcai.manage.enums.OrderEnum;

public class OrderUtils {

    private static String randomString(){
        return UUIDUtils.randomUUID().substring(0, 13);
    }

    public static String getStateNew(){
        return OrderEnum.NEW.getState();
    }

    public static String getStateBacking(){
        return OrderEnum.BACKING.getState() + "-" + randomString();
    }

    public static String getStateBacked(){
        return OrderEnum.BACKED.getState() + "-" + randomString();
    }

    public static String getStateDelivered(){
        return OrderEnum.DELIVERED.getState();
    }

}
