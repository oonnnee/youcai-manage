package com.youcai.manage.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum  ResultEnum {

    SUCCESS(0, "成功"),
    NO_LOGIN(1, "未登录"),
    ERROR(2, "错误");

    private Integer code;

    private String note;

}
