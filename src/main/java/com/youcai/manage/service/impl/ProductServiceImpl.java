package com.youcai.manage.service.impl;

import com.youcai.manage.dataobject.Product;
import com.youcai.manage.dto.product.AllDTO;
import com.youcai.manage.exception.ManageException;
import com.youcai.manage.repository.ProductRepository;
import com.youcai.manage.service.DeliverService;
import com.youcai.manage.service.OrderService;
import com.youcai.manage.service.PricelistService;
import com.youcai.manage.service.ProductService;
import com.youcai.manage.transform.ProductTransform;
import com.youcai.manage.utils.KeyUtils;
import com.youcai.manage.utils.ManageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProductServiceImpl implements ProductService {

    private void checkName(String name){
        ManageUtils.ManageException(this.isNameRepeat(name), "此产品已存在");
    }
    private void checkName(String name, String id){
        ManageUtils.ManageException(this.isNameRepeat(name, id), "此产品已存在");
    }

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private PricelistService pricelistService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private DeliverService deliverService;

    @Override
    @Transactional
    public Product save(Product product) {
        checkName(product.getName());

        product.setId(KeyUtils.generate());

        Product saveResult = productRepository.save(product);
        ManageUtils.ManageException(saveResult, "添加产品失败，请稍后再试");

        return saveResult;
    }

    @Override
    @Transactional
    public Product update(Product product) {
        checkName(product.getName(), product.getId());

        Product updateResult = productRepository.save(product);
        ManageUtils.ManageException(updateResult, "更新产品失败，请稍后再试");

        return updateResult;
    }

    @Override
    @Transactional
    public void delete(String id) {
        ManageUtils.ManageException(this.findOne(id), "删除产品失败\n原因：此产品不存在");
        ManageUtils.ManageException(pricelistService.isProductExist(id), "删除产品失败\n原因：报价单中存在此产品");
        ManageUtils.ManageException(orderService.isProductExist(id), "删除产品失败\n原因：采购单中存在此产品");
        ManageUtils.ManageException(deliverService.isProductExist(id), "删除产品失败\n原因：送货单中存在此产品");

        productRepository.delete(id);
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

    @Override
    public boolean isNameRepeat(String name) {
        return productRepository.findByName(name) != null;
    }
    @Override
    public boolean isNameRepeat(String name, String id) {
        Product product = productRepository.findByName(name);
        return product == null ?
                false : !product.getId().equals(id);
    }

    @Override
    public List<AllDTO> findAllWith() {
        List<Object[]> objectss = productRepository.findAllWith();
        List<AllDTO> allDTOS = ProductTransform.objectssToAllDTOS(objectss);

        return allDTOS;
    }
}
