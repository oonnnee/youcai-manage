package com.youcai.manage.enums;

import lombok.Getter;

@Getter
public enum  ResultEnum {

    MANAGE_NO_LOGIN(1, "未登录"),
    MANAGE_LOGIN_ERROR(2, "登录失败, 用户名或密码错误"),
    MANAGE_GUEST_SAVE_ERROR(3, "新增客户，保存时失败"),
    MANAGE_GUEST_UPDATE_ERROR(4, "更新客户，更新时失败"),
    MANAGE_PRODUCT_SAVE_ERROR(5, "新增产品，保存时失败"),
    MANAGE_PRODUCT_UPDATE_ERROR(6, "更新产品，更新时失败"),
    MANAGE_PRICELIST_SAVE_PRICES_PARSE_ERROR(7, "新增报价单，报价解析错误"),
    MANAGE_DRIVER_SAVE_ERROR(8, "新增司机，保存时失败"),
    MANAGE_DRIVER_UPDATE_NULL_ID(9, "更新司机信息，司机id为空"),
    MANAGE_DRIVER_UPDATE_ERROR(10, "更新司机信息，更新时失败"),
    MANAGE_UPLOAD_IMAGE_EMPTY(11, "上传图片，图片为空");

    private Integer code;

    private String msg;

    ResultEnum() { }

    ResultEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
