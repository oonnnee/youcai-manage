package com.youcai.manage.utils;

import com.youcai.manage.exception.HintException;
import com.youcai.manage.exception.ManageException;

public class ManageUtils {
    public static void ManageException(Object object, String msg){
        if (object == null){
            throw new ManageException(msg);
        }
    }
    public static void ManageException(boolean b, String msg){
        if (b){
            throw new ManageException(msg);
        }
    }

    public static void HintException(Object object, String msg){
        if (object == null){
            throw new HintException(msg);
        }
    }
}
