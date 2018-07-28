package com.youcai.manage.controller;


import com.youcai.manage.dataobject.Guest;
import com.youcai.manage.form.guest.SaveForm;
import com.youcai.manage.form.guest.UpdateForm;
import com.youcai.manage.form.guest.UpdatePwdForm;
import com.youcai.manage.service.GuestService;
import com.youcai.manage.utils.ManageUtils;
import com.youcai.manage.utils.ResultVOUtils;
import com.youcai.manage.vo.ResultVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/guest")
public class GuestRestController {

    @Autowired
    private GuestService guestService;

    @PostMapping("/save")
    public ResultVO<Guest> save(
            @Valid SaveForm saveForm
    ){
        Guest guest = new Guest();
        BeanUtils.copyProperties(saveForm, guest);

        Guest saveResult = guestService.save(guest);

        return ResultVOUtils.success(saveResult);
    }

    @PostMapping("/update")
    public ResultVO<Guest> update(
            @Valid UpdateForm updateForm
    ){
        Guest guest = new Guest();
        BeanUtils.copyProperties(updateForm, guest);

        Guest updateResult = guestService.update(guest);

        return ResultVOUtils.success(updateResult);
    }

    @PostMapping("/updatePwd")
    public ResultVO updatePwd(
            @Valid UpdatePwdForm updatePwdForm
    ){
        guestService.updatePwd(updatePwdForm.getId(), updatePwdForm.getPwd());

        return ResultVOUtils.success("更新客户密码成功");
    }

    @PostMapping("/delete")
    public ResultVO delete(
            @RequestParam String id
    ){
        guestService.delete(id);

        return ResultVOUtils.success("删除客户成功");
    }


    @GetMapping("/findOne")
    public ResultVO<Guest> findOne(
            @RequestParam String id
    ){
        Guest findResult = guestService.findOne(id);
        ManageUtils.ManageException(findResult, "此客户不存在");

        return ResultVOUtils.success(findResult);
    }

    @GetMapping("/findPage")
    public ResultVO<Page<Guest>> findAll(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ){
        Pageable pageable = ManageUtils.getPageable(page, size);

        Page<Guest> guestPage = guestService.findAll(pageable);

        return ResultVOUtils.success(guestPage);
    }

    @GetMapping("/findPageByNameLike")
    public ResultVO<Page<Guest>> findByNameLike(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String name
    ){
        Pageable pageable = ManageUtils.getPageable(page, size);

        Page<Guest> guestPage = guestService.findByNameLike(name, pageable);

        return ResultVOUtils.success(guestPage);
    }
    @GetMapping("/findPageByIdLike")
    public ResultVO<Page<Guest>> findByIdLike(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String id
    ){
        Pageable pageable = ManageUtils.getPageable(page, size);

        Page<Guest> guestPage = guestService.findByIdLike(id, pageable);

        return ResultVOUtils.success(guestPage);
    }
    @GetMapping("/findPageByPhoneLike")
    public ResultVO<Page<Guest>> findByPhoneLike(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String phone
    ){
        Pageable pageable = ManageUtils.getPageable(page, size);

        Page<Guest> guestPage = guestService.findByPhoneLike(phone, pageable);

        return ResultVOUtils.success(guestPage);
    }

    @GetMapping("/countAll")
    public ResultVO<Long> count(){
        return ResultVOUtils.success(guestService.countAll());
    }
}
