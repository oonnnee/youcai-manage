package com.youcai.manage.service.impl;

import com.youcai.manage.enums.OrderEnum;
import com.youcai.manage.service.GuestService;
import com.youcai.manage.service.OrderService;
import com.youcai.manage.service.StatService;
import com.youcai.manage.vo.stat.RangeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
}
