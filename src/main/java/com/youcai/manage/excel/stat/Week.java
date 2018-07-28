package com.youcai.manage.excel.stat;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Week {
    @Excel(name = "序号", type = 10)
    private Integer id;

    private String guestId;

    @Excel(name = "客户名称", width = 50)
    private String name;

    @Excel(name = "星期一", type = 10)
    private BigDecimal day1;
    @Excel(name = "星期二", type = 10)
    private BigDecimal day2;
    @Excel(name = "星期三", type = 10)
    private BigDecimal day3;
    @Excel(name = "星期四", type = 10)
    private BigDecimal day4;
    @Excel(name = "星期五", type = 10)
    private BigDecimal day5;
    @Excel(name = "星期六", type = 10)
    private BigDecimal day6;
    @Excel(name = "星期七", type = 10)
    private BigDecimal day7;

    @Excel(name = "合计", type = 10, width = 12)
    private BigDecimal sum;
}
