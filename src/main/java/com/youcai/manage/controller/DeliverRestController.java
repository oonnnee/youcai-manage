package com.youcai.manage.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.youcai.manage.dataobject.*;
import com.youcai.manage.dto.deliver.ProductDTO;
import com.youcai.manage.dto.excel.deliver.Export;
import com.youcai.manage.dto.excel.deliver.ProductExport;
import com.youcai.manage.enums.ResultEnum;
import com.youcai.manage.exception.ManageException;
import com.youcai.manage.service.CategoryService;
import com.youcai.manage.service.DeliverService;
import com.youcai.manage.service.DriverService;
import com.youcai.manage.service.ProductService;
import com.youcai.manage.utils.ResultVOUtils;
import com.youcai.manage.utils.comparator.DateComparator;
import com.youcai.manage.utils.excel.deliver.ExportUtil;
import com.youcai.manage.vo.ResultVO;
import com.youcai.manage.vo.deliver.CategoryVO;
import com.youcai.manage.vo.deliver.ListVO;
import com.youcai.manage.vo.deliver.ProductVO;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/deliver")
public class DeliverRestController {
    @Autowired
    private DeliverService deliverService;
    @Autowired
    private DriverService driverService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductService productService;

    @GetMapping("/list")
    public ResultVO<Page<ListVO>> list(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ){
        /*------------ 1.准备 -------------*/
        // 分页
        page = page<0 ? 0:page;
        size = size<=0 ? 10:size;
        Pageable pageable = new PageRequest(page, size);
        /*------------ 2.查询 -------------*/
        Set<ListVO> listVOSet = deliverService.findListVOSet();
        return ResultVOUtils.success(getListVOPage(listVOSet, pageable));
    }

    @GetMapping("/listByGuestNameLike")
    public ResultVO<Page<ListVO>> listByGuestNameLike(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String guestName
    ){
        /*------------ 1.准备 -------------*/
        // 分页
        page = page<0 ? 0:page;
        size = size<=0 ? 10:size;
        Pageable pageable = new PageRequest(page, size);
        /*------------ 2.查询 -------------*/
        Set<ListVO> listVOSet = deliverService.findListVOSetByGuestName(guestName);
        return ResultVOUtils.success(getListVOPage(listVOSet, pageable));
    }

    @GetMapping("/listByDriverNameLike")
    public ResultVO<Page<ListVO>> listByDriverNameLike(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String driverName
    ){
        /*------------ 1.准备 -------------*/
        // 分页
        page = page<0 ? 0:page;
        size = size<=0 ? 10:size;
        Pageable pageable = new PageRequest(page, size);
        /*------------ 2.查询 -------------*/
        Set<ListVO> listVOSet = deliverService.findListVOSetByDriverName(driverName);
        return ResultVOUtils.success(getListVOPage(listVOSet, pageable));
    }

    private Page<ListVO> getListVOPage(Set<ListVO> listVOSet, Pageable pageable){
        List<ListVO> listVOS = new ArrayList<>();
        for (ListVO l : listVOSet){
            ListVO listVO = new ListVO(l);
            listVOS.add(listVO);
        }
        return new PageImpl<>(listVOS, pageable, listVOS.size());
    }

    @GetMapping("/findCategories")
    public ResultVO<List<CategoryVO>> findCategories(
            @RequestParam String guestId,
            @RequestParam Integer driverId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date date
    ){
        /*------------ 1.查询数据 -------------*/
        /*--- 产品大类数据 ---*/
        List<Category> categories = categoryService.findAll();
        /*--- 送货数据 ---*/
        List<Deliver> delivers = deliverService.findByGuestIdAndDriverIdAndDate(guestId, driverId, date);
        /*--- 产品数据 ---*/
        Map<String, Product> productMap = productService.findMap();
        /*------------ 2.数据拼装 -------------*/
        List<CategoryVO> categoryVOS = new ArrayList<>();
        for (Category category : categories){
            CategoryVO categoryVO = new CategoryVO();
            categoryVO.setCode(category.getCode());
            categoryVO.setName(category.getName());
            List<ProductVO> productVOS = new ArrayList<>();
            for (Deliver deliver : delivers){
                Product product = productMap.get(deliver.getId().getProductId());
                if (product.getPCode().equals(category.getCode())){
                    ProductVO productVO = new ProductVO();
                    productVO.setId(deliver.getId().getProductId());
                    productVO.setName(product.getName());
                    productVO.setUnit(product.getUnit());
                    productVO.setPrice(deliver.getPrice());
                    productVO.setNum(deliver.getNum());
                    productVO.setAmount(deliver.getAmount());
                    productVO.setNote(deliver.getNote());
                    productVOS.add(productVO);
                }
            }
            categoryVO.setProducts(productVOS);
            categoryVOS.add(categoryVO);
        }
        /*------------ 3.返回 -------------*/
        return ResultVOUtils.success(categoryVOS);
    }

    @PostMapping("/delete")
    public ResultVO delete(
            @RequestParam String guestId,
            @RequestParam Integer driverId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date date
    ){
        deliverService.delete(guestId, driverId, date);
        return ResultVOUtils.success();
    }

    @PostMapping("/save")
    public ResultVO save(
            @RequestParam String guestId,
            @RequestParam Integer driverId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date date,
            @RequestParam String products
    ){
        List<ProductDTO> productDTOS = new ArrayList<>();
        try {
            productDTOS = new Gson().fromJson(products,
                    new TypeToken<List<ProductDTO>>() {
                    }.getType());
        } catch (Exception e) {
            throw new ManageException(ResultEnum.MANAGE_DELIVER_SAVE_JSON_PARSE_ERROR);
        }
        List<Deliver> delivers = productDTOS.stream().map(e ->
                new Deliver(new DeliverKey(driverId, guestId, date, e.getId()),
                        e.getPrice(), e.getNum(), e.getPrice().multiply(e.getNum()), e.getNote())
        ).collect(Collectors.toList());
        deliverService.save(delivers);
        return ResultVOUtils.success();
    }

}
