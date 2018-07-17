package com.youcai.manage.vo.pricelist;

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

    private BigDecimal price;

    private String note;
}
