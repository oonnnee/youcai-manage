package com.youcai.manage.controller;


import com.youcai.manage.dataobject.Guest;
import com.youcai.manage.service.GuestService;
import com.youcai.manage.utils.ResultVOUtils;
import com.youcai.manage.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/guest")
public class GuestRestController {

    @Autowired
    private GuestService guestService;

    @PostMapping("/save")
    public ResultVO<Guest> save(Guest guest){
        //TODO 【管理端】新增客户，表单校验
        Guest saveResult = guestService.save(guest);
        saveResult.setPwd(null);
        return ResultVOUtils.success(saveResult);
    }

    @PostMapping("/delete")
    public ResultVO delete(
            @RequestParam String id
    ){
        guestService.delete(id);
        return ResultVOUtils.success();
    }

    @PostMapping("/update")
    public ResultVO<Guest> update(Guest guest){
        //TODO 【管理端】更新客户，表单校验
        Guest updateResult = guestService.update(guest);
        updateResult.setPwd(null);
        return ResultVOUtils.success(updateResult);
    }

    @PostMapping("/updatePwd")
    public ResultVO updatePwd(String id, String pwd){
        guestService.updatePwd(id, pwd);
        return ResultVOUtils.success();
    }

    @GetMapping("/findOne")
    public ResultVO<Guest> findOne(
            @RequestParam String id
    ){
        Guest findResult = guestService.findOne(id);
        findResult.setPwd(null);
        return ResultVOUtils.success(findResult);
    }

    @GetMapping("/findPage")
    public ResultVO<Page<Guest>> findAll(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ){
        /*------------ 1.准备 -------------*/
        page = page<0 ? 0:page;
        size = size<=0 ? 10:size;
        Pageable pageable = new PageRequest(page, size);

        /*------------ 2.查询 -------------*/
        Page<Guest> guestPage = guestService.findAll(pageable);
        // 密码置空
        for (Guest guest : guestPage.getContent()){
            guest.setPwd(null);
        }
        return ResultVOUtils.success(guestPage);
    }

    @GetMapping("/findPageByNameLike")
    public ResultVO<Page<Guest>> findByNameLike(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String name
    ){
        /*------------ 1.准备 -------------*/
        page = page<0 ? 0:page;
        size = size<=0 ? 10:size;
        Pageable pageable = new PageRequest(page, size);

        /*------------ 2.查询 -------------*/
        Page<Guest> guestPage = guestService.findByNameLike(name, pageable);
        // 密码置空
        for (Guest guest : guestPage.getContent()){
            guest.setPwd(null);
        }
        return ResultVOUtils.success(guestPage);
    }

    @GetMapping("/findPageByIdLike")
    public ResultVO<Page<Guest>> findByIdLike(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String id
    ){
        /*------------ 1.准备 -------------*/
        page = page<0 ? 0:page;
        size = size<=0 ? 10:size;
        Pageable pageable = new PageRequest(page, size);

        /*------------ 2.查询 -------------*/
        Page<Guest> guestPage = guestService.findByIdLike(id, pageable);
        // 密码置空
        for (Guest guest : guestPage.getContent()){
            guest.setPwd(null);
        }
        return ResultVOUtils.success(guestPage);
    }

    @GetMapping("/countAll")
    public ResultVO<Long> count(){
        return ResultVOUtils.success(guestService.countAll());
    }
}
