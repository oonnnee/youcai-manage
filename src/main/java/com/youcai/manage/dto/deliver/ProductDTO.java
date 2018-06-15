package com.youcai.manage.dto.deliver;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductDTO {
    private String id;
    private BigDecimal price;
    private BigDecimal num;
    private String note;
}
