package com.youcai.manage.controller;

import com.youcai.manage.dataobject.Category;
import com.youcai.manage.dataobject.Product;
import com.youcai.manage.service.CategoryService;
import com.youcai.manage.service.ProductService;
import com.youcai.manage.utils.ResultVOUtils;
import com.youcai.manage.vo.CategoryWithProductsVO;
import com.youcai.manage.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/category")
public class CategoryRestController {

    @Autowired
    private CategoryService categoryService;
    
    @Autowired
    private ProductService productService;

    @GetMapping("/findMap")
    public ResultVO<Map<String, String>> map(){
        Map<String, String> categoryMap = categoryService.findAllInMap();
        return ResultVOUtils.success(categoryMap);
    }

    @GetMapping("/findList")
    public ResultVO<List<Category>> list(){
        List<Category> categories = categoryService.findAll();
        return ResultVOUtils.success(categories);
    }

    @GetMapping("/findListWithProducts")
    public ResultVO<List<CategoryWithProductsVO>> listWithProducts(){
        /*------------ 1.查询数据 -------------*/
        // 产品大类数据
        List<Category> categories = categoryService.findAll();
        // 产品列表数据
        List<Product> products = productService.findAll();
        /*------------ 2.数据拼装 -------------*/
        List<CategoryWithProductsVO> categoryWithProductsVOS = new ArrayList<>();
        for (Category category : categories){
            CategoryWithProductsVO categoryWithProductsVO = new CategoryWithProductsVO();
            categoryWithProductsVO.setCategoryCode(category.getCode());
            categoryWithProductsVO.setCategoryName(category.getName());
            categoryWithProductsVO.setNote(category.getNote());
            categoryWithProductsVO.setProducts(new ArrayList<>());
            categoryWithProductsVOS.add(categoryWithProductsVO);
        }
        for (Product product : products){
            for (CategoryWithProductsVO categoryWithProductsVO : categoryWithProductsVOS){
                if (categoryWithProductsVO.getCategoryCode().equals(product.getPCode())){
                    categoryWithProductsVO.getProducts().add(product);
                    break;
                }
            }
        }
        return ResultVOUtils.success(categoryWithProductsVOS);
    }
}
