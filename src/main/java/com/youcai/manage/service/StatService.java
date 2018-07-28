package com.youcai.manage.service;

import com.youcai.manage.excel.stat.Month;
import com.youcai.manage.excel.stat.Quarter;
import com.youcai.manage.excel.stat.Week;
import com.youcai.manage.excel.stat.Year;
import com.youcai.manage.vo.stat.RangeVO;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;

public interface StatService {

    List<RangeVO> range(Date startDate, Date endDate);

    List<Year> year() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException;
    List<Quarter> quarter() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException;
    List<Month> month() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException;
    List<Week> week() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException;
}
