package com.youcai.manage.service;

import com.youcai.manage.dataobject.Driver;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface DriverService {

    Driver save(Driver driver);

    void delete(Integer id);

    Driver update(Driver driver);

    Driver findOne(Integer id);

    List<Driver> findAll();

    Page<Driver> list(Pageable pageable);

    Page<Driver> findByNameLike(String name, Pageable pageable);

    Map<Integer, String> findMap();
    Map<Integer, String> findMapByNameLike(String name);
}
