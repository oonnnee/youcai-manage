package com.youcai.manage.excel.stat;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Year {
    @Excel(name = "序号", type = 10)
    private Integer id;

    private String guestId;

    @Excel(name = "客户名称", width = 50)
    private String name;

    @Excel(name = "1月份", type = 10)
    private BigDecimal month1;
    @Excel(name = "2月份", type = 10)
    private BigDecimal month2;
    @Excel(name = "3月份", type = 10)
    private BigDecimal month3;
    @Excel(name = "4月份", type = 10)
    private BigDecimal month4;
    @Excel(name = "5月份", type = 10)
    private BigDecimal month5;
    @Excel(name = "6月份", type = 10)
    private BigDecimal month6;
    @Excel(name = "7月份", type = 10)
    private BigDecimal month7;
    @Excel(name = "8月份", type = 10)
    private BigDecimal month8;
    @Excel(name = "9月份", type = 10)
    private BigDecimal month9;
    @Excel(name = "10月份", type = 10)
    private BigDecimal month10;
    @Excel(name = "11月份", type = 10)
    private BigDecimal month11;
    @Excel(name = "12月份", type = 10)
    private BigDecimal month12;

    @Excel(name = "合计", type = 10, width = 12)
    private BigDecimal sum;
}
