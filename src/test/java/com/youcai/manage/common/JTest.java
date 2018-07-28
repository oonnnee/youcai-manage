package com.youcai.manage.common;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


public class JTest {

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Test
    public void test() throws ParseException {
        int dayOfWeek = LocalDate.now().getDayOfWeek().getValue();
        int dayOfMonth = LocalDate.now().getDayOfMonth();

        Date startDate = DateUtils.addDays(new Date(), -(dayOfWeek-1));

        System.out.println(dateFormat.format(DateUtils.addDays(startDate, 0)));
    }
}
