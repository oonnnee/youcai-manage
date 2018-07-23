package com.youcai.manage.controller;

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

}
