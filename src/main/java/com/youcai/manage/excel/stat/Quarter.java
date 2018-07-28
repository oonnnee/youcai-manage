package com.youcai.manage.excel.stat;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Quarter {
    @Excel(name = "序号", type = 10)
    private Integer id;

    private String guestId;

    @Excel(name = "客户名称", width = 50)
    private String name;

    @Excel(name = "第1月", type = 10)
    private BigDecimal month1;
    @Excel(name = "第2月", type = 10)
    private BigDecimal month2;
    @Excel(name = "第3月", type = 10)
    private BigDecimal month3;

    @Excel(name = "合计", type = 10, width = 12)
    private BigDecimal sum;
}
