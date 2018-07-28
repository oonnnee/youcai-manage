package com.youcai.manage.excel.stat;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Month {
    @Excel(name = "序号", type = 10)
    private Integer id;

    private String guestId;

    @Excel(name = "客户名称", width = 50)
    private String name;

    @Excel(name = "第1周", type = 10)
    private BigDecimal week1;
    @Excel(name = "第2周", type = 10)
    private BigDecimal week2;
    @Excel(name = "第3周", type = 10)
    private BigDecimal week3;
    @Excel(name = "第4周", type = 10)
    private BigDecimal week4;
    @Excel(name = "第5周", type = 10)
    private BigDecimal week5;

    @Excel(name = "合计", type = 10, width = 12)
    private BigDecimal sum;
}
