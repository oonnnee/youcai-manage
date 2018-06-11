package com.youcai.manage.enums;

import lombok.Getter;

@Getter
public enum  ResultEnum {

    MANAGE_GUEST_SAVE_ERROR(1, "新增客户，保存时失败"),
    MANAGE_GUEST_UPDATE_ERROR(4, "更新客户，更新时失败"),

    MANAGE_PRODUCT_SAVE_ERROR(11, "新增产品，保存时失败"),
    MANAGE_PRODUCT_UPDATE_ERROR(12, "更新产品，更新时失败"),

    MANAGE_PRICELIST_SAVE_PRICES_PARSE_ERROR(13, "新增报价单，报价解析错误"),

    MANAGE_DRIVER_SAVE_ERROR(11, "新增司机，保存时失败"),
    MANAGE_DRIVER_UPDATE_NULL_ID(12, "更新司机信息，司机id为空"),
    MANAGE_DRIVER_UPDATE_ERROR(13, "更新司机信息，更新时失败"),

    MANAGE_LOGIN_ERROR(14, "登录失败, 用户名或密码错误"),
    MANAGE_NO_LOGIN(15, "未登录"),

    MANAGE_UPLOAD_IMAGE_EMPTY(16, "上传图片，图片为空");

    private Integer code;

    private String msg;

    ResultEnum() { }

    ResultEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
