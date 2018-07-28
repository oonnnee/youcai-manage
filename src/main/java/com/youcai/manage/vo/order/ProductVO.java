package com.youcai.manage.vo.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductVO {
    private String id;
    private String name;
    private String category;
    private String unit;
    private BigDecimal price;
    private BigDecimal num;
    private BigDecimal amount;
    private String imgfile;
    private String note;
}
