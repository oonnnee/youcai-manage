package com.youcai.manage.controller.nonrest;

import com.youcai.manage.excel.stat.Month;
import com.youcai.manage.excel.stat.Quarter;
import com.youcai.manage.excel.stat.Week;
import com.youcai.manage.excel.stat.Year;
import com.youcai.manage.service.StatService;
import com.youcai.manage.utils.PoiUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/stat/export")
public class StatController {

    @Autowired
    private StatService statService;

    @GetMapping("/year")
    public void year(HttpServletResponse response) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        List<Year> years = statService.year();

        int currentYear = LocalDate.now().getYear();

        String title = currentYear + "年广东优菜农业发展有限公司销售数据统计表（单位：万元）";
        String filename = currentYear + "年销售数据统计表.xls";

        PoiUtils.exportExcel(years, title,"sheet1", Year.class, filename, response);
    }
    @GetMapping("/quarter")
    public void quarter(HttpServletResponse response) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        List<Quarter> quarters = statService.quarter();

        int currentYear = LocalDate.now().getYear();
        int currentQuarter = (LocalDate.now().getMonthValue()-1)/3 + 1;

        String title = currentYear+ "年第"+currentQuarter+"季度广东优菜农业发展有限公司销售数据统计表（单位：万元）";
        String filename = currentYear+ "年第"+currentQuarter+"季度销售数据统计表.xls";

        PoiUtils.exportExcel(quarters, title,"sheet1", Quarter.class, filename, response);
    }
    @GetMapping("/month")
    public void month(HttpServletResponse response) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        List<Month> months = statService.month();

        int currentYear = LocalDate.now().getYear();
        int currentMonth = LocalDate.now().getMonthValue();

        String title = currentYear+ "年第"+currentMonth+"月份广东优菜农业发展有限公司销售数据统计表（单位：万元）";
        String filename = currentYear+ "年第"+currentMonth+"月份销售数据统计表.xls";

        PoiUtils.exportExcel(months, title,"sheet1", Month.class, filename, response);
    }
    @GetMapping("/week")
    public void week(HttpServletResponse response) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        List<Week> weeks = statService.week();

        int currentYear = LocalDate.now().getYear();
        int currentMonth = LocalDate.now().getMonthValue();

        String title = currentYear+ "年第"+currentMonth+"月份广东优菜农业发展有限公司销售数据周报（单位：万元）";
        String filename = currentYear+ "年第"+currentMonth+"月份销售数据周报.xls";

        PoiUtils.exportExcel(weeks, title,"sheet1", Week.class, filename, response);
    }
}
