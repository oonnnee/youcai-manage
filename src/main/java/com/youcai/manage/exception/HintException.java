package com.youcai.manage.exception;

import lombok.Getter;

@Getter
public class HintException extends RuntimeException {

    public HintException(String msg){
        super(msg);
    }
}
