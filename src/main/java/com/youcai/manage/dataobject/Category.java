package com.youcai.manage.dataobject;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * 产品大类
 */
@Entity
@Data
public class Category {

    /*--- 大类编号 ---*/
    @Id
    private String code;

    private String name;

    private String note;

}
