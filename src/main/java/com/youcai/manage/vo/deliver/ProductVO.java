package com.youcai.manage.vo.deliver;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductVO {
    private String id;
    private String name;
    private String unit;
    private BigDecimal price;
    private BigDecimal num;
    private BigDecimal amount;
    private String note;
}
