package com.youcai.manage.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.youcai.manage.dataobject.*;
import com.youcai.manage.dto.deliver.ProductDTO;
import com.youcai.manage.dto.excel.deliver.Export;
import com.youcai.manage.dto.excel.deliver.ProductExport;
import com.youcai.manage.enums.ResultEnum;
import com.youcai.manage.exception.ManageException;
import com.youcai.manage.service.*;
import com.youcai.manage.utils.DeliverUtils;
import com.youcai.manage.utils.ResultVOUtils;
import com.youcai.manage.utils.comparator.DateComparator;
import com.youcai.manage.utils.excel.deliver.ExportUtil;
import com.youcai.manage.vo.ResultVO;
import com.youcai.manage.vo.deliver.CategoryVO;
import com.youcai.manage.vo.deliver.DeliversVO;
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
import org.springframework.util.CollectionUtils;
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
    @Autowired
    private GuestService guestService;

    @GetMapping("/findPage")
    public ResultVO<Page<ListVO>> list(
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
        Page<Guest> guestPage = deliverService.findGuestPage(pageable, guestName);
        List<ListVO> listVOS = new ArrayList<>();
        for (Guest guest : guestPage.getContent()){
            List<Deliver> delivers = deliverService.findByIdGuestId(guest.getId());
            Set<Date> dates = new TreeSet<>(new DateComparator());
            for (Deliver deliver : delivers){
                dates.add(deliver.getId().getDdate());
            }
            ListVO listVO = new ListVO(guest.getId(), guest.getName(), dates);
            listVOS.add(listVO);
        }
        Page<ListVO> listVOPage = new PageImpl<ListVO>(listVOS, pageable, guestPage.getTotalElements());
        return ResultVOUtils.success(listVOPage);
    }

    @GetMapping("/findOneWithCategories")
    public ResultVO<List<CategoryVO>> findCategories(
            @RequestParam String guestId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date date
    ){
        /*------------ 1.查询数据 -------------*/
        /*--- 产品大类数据 ---*/
        List<Category> categories = categoryService.findAll();
        /*--- 送货数据 ---*/
        List<Deliver> delivers = deliverService.findByGuestIdAndDate(guestId, date);
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

//    @PostMapping("/delete")
//    public ResultVO delete(
//            @RequestParam String guestId,
//            @RequestParam Integer driverId,
//            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date date
//    ){
//        deliverService.delete(guestId, driverId, date);
//        return ResultVOUtils.success();
//    }

    @PostMapping("/save")
    public ResultVO save(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date orderDate,
            @RequestParam String guestId,
            @RequestParam Integer driverId,
            @RequestParam String products
    ){
        List<ProductDTO> productDTOS = new ArrayList<>();
        try {
            productDTOS = new Gson().fromJson(products,
                    new TypeToken<List<ProductDTO>>() {
                    }.getType());
        } catch (Exception e) {
            throw new ManageException("新增送货单失败，产品json解析错误");
        }
        List<Deliver> delivers = productDTOS.stream().map(e ->
                new Deliver(new DeliverKey(driverId, guestId, new Date(), e.getId(), DeliverUtils.getStateDelivering()),
                        e.getPrice(), e.getNum(), e.getPrice().multiply(e.getNum()), e.getNote())
        ).collect(Collectors.toList());
        deliverService.save(delivers, guestId, orderDate);
        return ResultVOUtils.success();
    }

    //  TODO 更新api
    @GetMapping("/findOne")
    public ResultVO findOne(
            @RequestParam String guestId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date date
    ){
        Guest guest = guestService.findOne(guestId);
        if (guest == null){
            return ResultVOUtils.error("未查询到此客户");
        }
        List<Deliver> delivers = deliverService.findByGuestIdAndDate(guestId, date);

        if (CollectionUtils.isEmpty(delivers)){
            return ResultVOUtils.error("未查询到此送货单");
        }
        Driver driver = driverService.findOne(delivers.get(0).getId().getDriverId());
        if (driver == null){
            return ResultVOUtils.error("未查询到此司机");
        }
        Map<String, Product> productMap = productService.findMap();

        DeliversVO deliversVO = new DeliversVO();

        List<ProductVO> products = delivers.stream().map( e -> {
                    Product product = productMap.get(e.getId().getProductId());
            return new ProductVO(
                            product.getId(), product.getName(), product.getUnit(),
                            e.getPrice(), e.getNum(), e.getAmount(), e.getNote()
                    );
                }
        ).collect(Collectors.toList());

        deliversVO.setGuestId(guestId);
        deliversVO.setGuestName(guest.getName());
        deliversVO.setDriverId(driver.getId());
        deliversVO.setDriverName(driver.getName());
        deliversVO.setDate(date);
        deliversVO.setState(delivers.get(0).getId().getState());
        deliversVO.setProducts(products);

        return ResultVOUtils.success(deliversVO);
    }


    //  TODO 更新api
    @GetMapping("/findDatesByGuestId")
    public ResultVO<List<Date>> findDatesByGuestId(
            @RequestParam String guestId
    ){
        List<Date> dates = deliverService.findDatesByGuestId(guestId);
        return ResultVOUtils.success(dates);
    }
}
