package com.youcai.manage.service.impl;

import com.youcai.manage.dataobject.Driver;
import com.youcai.manage.enums.ResultEnum;
import com.youcai.manage.exception.ManageException;
import com.youcai.manage.repository.DriverRepository;
import com.youcai.manage.service.DeliverService;
import com.youcai.manage.service.DriverService;
import com.youcai.manage.utils.ManageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DriverServiceImpl implements DriverService {

    private void checkName(String name){
        ManageUtils.ManageException(this.isNameRepeat(name), "此司机已存在");
    }
    private void checkName(String name, Integer id){
        ManageUtils.ManageException(this.isNameRepeat(name, id), "此司机已存在");
    }

    @Autowired
    private DriverRepository driverRepository;
    @Autowired
    private DeliverService deliverService;

    @Override
    @Transactional
    public Driver save(Driver driver) {
        this.checkName(driver.getName());

        Driver saveResult = driverRepository.save(driver);
        ManageUtils.ManageException(saveResult, "新增司机失败，请稍后再试");

        return saveResult;
    }

    @Override
    @Transactional
    public Driver update(Driver driver) {
        ManageUtils.ManageException(driverRepository.findOne(driver.getId()), "此司机不存在");

        this.checkName(driver.getName(), driver.getId());

        Driver updateResult = driverRepository.save(driver);
        ManageUtils.ManageException(updateResult, "更新司机信息失败，请稍后再试");

        return updateResult;
    }


    @Override
    @Transactional
    public void delete(Integer id) {
        ManageUtils.ManageException(driverRepository.findOne(id), "删除失败\n原因：此司机不存在");
        ManageUtils.ManageException(deliverService.isDriverExist(id), "删除失败\n原因：送货单中存在此司机");

        driverRepository.delete(id);
    }


    @Override
    public Driver findOne(Integer id) {
        Driver findResult = driverRepository.findOne(id);

        return findResult;
    }

    @Override
    public Page<Driver> list(Pageable pageable) {
        Page<Driver> driverPage = driverRepository.findAll(pageable);

        return driverPage;
    }

    @Override
    public Page<Driver> findByNameLike(String name, Pageable pageable) {
        if (StringUtils.isEmpty(name)){
            return driverRepository.findAll(pageable);
        }

        return driverRepository.findByNameLike("%"+name+"%", pageable);
    }

    @Override
    public Map<Integer, String> findMap() {
        List<Driver> drivers = driverRepository.findAll();
        Map<Integer, String> map = new HashMap<>();
        for (Driver driver : drivers){
            map.put(driver.getId(), driver.getName());
        }
        return map;
    }

    @Override
    public Map<Integer, String> findMapByNameLike(String name) {
        List<Driver> drivers = driverRepository.findByNameLike("%"+name+"%");
        Map<Integer, String> map = new HashMap<>();
        for (Driver driver : drivers){
            map.put(driver.getId(), driver.getName());
        }
        return map;
    }

    @Override
    public List<Driver> findAll() {
        return driverRepository.findAll();
    }
    @Override
    public List<Driver> findAllWithState() {
        List<Driver> drivers = driverRepository.findAll();
        for (Driver driver : drivers){
            String note;
            if (deliverService.isDriverIdle(driver.getId())){
                note = "（空闲）";
            }else{
                note = "（送货中）";
            }
            driver.setName(driver.getName()+note);
        }
        return drivers;
    }

    @Override
    public Long countAll() {
        return driverRepository.count();
    }

    @Override
    public boolean isNameRepeat(String name) {
        return driverRepository.findByName(name) != null;
    }
    @Override
    public boolean isNameRepeat(String name, Integer id) {
        Driver driver = driverRepository.findByName(name);
        return driver == null ?
                false : !driver.getId().equals(id);
    }
}
