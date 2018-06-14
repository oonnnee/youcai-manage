package com.youcai.manage.controller;

import com.youcai.manage.dataobject.Deliver;
import com.youcai.manage.dataobject.Guest;
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
        Page<Guest> guestPage = deliverService.findGuestPage(pageable);
        return ResultVOUtils.success(this.getListVOPage(guestPage, pageable));
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
        Page<Guest> guestIdPage = deliverService.findGuestPageByGuestNameLike(pageable, guestName);
        return ResultVOUtils.success(this.getListVOPage(guestIdPage, pageable));
    }

    @GetMapping("/listByDriverNameLike")
    public ResultVO<Page<ListVO>> listByDriverNameLike(
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
        Page<Guest> guestIdPage = deliverService.findGuestPageByGuestNameLike(pageable, guestName);
        return ResultVOUtils.success(this.getListVOPage(guestIdPage, pageable));
    }

    private Page<ListVO> getListVOPage(Page<Guest> guestPage, Pageable pageable){
        List<ListVO> listVOS = new ArrayList<>();
        for (Guest guest : guestPage.getContent()){
            List<Deliver> delivers = deliverService.findByIdGuestId(guest.getId());
            Map<Integer, Set<Date>> map = new HashMap<>();
            for (Deliver deliver : delivers){
                Integer driverId = deliver.getId().getDriverId();
                Set<Date> dates = map.get(driverId);
                if (dates != null){
                    dates.add(deliver.getId().getDdate());
                }else{
                    Set<Date> ds = new TreeSet<>(new DateComparator());
                    ds.add(deliver.getId().getDdate());
                    map.put(driverId, ds);
                }
            }
            Set<Integer> driverIdSet = map.keySet();
            for (Integer driverId : driverIdSet){
                ListVO listVO = new ListVO(
                        guest.getId(), guest.getName(),
                        driverId, driverService.findOne(driverId).getName(),
                        map.get(driverId)
                );
                listVOS.add(listVO);
            }
        }
        Page<ListVO> listVOPage = new PageImpl<ListVO>(listVOS, pageable, guestPage.getTotalElements());
        return listVOPage;
    }
}
