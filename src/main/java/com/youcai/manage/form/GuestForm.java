package com.youcai.manage.form;

import lombok.Data;

@Data
public class GuestForm {

    /*--- 客户密码 ---*/
    private String pwd;

    private String name;

    private String addr;

    private String phone;

    /*--- 客户负责人1手机号 ---*/
    private String mobile1;

    /*--- 客户负责人1姓名 ---*/
    private String leader1;

    /*--- 客户负责人2手机号 ---*/
    private String mobile2;

    /*--- 客户负责人2姓名 ---*/
    private String leader2;

    private String note;


}
