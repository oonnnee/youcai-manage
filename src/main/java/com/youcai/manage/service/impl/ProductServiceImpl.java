package com.youcai.manage.service.impl;

import com.youcai.manage.dataobject.Product;
import com.youcai.manage.enums.ResultEnum;
import com.youcai.manage.exception.YoucaiException;
import com.youcai.manage.repository.ProductRepository;
import com.youcai.manage.service.ProductService;
import com.youcai.manage.utils.KeyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Product save(Product product) {
        /*------------ 1.准备 -------------*/
        // 生成产品id
        product.setId(KeyUtils.generate());

        /*------------ 2.保存 -------------*/
        Product saveResult = productRepository.save(product);
        if (saveResult == null){
            throw new YoucaiException(ResultEnum.MANAGE_PRODUCT_SAVE_ERROR);
        }
        return saveResult;
    }

    @Override
    public void delete(String id) {
        productRepository.delete(id);
    }

    @Override
    public Product update(Product product) {
        Product updateResult = productRepository.save(product);
        if (updateResult == null){
            throw new YoucaiException(ResultEnum.MANAGE_PRODUCT_UPDATE_ERROR);
        }
        return updateResult;
    }

    @Override
    public Product findOne(String id) {
        return productRepository.findOne(id);
    }

    @Override
    public Page<Product> findAll(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    @Override
    public Page<Product> findBy(String name, List<String> pCodes, Pageable pageable) {
        Page<Product> productPage = null;
        if (StringUtils.isEmpty(name)){
            if (CollectionUtils.isEmpty(pCodes)){
                // 查询全部
                productPage = productRepository.findAll(pageable);
            }else{
                // 通过pCodes查询
                productPage = productRepository.findByPCodeIn(pCodes, pageable);
            }
        }else{
            name = "%"+name+"%";
            if (CollectionUtils.isEmpty(pCodes)){
                // 通过name查询
                productPage = productRepository.findByNameLike(name, pageable);
            }else{
                // 通过pCodes和name查询
                productPage = productRepository.findByNameLikeAndPCodeIn(name, pCodes, pageable);
            }
        }
        return productPage;
    }

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public Map<String, Product> findMap() {
        List<Product> products = productRepository.findAll();
        Map<String, Product> map = new HashMap<>();
        for (Product product : products){
            map.put(product.getId(), product);
        }
        return map;
    }

    @Override
    public Long countAll() {
        return productRepository.countBy();
    }
}
