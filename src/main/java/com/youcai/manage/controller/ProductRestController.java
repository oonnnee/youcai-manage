package com.youcai.manage.controller;

import com.youcai.manage.dataobject.Product;
import com.youcai.manage.form.product.SaveForm;
import com.youcai.manage.form.product.UpdateForm;
import com.youcai.manage.service.CategoryService;
import com.youcai.manage.service.ProductService;
import com.youcai.manage.utils.ManageUtils;
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

import javax.validation.Valid;
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
    public ResultVO<Product> save(
            @Valid SaveForm saveForm
    ){
        Product product = new Product();
        BeanUtils.copyProperties(saveForm, product);

        Product saveResult = productService.save(product);

        return ResultVOUtils.success(saveResult);
    }

    @PostMapping("/update")
    public ResultVO<Product> update(
            @Valid UpdateForm updateForm
    ){
        Product product = new Product();
        BeanUtils.copyProperties(updateForm, product);

        Product updateResult = productService.update(product);

        return ResultVOUtils.success(updateResult);
    }

    @PostMapping("/delete")
    public ResultVO delete(
            @RequestParam String id
    ){
        productService.delete(id);

        return ResultVOUtils.success("删除产品成功");
    }

    @GetMapping("/findOne")
    public ResultVO<ProductVO> findOne(
            @RequestParam String id
    ){
        Product product = productService.findOne(id);
        ManageUtils.ManageException(product, "此产品不存在");

        Map<String, String> categoryMap = categoryService.findAllInMap();

        ProductVO productVO = new ProductVO();
        BeanUtils.copyProperties(product, productVO);

        productVO.setPCodeName(categoryMap.get(productVO.getPCode()));

        return ResultVOUtils.success(productVO);
    }

    @GetMapping("/findPage")
    public ResultVO<Page<ProductVO>> list(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size
    ){
        page = page<0 ? 0:page;
        size = size<=0 ? 10:size;
        Pageable pageable = new PageRequest(page, size);

        Page<Product> productPage = productService.findAll(pageable);
        Map<String, String> categoryMap = categoryService.findAllInMap();

        List<ProductVO> productVOS = productPage.getContent().stream().map(e -> {
            ProductVO productVO = new ProductVO();
            BeanUtils.copyProperties(e, productVO);
            productVO.setPCodeName(categoryMap.get(productVO.getPCode()));
            return productVO;
        }).collect(Collectors.toList());
        Page<ProductVO> productVOPage = new PageImpl<ProductVO>(productVOS, pageable, productPage.getTotalElements());

        return ResultVOUtils.success(productVOPage);
    }


    @GetMapping("/findPageByNameLikeAndCodeIn")
    public ResultVO<Page<ProductVO>> findByPCodeIn(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false, name = "codes") String codeStr
    ){
        page = page<0 ? 0:page;
        size = size<=0 ? 10:size;
        Pageable pageable = new PageRequest(page, size);

        List<String> codes = null;
        if (StringUtils.isEmpty(codeStr) == false){
            String[] codeArr = codeStr.split(",");
            codes = Arrays.asList(codeArr);
        }

        Page<Product> productPage = productService.findBy(name, codes, pageable);
        Map<String, String> categoryMap = categoryService.findAllInMap();

        List<ProductVO> productVOS = productPage.getContent().stream().map(e -> {
            ProductVO productVO = new ProductVO();
            BeanUtils.copyProperties(e, productVO);
            productVO.setPCodeName(categoryMap.get(productVO.getPCode()));
            return productVO;
        }).collect(Collectors.toList());
        Page<ProductVO> productVOPage = new PageImpl<ProductVO>(productVOS, pageable, productPage.getTotalElements());

        return ResultVOUtils.success(productVOPage);
    }

    @GetMapping("/countAll")
    public ResultVO<Long> count(){
        return ResultVOUtils.success(productService.countAll());
    }

    @GetMapping("/findList")
    public ResultVO list(){
        return ResultVOUtils.success(productService.findAll());
    }
}
