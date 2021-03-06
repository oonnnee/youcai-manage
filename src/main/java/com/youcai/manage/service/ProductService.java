package com.youcai.manage.service;

import com.youcai.manage.dataobject.Product;
import com.youcai.manage.dto.product.AllDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface ProductService {

    Product save(Product product);

    void delete(String id);

    Product update(Product product);

    Product findOne(String id);

    List<Product> findAll();

    Page<Product> findAll(Pageable pageable);

    Page<Product> findBy(String name, List<String> pCodes, Pageable pageable);

    // 产品id: 产品
    Map<String, Product> findMap();

    Long countAll();

    boolean isNameRepeat(String name);
    boolean isNameRepeat(String name, String id);

    List<AllDTO> findAllWith();
}
