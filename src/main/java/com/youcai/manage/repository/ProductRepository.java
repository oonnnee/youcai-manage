package com.youcai.manage.repository;

import com.youcai.manage.dataobject.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, String> {

    Page<Product> findByNameLike(String name, Pageable pageable);

    Page<Product> findByPCodeIn(List<String> pCodes, Pageable pageable);

    Page<Product> findByNameLikeAndPCodeIn(String name, List<String> pCodes, Pageable pageable);

    Long countBy();

    Product findByName(String name);

    @Query(value = "SELECT c.name as category, pro.id, pro.name, pro.unit, pro.price, pro.imgfile " +
            "FROM product pro LEFT JOIN category c ON c.code= pro.p_code ORDER BY pro.p_code",nativeQuery = true)
    List<Object[]> findAllWith();
}
