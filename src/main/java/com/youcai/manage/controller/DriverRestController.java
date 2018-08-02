package com.youcai.manage.controller;

import com.youcai.manage.dataobject.Driver;
import com.youcai.manage.form.driver.SaveForm;
import com.youcai.manage.form.driver.UpdateForm;
import com.youcai.manage.service.DriverService;
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
import java.util.List;

@RestController
@RequestMapping("/driver")
public class DriverRestController {

    @Autowired
    private DriverService driverService;

    @PostMapping("/save")
    public ResultVO<Driver> save(
            @Valid SaveForm saveForm
    ){
        Driver driver = new Driver();
        BeanUtils.copyProperties(saveForm, driver);

        Driver saveResult = driverService.save(driver);

        return ResultVOUtils.success(saveResult);
    }

    @PostMapping("/update")
    public ResultVO<Driver> update(
            @Valid UpdateForm updateForm
    ){
        Driver driver = new Driver();
        BeanUtils.copyProperties(updateForm, driver);

        Driver updateResult = driverService.update(driver);

        return ResultVOUtils.success(updateResult);
    }

    @PostMapping("/delete")
    public ResultVO delete(
            @RequestParam Integer id
    ){
        driverService.delete(id);

        return ResultVOUtils.success("删除司机成功");
    }

    @GetMapping("/findOne")
    public ResultVO<Driver> find(
            @RequestParam Integer id
    ){
        Driver findResult = driverService.findOne(id);
        ManageUtils.ManageException(findResult, "此司机不存在");

        return ResultVOUtils.success(findResult);
    }

    @GetMapping("/findPageByNameLike")
    public ResultVO<Page<Driver>> findByNameLike(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String name
    ){
        Pageable pageable = ManageUtils.getPageable(page, size);

        Page<Driver> driverPage = driverService.findByNameLike(name, pageable);

        return ResultVOUtils.success(driverPage);
    }

    @GetMapping("/findPage")
    public ResultVO<Page<Driver>> list(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ){
        Pageable pageable = ManageUtils.getPageable(page, size);

        Page<Driver> driverPage = driverService.list(pageable);

        return ResultVOUtils.success(driverPage);
    }

    @GetMapping("/findList")
    public ResultVO<List<Driver>> findAll(){
        return ResultVOUtils.success(driverService.findAll());
    }

    @GetMapping("/findListWithState")
    public ResultVO<List<Driver>> findListWithState(){
        return ResultVOUtils.success(driverService.findAllWithState());
    }

    @GetMapping("/countAll")
    public ResultVO<Long> count(){
        return ResultVOUtils.success(driverService.countAll());
    }
}
