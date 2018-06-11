package com.youcai.manage.utils.comparator;

import java.util.Comparator;
import java.util.Date;

public class DateComparator implements Comparator<Date> {

    @Override
    public int compare(Date o1, Date o2) {
        return (int) (o2.getTime() - o1.getTime());
    }

}
