package com.youcai.manage.dataobject;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

/**
 * 产品
 */
@Entity
@Data
public class Product {

    @Id
    private String id;

    private String name;

    /*--- 产品大类编码 ---*/
    private String pCode;

    /*--- 产品单位 ---*/
    private String unit;

    private BigDecimal price;

    /*--- 产品图片文件名 ---*/
    private String imgfile;

    private String note;

}
