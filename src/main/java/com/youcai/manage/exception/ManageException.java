package com.youcai.manage.exception;

import com.youcai.manage.enums.ResultEnum;
import lombok.Getter;

@Getter
public class ManageException extends RuntimeException {

    private Integer code;

    public ManageException(ResultEnum resultEnum) {
        super(resultEnum.getMsg());
        this.code = resultEnum.getCode();
    }

    public ManageException(Integer code, String msg){
        super(msg);
        this.code = code;
    }
}
