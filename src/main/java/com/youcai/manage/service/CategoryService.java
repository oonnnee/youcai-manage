package com.youcai.manage.service;

import com.youcai.manage.dataobject.Category;

import java.util.List;
import java.util.Map;

public interface CategoryService {

    Category findOne(String code);

    List<Category> findAll();

    Map<String, String> findAllInMap();
}
