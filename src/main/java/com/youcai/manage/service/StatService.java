package com.youcai.manage.service;

import com.youcai.manage.vo.stat.RangeVO;

import java.util.Date;
import java.util.List;

public interface StatService {

    List<RangeVO> range(Date startDate, Date endDate);

}
