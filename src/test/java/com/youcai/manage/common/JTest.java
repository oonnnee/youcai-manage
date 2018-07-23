package com.youcai.manage.common;

import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


public class JTest {

    @Test
    public void test() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = dateFormat.parse("2017-01-01");
        Date date2 = dateFormat.parse("2017-01-01");
        System.out.println(date1.getTime() == date2.getTime());
    }
}
