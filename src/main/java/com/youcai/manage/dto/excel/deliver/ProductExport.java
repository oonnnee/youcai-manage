package com.youcai.manage.dto.excel.deliver;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductExport {
    /*--- 编号 ---*/
    private Integer index;
    private String name;
    private BigDecimal num;
    private String unit;
    private BigDecimal price;
    private BigDecimal amount;
    private String note;
}
