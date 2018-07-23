package com.youcai.manage.utils;

import com.youcai.manage.enums.ResultEnum;
import com.youcai.manage.vo.ResultVO;
import org.springframework.util.CollectionUtils;

import java.util.Collection;

public class ResultVOUtils {

    public static ResultVO success(Object data){
        return new ResultVO(0,"成功", data);
    }

    public static ResultVO successString(String data){
        return new ResultVO(0,"成功", data);
    }

    public static ResultVO success(String msg){
        return new ResultVO(0, msg, null);
    }

    public static ResultVO success(){
        return new ResultVO(0, "成功", null);
    }

    public static ResultVO success(Object object, String msg){
        return object == null ?
                ResultVOUtils.success(msg) : ResultVOUtils.success(object);
    }

    public static ResultVO success(Collection collection, String msg){
        return CollectionUtils.isEmpty(collection) ?
                ResultVOUtils.success(msg) : ResultVOUtils.success(collection);
    }

    public static ResultVO error(String msg){
        return new ResultVO(ResultEnum.ERROR.getCode(), msg, null);
    }

    public static ResultVO error(Integer code, String msg){
        return new ResultVO(code, msg, null);
    }

}
