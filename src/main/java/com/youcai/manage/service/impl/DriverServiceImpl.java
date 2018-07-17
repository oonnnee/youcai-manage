package com.youcai.manage.service.impl;

import com.youcai.manage.dataobject.Driver;
import com.youcai.manage.enums.ResultEnum;
import com.youcai.manage.exception.ManageException;
import com.youcai.manage.repository.DriverRepository;
import com.youcai.manage.service.DriverService;
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

    @Autowired
    private DriverRepository driverRepository;

    @Override
    @Transactional
    public Driver save(Driver driver) {
        Driver saveResult = driverRepository.save(driver);
        if (saveResult == null){
            throw new ManageException("新增司机失败");
        }
        return saveResult;
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        driverRepository.delete(id);
    }

    @Override
    @Transactional
    public Driver update(Driver driver) {
        if (driver.getId() == null){
            throw new ManageException("更新司机失败，司机id为空");
        }
        Driver updateResult = driverRepository.save(driver);
        if (updateResult == null){
            throw new ManageException("更新司机失败");
        }
        return updateResult;
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
    public Long countAll() {
        return driverRepository.count();
    }
}
