package com.youcai.manage.vo;

import lombok.Data;

@Data
public class ResultVO<T> {

    /*--- 状态码 ---*/
    private Integer code;

    /*--- 描述信息 ---*/
    private String msg;

    /*--- 返回的数据 ---*/
    private T data;

    public ResultVO() { }

    public ResultVO(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
}
