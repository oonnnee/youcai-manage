package com.youcai.manage.repository;

import com.youcai.manage.dataobject.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, String> {

}
