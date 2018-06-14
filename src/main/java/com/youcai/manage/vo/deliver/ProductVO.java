package com.youcai.manage.vo.deliver;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductVO {
    private String id;
    private String name;
    private String unit;
    private BigDecimal price;
    private BigDecimal num;
    private BigDecimal amount;
    private String note;
}
