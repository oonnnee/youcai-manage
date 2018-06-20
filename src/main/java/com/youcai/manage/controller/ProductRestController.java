package com.youcai.manage.controller;

import com.youcai.manage.dataobject.Product;
import com.youcai.manage.service.CategoryService;
import com.youcai.manage.service.ProductService;
import com.youcai.manage.utils.ResultVOUtils;
import com.youcai.manage.vo.ProductVO;
import com.youcai.manage.vo.ResultVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/product")
public class ProductRestController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @PostMapping("/save")
    public ResultVO<Product> save(Product product){
        Product saveResult = productService.save(product);
        return ResultVOUtils.success(saveResult);
    }

    @PostMapping("/delete")
    public ResultVO delete(
            @RequestParam String id
    ){
        productService.delete(id);
        return ResultVOUtils.success();
    }

    @PostMapping("/update")
    public ResultVO<Product> update(Product product){
        Product updateResult = productService.update(product);
        return ResultVOUtils.success(updateResult);
    }

    @GetMapping("/find")
    public ResultVO<ProductVO> findOne(
            @RequestParam String id
    ){
        /*------------ 1.查询 -------------*/
        Product product = productService.findOne(id);
        Map<String, String> categoryMap = categoryService.findAllInMap();

        /*------------ 2.数据拼装 -------------*/
        ProductVO productVO = new ProductVO();
        BeanUtils.copyProperties(product, productVO);
        productVO.setPCodeName(categoryMap.get(productVO.getPCode()));

        return ResultVOUtils.success(productVO);
    }

    @GetMapping("/list")
    public ResultVO<Page<ProductVO>> list(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size
    ){
        /*------------ 1.准备 -------------*/
        page = page<0 ? 0:page;
        size = size<=0 ? 10:size;
        Pageable pageable = new PageRequest(page, size);

        /*------------ 2.查询 -------------*/
        Page<Product> productPage = productService.findAll(pageable);
        Map<String, String> categoryMap = categoryService.findAllInMap();

        /*------------ 3.数据拼装 -------------*/
        List<ProductVO> productVOS = productPage.getContent().stream().map(e -> {
            ProductVO productVO = new ProductVO();
            BeanUtils.copyProperties(e, productVO);
            productVO.setPCodeName(categoryMap.get(productVO.getPCode()));
            return productVO;
        }).collect(Collectors.toList());
        Page<ProductVO> productVOPage = new PageImpl<ProductVO>(productVOS, pageable, productPage.getTotalElements());

        return ResultVOUtils.success(productVOPage);
    }


    @GetMapping("/findBy")
    public ResultVO<Page<ProductVO>> findByPCodeIn(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false, name = "PCodes") String codeStr
    ){
        /*------------ 1.准备 -------------*/
        // 分页
        page = page<0 ? 0:page;
        size = size<=0 ? 10:size;
        Pageable pageable = new PageRequest(page, size);
        // 产品大类编码列表
        List<String> codes = null;
        if (StringUtils.isEmpty(codeStr) == false){
            String[] codeArr = codeStr.split(",");
            codes = Arrays.asList(codeArr);
        }

        /*------------ 2.查询 -------------*/
        Page<Product> productPage = productService.findBy(name, codes, pageable);
        Map<String, String> categoryMap = categoryService.findAllInMap();

        /*------------ 3.数据拼装 -------------*/
        List<ProductVO> productVOS = productPage.getContent().stream().map(e -> {
            ProductVO productVO = new ProductVO();
            BeanUtils.copyProperties(e, productVO);
            productVO.setPCodeName(categoryMap.get(productVO.getPCode()));
            return productVO;
        }).collect(Collectors.toList());
        Page<ProductVO> productVOPage = new PageImpl<ProductVO>(productVOS, pageable, productPage.getTotalElements());

        return ResultVOUtils.success(productVOPage);
    }

    @GetMapping("/count")
    public ResultVO<Long> count(){
        return ResultVOUtils.success(productService.countAll());
    }
}
