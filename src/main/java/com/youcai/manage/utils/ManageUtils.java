package com.youcai.manage.utils;

import com.youcai.manage.exception.HintException;
import com.youcai.manage.exception.ManageException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.util.CollectionUtils;

import java.util.Collection;

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
    public static void ManageException(Collection collection, String msg){
        if(CollectionUtils.isEmpty(collection)){
            throw new ManageException(msg);
        }
    }

    public static void HintException(Object object, String msg){
        if (object == null){
            throw new HintException(msg);
        }
    }
    public static void HintException(Collection collection, String msg){
        if(CollectionUtils.isEmpty(collection)){
            throw new HintException(msg);
        }
    }

    public static Pageable getPageable(Integer page, Integer size){
        page = page<0 ? 0:page;
        size = size<=0 ? 10:size;

        Pageable pageable = new PageRequest(page, size);

        return pageable;
    }

    public static String toErrorString(String error, String cause){
        return error+"\n原因："+cause;
    }
}
