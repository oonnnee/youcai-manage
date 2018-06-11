package com.youcai.manage.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PricelistDTO {

    private String productId;

    private String productName;

    /*--- 产品大类编码 ---*/
    private String productCode;

    private String productUnit;

    private String productImg;

    private BigDecimal price;

    private String note;
}
