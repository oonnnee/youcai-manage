package com.youcai.manage.dataobject;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
public class Driver {

    @Id
    @GeneratedValue
    private Integer id;

    private String name;

    /*--- 身份证 ---*/
    private String cardid;

    private String mobile;

    private String note;

}
