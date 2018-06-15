package com.youcai.manage.controller;

import com.youcai.manage.dataobject.Deliver;
import com.youcai.manage.dataobject.Guest;
import com.youcai.manage.dto.deliver.ListDTO;
import com.youcai.manage.dto.deliver.ListKey;
import com.youcai.manage.service.DeliverService;
import com.youcai.manage.service.DriverService;
import com.youcai.manage.utils.ResultVOUtils;
import com.youcai.manage.utils.comparator.DateComparator;
import com.youcai.manage.vo.ResultVO;
import com.youcai.manage.vo.deliver.ListVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/deliver")
public class DeliverController {
    @Autowired
    private DeliverService deliverService;
    @Autowired
    private DriverService driverService;

    @GetMapping("/list")
    public ResultVO<Page<ListVO>> list(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ){
        /*------------ 1.准备 -------------*/
        // 分页
        page = page<0 ? 0:page;
        size = size<=0 ? 10:size;
        Pageable pageable = new PageRequest(page, size);
        /*------------ 2.查询 -------------*/
        List<ListDTO> listDTOS = deliverService.findListDTOS();
        return ResultVOUtils.success(getListVOPage(listDTOS, pageable));
    }

    @GetMapping("/listByGuestNameLike")
    public ResultVO<Page<ListVO>> listByGuestNameLike(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String guestName
    ){
        /*------------ 1.准备 -------------*/
        // 分页
        page = page<0 ? 0:page;
        size = size<=0 ? 10:size;
        Pageable pageable = new PageRequest(page, size);
        /*------------ 2.查询 -------------*/
        List<ListDTO> listDTOS = deliverService.findListDTOSByGuestName(guestName);
        return ResultVOUtils.success(getListVOPage(listDTOS, pageable));
    }

    @GetMapping("/listByDriverNameLike")
    public ResultVO<Page<ListVO>> listByDriverNameLike(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String driverName
    ){
        /*------------ 1.准备 -------------*/
        // 分页
        page = page<0 ? 0:page;
        size = size<=0 ? 10:size;
        Pageable pageable = new PageRequest(page, size);
        /*------------ 2.查询 -------------*/
        List<ListDTO> listDTOS = deliverService.findListDTOSByDriverName(driverName);
        return ResultVOUtils.success(getListVOPage(listDTOS, pageable));
    }

    private Page<ListVO> getListVOPage(List<ListDTO> listDTOS, Pageable pageable){
        Map<ListKey, Set<Date>> map = new HashMap<>();
        for (ListDTO listDTO : listDTOS){
            Set<Date> dates = map.get(listDTO.getListKey());
            if (dates != null){
                dates.add(listDTO.getDate());
            }else{
                Set<Date> ds = new TreeSet<>(new DateComparator());
                ds.add(listDTO.getDate());
                map.put(listDTO.getListKey(), ds);
            }
        }
        List<ListVO> listVOS = new ArrayList<>();
        Set<ListKey> listKeySet = map.keySet();
        for (ListKey listKey : listKeySet){
            ListVO listVO = new ListVO();
            listVO.setDriverId(listKey.getDriverId());
            listVO.setGuestId(listKey.getGuestId());
            listVO.setDriverName(listKey.getDriverName());
            listVO.setGuestName(listKey.getGuestName());
            listVO.setDates(map.get(listKey));
            listVOS.add(listVO);
        }
        return new PageImpl<>(listVOS, pageable, listVOS.size());
    }
}
