package com.youcai.manage.dto.pricelist;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductDTO {
    private String id;
    private BigDecimal price;
    private String note;
}
