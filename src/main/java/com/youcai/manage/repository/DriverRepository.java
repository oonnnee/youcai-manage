package com.youcai.manage.repository;

import com.youcai.manage.dataobject.Driver;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DriverRepository extends JpaRepository<Driver, Integer> {
    Page<Driver> findByNameLike(String name, Pageable pageable);
    List<Driver> findByNameLike(String name);
}
