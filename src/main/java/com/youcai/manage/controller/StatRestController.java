package com.youcai.manage.controller;

import com.youcai.manage.excel.stat.Month;
import com.youcai.manage.excel.stat.Quarter;
import com.youcai.manage.excel.stat.Week;
import com.youcai.manage.excel.stat.Year;
import com.youcai.manage.service.StatService;
import com.youcai.manage.utils.ManageUtils;
import com.youcai.manage.utils.ResultVOUtils;
import com.youcai.manage.vo.ResultVO;
import com.youcai.manage.vo.stat.RangeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/stat")
public class StatRestController {

    @Autowired
    private StatService statService;

    @GetMapping("/range")
    public ResultVO range(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate
    ){
        List<RangeVO> rangeVOS = statService.range(startDate, endDate);

        return ResultVOUtils.success(rangeVOS);
    }

    @GetMapping("/year")
    public ResultVO year() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        List<Year> years = statService.year();

        return ResultVOUtils.success(years);
    }
    @GetMapping("/quarter")
    public ResultVO quarter() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        List<Quarter> quarters = statService.quarter();

        return ResultVOUtils.success(quarters);
    }
    @GetMapping("/month")
    public ResultVO month() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        List<Month> months = statService.month();

        return ResultVOUtils.success(months);
    }
    @GetMapping("/week")
    public ResultVO week() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        List<Week> weeks = statService.week();

        return ResultVOUtils.success(weeks);
    }
}
