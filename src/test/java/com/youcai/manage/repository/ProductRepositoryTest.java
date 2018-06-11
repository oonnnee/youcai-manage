package com.youcai.manage.repository;

import com.youcai.manage.dataobject.Product;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductRepositoryTest {

    private static final String NAME = "["+ProductRepositoryTest.class.getName()+"]"+" ";

    @Autowired
    private ProductRepository productRepository;

    @Test
    @Transactional
    public void test(){
        Product product = new Product();
        product.setId("123");
        product.setName("小白菜");
        product.setPCode("0001");
        product.setUnit("千克");
        product.setPrice(new BigDecimal(2.3));
        product.setImgfile("http://xxx.png");
        product.setNote("好吃的小白菜");

        Product result = null;


        result = productRepository.save(product);
        assertTrue(NAME+"save", result != null);

        product.setPrice(new BigDecimal(2.5));
        result = productRepository.save(product);
        assertTrue(NAME+"update", result.getPrice().equals(new BigDecimal(2.5)));

        result = productRepository.findOne("123");
        assertTrue(NAME+"findOne", result != null);

        productRepository.delete("123");
        result = productRepository.findOne("123");
        assertTrue(NAME+"delete", result == null);
    }

    @Test
    public void findByNameLikeAndPCodeIn(){
        /*------------ 1.准备 -------------*/
        String name = "%肉%";
        List<String> pCodes = Arrays.asList("0100", "0200");
        Pageable pageable = new PageRequest(0,3);

        /*------------ 2.查询 -------------*/
        Page<Product> productPage = productRepository.findByNameLikeAndPCodeIn(name, pCodes, pageable);
    }
}