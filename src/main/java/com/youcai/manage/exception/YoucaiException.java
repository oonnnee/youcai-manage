package com.youcai.manage.exception;

import com.youcai.manage.enums.ResultEnum;
import lombok.Getter;

@Getter
public class YoucaiException extends RuntimeException {

    private Integer code;

    public YoucaiException(ResultEnum resultEnum) {
        super(resultEnum.getMsg());
        this.code = resultEnum.getCode();
    }

    public YoucaiException(Integer code, String msg){
        super(msg);
        this.code = code;
    }
}
