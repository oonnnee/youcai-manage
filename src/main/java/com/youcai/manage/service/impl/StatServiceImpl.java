package com.youcai.manage.service.impl;

import com.youcai.manage.enums.OrderEnum;
import com.youcai.manage.excel.stat.Month;
import com.youcai.manage.excel.stat.Quarter;
import com.youcai.manage.excel.stat.Week;
import com.youcai.manage.excel.stat.Year;
import com.youcai.manage.service.GuestService;
import com.youcai.manage.service.OrderService;
import com.youcai.manage.service.StatService;
import com.youcai.manage.vo.stat.RangeVO;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StatServiceImpl implements StatService {

    @Autowired
    private OrderService orderService;
    @Autowired
    private GuestService guestService;

    @Override
    public List<RangeVO> range(Date startDate, Date endDate) {
        List<Object[]> objectss = orderService.sumByStateAndDate(OrderEnum.DELIVERED.getState(), startDate, endDate);

        Map<String, String> guestMap = guestService.findMap();

        List<RangeVO> rangeVOS = objectss.stream().map(
                objects -> new RangeVO(guestMap.get(objects[0]), ((BigDecimal) objects[1]).divide(new BigDecimal(10000)))
        ).collect(Collectors.toList());

        return rangeVOS;
    }

    @Override
    public List<Year> year() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        List<Year> years = new ArrayList<>();

        Map<String, String> guestMap = guestService.findMap();

        int id = 1;
        for (Map.Entry<String, String> entry : guestMap.entrySet()) {
            Year year = new Year();
            year.setId(id++);
            year.setGuestId(entry.getKey());
            year.setName(entry.getValue());
            year.setSum(BigDecimal.ZERO);
            years.add(year);
        }

        Year statYear = new Year();
        statYear.setId(years.size()+1);
        statYear.setName("合计");

        String state = OrderEnum.DELIVERED.getState();
        Integer endMonth = LocalDate.now().getMonthValue();
        BigDecimal sum = BigDecimal.ZERO;   // 纵向合计

        for (int i=1; i<=endMonth; i++){
            List<Object[]> objectss = orderService.sumByStateAndMonth(state, i);
            for (Object[] objects : objectss){
                String guestId = (String)objects[0];
                BigDecimal amount = ((BigDecimal)objects[1]).divide(new BigDecimal(10000));
                amount = amount.setScale(2, BigDecimal.ROUND_HALF_UP);

                sum = sum.add(amount);

                for (Year year : years){
                    if (year.getGuestId().equals(guestId)){
                        Method method = Year.class.getDeclaredMethod("setMonth" + i, BigDecimal.class);
                        method.invoke(year, amount);

                        year.setSum(year.getSum().add(amount));

                        break;
                    }
                }

            }

            if (sum.subtract(BigDecimal.ZERO)
                    .compareTo(new BigDecimal(0.001)) > 0) {
                Method method = Year.class.getDeclaredMethod("setMonth" + i, BigDecimal.class);
                method.invoke(statYear, sum);
                sum = BigDecimal.ZERO;
            }
        }

        for (Year year : years){
            if (year.getSum().subtract(BigDecimal.ZERO)
                    .compareTo(new BigDecimal(0.001)) < 0) {
                year.setSum(null);
            }
        }

        for (Year year : years){
            if (year.getSum() != null){
                sum = sum.add(year.getSum());
            }
        }
        if (sum.subtract(BigDecimal.ZERO)
                .compareTo(new BigDecimal(0.001)) > 0) {
            statYear.setSum(sum);
        }

        years.add(statYear);

        return years;
    }

    @Override
    public List<Quarter> quarter() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        List<Quarter> quarters = new ArrayList<>();

        Map<String, String> guestMap = guestService.findMap();

        int id = 1;
        for (Map.Entry<String, String> entry : guestMap.entrySet()) {
            Quarter quarter = new Quarter();
            quarter.setId(id++);
            quarter.setGuestId(entry.getKey());
            quarter.setName(entry.getValue());
            quarter.setSum(BigDecimal.ZERO);
            quarters.add(quarter);
        }

        Quarter statQuarter = new Quarter();
        statQuarter.setId(quarters.size()+1);
        statQuarter.setName("合计");

        String state = OrderEnum.DELIVERED.getState();

        Integer currentMonth = LocalDate.now().getMonthValue();
        Integer startMonth = ((currentMonth-1)/3)*3 + 1;
        Integer endMonth = currentMonth + 1;

        BigDecimal sum = BigDecimal.ZERO;   // 纵向合计

        Integer monthCount = endMonth-startMonth;
        for (int i=1; i<=monthCount; i++) {
            List<Object[]> objectss = orderService.sumByStateAndMonth(state, startMonth+i-1);
            for (Object[] objects : objectss) {
                String guestId = (String) objects[0];
                BigDecimal amount = ((BigDecimal) objects[1]).divide(new BigDecimal(10000));
                amount = amount.setScale(2, BigDecimal.ROUND_HALF_UP);

                sum = sum.add(amount);

                for (Quarter quarter : quarters) {
                    if (quarter.getGuestId().equals(guestId)) {
                        Method method = Quarter.class.getDeclaredMethod("setMonth" + i, BigDecimal.class);
                        method.invoke(quarter, amount);

                        quarter.setSum(quarter.getSum().add(amount));

                        break;
                    }
                }

            }

            if (sum.subtract(BigDecimal.ZERO)
                    .compareTo(new BigDecimal(0.001)) > 0) {
                Method method = Quarter.class.getDeclaredMethod("setMonth" + i, BigDecimal.class);
                method.invoke(statQuarter, sum);
                sum = BigDecimal.ZERO;
            }
        }

        for (Quarter quarter : quarters){
            if (quarter.getSum().subtract(BigDecimal.ZERO)
                    .compareTo(new BigDecimal(0.001)) < 0) {
                quarter.setSum(null);
            }
        }

        for (Quarter quarter : quarters){
            if (quarter.getSum() != null){
                sum = sum.add(quarter.getSum());
            }
        }
        if (sum.subtract(BigDecimal.ZERO)
                .compareTo(new BigDecimal(0.001)) > 0) {
            statQuarter.setSum(sum);
        }

        quarters.add(statQuarter);

        return quarters;
    }

    @Override
    public List<Month> month() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        List<Month> months = new ArrayList<>();

        Map<String, String> guestMap = guestService.findMap();

        int id = 1;
        for (Map.Entry<String, String> entry : guestMap.entrySet()) {
            Month month = new Month();
            month.setId(id++);
            month.setGuestId(entry.getKey());
            month.setName(entry.getValue());
            month.setSum(BigDecimal.ZERO);
            months.add(month);
        }

        Month statMonth = new Month();
        statMonth.setId(months.size()+1);
        statMonth.setName("合计");

        String state = OrderEnum.DELIVERED.getState();

        BigDecimal sum = BigDecimal.ZERO;   // 纵向合计2

        for (int i=1; i<=5; i++) {
            List<Object[]> objectss = orderService.sumByStateAndDay(state, 7*(i-1)+1, 7*i+1);
            for (Object[] objects : objectss) {
                String guestId = (String) objects[0];
                BigDecimal amount = ((BigDecimal) objects[1]).divide(new BigDecimal(10000));
                amount = amount.setScale(2, BigDecimal.ROUND_HALF_UP);

                sum = sum.add(amount);

                for (Month month : months) {
                    if (month.getGuestId().equals(guestId)) {
                        Method method = Month.class.getDeclaredMethod("setWeek" + i, BigDecimal.class);
                        method.invoke(month, amount);

                        month.setSum(month.getSum().add(amount));

                        break;
                    }
                }

            }

            if (sum.subtract(BigDecimal.ZERO)
                    .compareTo(new BigDecimal(0.001)) > 0) {
                Method method = Month.class.getDeclaredMethod("setWeek" + i, BigDecimal.class);
                method.invoke(statMonth, sum);
                sum = BigDecimal.ZERO;
            }
        }

        for (Month month : months){
            if (month.getSum().subtract(BigDecimal.ZERO)
                    .compareTo(new BigDecimal(0.001)) < 0) {
                month.setSum(null);
            }
        }

        for (Month month : months){
            if (month.getSum() != null){
                sum = sum.add(month.getSum());
            }
        }
        if (sum.subtract(BigDecimal.ZERO)
                .compareTo(new BigDecimal(0.001)) > 0) {
            statMonth.setSum(sum);
        }

        months.add(statMonth);

        return months;
    }

    @Override
    public List<Week> week() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        List<Week> weeks = new ArrayList<>();

        Map<String, String> guestMap = guestService.findMap();

        int id = 1;
        for (Map.Entry<String, String> entry : guestMap.entrySet()) {
            Week week = new Week();
            week.setId(id++);
            week.setGuestId(entry.getKey());
            week.setName(entry.getValue());
            week.setSum(BigDecimal.ZERO);
            weeks.add(week);
        }

        Week statWeek = new Week();
        statWeek.setId(weeks.size()+1);
        statWeek.setName("合计");

        String state = OrderEnum.DELIVERED.getState();

        BigDecimal sum = BigDecimal.ZERO;   // 纵向合计2

        int dayOfWeek = LocalDate.now().getDayOfWeek().getValue();
        int dayOfMonth = LocalDate.now().getDayOfMonth();

        Date startDate = DateUtils.addDays(new Date(), -(dayOfWeek-1));


        for (int i=1; i<=dayOfWeek; i++) {
            List<Object[]> objectss = orderService.sumByStateAndDateEqual(state, DateUtils.addDays(startDate, i-1));
            for (Object[] objects : objectss) {
                String guestId = (String) objects[0];
                BigDecimal amount = ((BigDecimal) objects[1]).divide(new BigDecimal(10000));
                amount = amount.setScale(2, BigDecimal.ROUND_HALF_UP);

                sum = sum.add(amount);

                for (Week week : weeks) {
                    if (week.getGuestId().equals(guestId)) {
                        Method method = Week.class.getDeclaredMethod("setDay" + i, BigDecimal.class);
                        method.invoke(week, amount);

                        week.setSum(week.getSum().add(amount));

                        break;
                    }
                }

            }

            if (sum.subtract(BigDecimal.ZERO)
                    .compareTo(new BigDecimal(0.001)) > 0) {
                Method method = Week.class.getDeclaredMethod("setDay" + i, BigDecimal.class);
                method.invoke(statWeek, sum);
                sum = BigDecimal.ZERO;
            }
        }

        for (Week week : weeks){
            if (week.getSum().subtract(BigDecimal.ZERO)
                    .compareTo(new BigDecimal(0.001)) < 0) {
                week.setSum(null);
            }
        }

        for (Week week : weeks){
            if (week.getSum() != null){
                sum = sum.add(week.getSum());
            }
        }
        if (sum.subtract(BigDecimal.ZERO)
                .compareTo(new BigDecimal(0.001)) > 0) {
            statWeek.setSum(sum);
        }

        weeks.add(statWeek);

        return weeks;
    }
}
