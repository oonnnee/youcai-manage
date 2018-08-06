package com.youcai.manage.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.youcai.manage.dataobject.Deliver;
import com.youcai.manage.dataobject.DeliverKey;
import com.youcai.manage.dataobject.Guest;
import com.youcai.manage.dto.deliver.AllDTO;
import com.youcai.manage.dto.deliver.ProductDTO;
import com.youcai.manage.exception.ManageException;
import com.youcai.manage.service.*;
import com.youcai.manage.utils.DeliverUtils;
import com.youcai.manage.utils.ManageUtils;
import com.youcai.manage.utils.ResultVOUtils;
import com.youcai.manage.vo.ResultVO;
import com.youcai.manage.vo.deliver.DeliversVO;
import com.youcai.manage.vo.deliver.ListVO;
import com.youcai.manage.vo.deliver.ProductVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
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
        Pageable pageable = ManageUtils.getPageable(page, size);

        List<ListVO> listVOS = deliverService.findList();

        if (!StringUtils.isEmpty(guestName)){
            Iterator<ListVO> it = listVOS.iterator();
            while(it.hasNext()){
                ListVO listVO = it.next();
                if(!listVO.getGuestName().contains(guestName)){
                    it.remove();
                }
            }
        }


        Page<ListVO> listVOPage = new PageImpl<ListVO>(listVOS, pageable, listVOS.size());
        return ResultVOUtils.success(listVOPage);
    }

    @PostMapping("/save")
    public ResultVO save(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date orderDate,
            @RequestParam String guestId,
            @RequestParam Integer driverId,
            @RequestParam String products
    ){
        List<ProductDTO> productDTOS;
        try {
            productDTOS = new Gson().fromJson(products,
                    new TypeToken<List<ProductDTO>>() {
                    }.getType());
        } catch (Exception e) {
            throw new ManageException(ManageUtils.toErrorString("新增送货单失败", "产品参数错误"));
        }

        List<Deliver> delivers = productDTOS.stream().map(e ->
                new Deliver(new DeliverKey(driverId, guestId, new Date(), e.getId(), DeliverUtils.getStateDelivering(), orderDate),
                        e.getPrice(), e.getNum(), e.getPrice().multiply(e.getNum()), e.getNote())
        ).collect(Collectors.toList());

        deliverService.save(delivers, guestId, orderDate);

        return ResultVOUtils.success("新增送货单成功");
    }

    //  TODO 更新api
    @GetMapping("/findOne")
    public ResultVO findOne(
            @RequestParam String guestId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date date
    ){
        Guest guest = guestService.findOne(guestId);
        ManageUtils.ManageException(guest, ManageUtils.toErrorString("获取送货单失败", "此客户不存在"));

        List<AllDTO> allDTOS = deliverService.findAllWith(guestId, date);
        ManageUtils.ManageException(allDTOS, ManageUtils.toErrorString("获取采购单失败", "未能查询到此送货单"));

        ManageUtils.ManageException(allDTOS.get(0).getDriverId(), ManageUtils.toErrorString("获取送货单失败", "送货司机不存在"));

        DeliversVO deliversVO = new DeliversVO();

        BigDecimal total = BigDecimal.ZERO;

        List<ProductVO> products = new ArrayList<>();
        for (AllDTO e : allDTOS){
            products.add(new ProductVO(
                    e.getProductId(), e.getProductName(), e.getProductCategory(), e.getProductUnit(),
                    e.getProductPrice(), e.getProductNum(), e.getProductAmount(), e.getProductImgfile(),
                    e.getNote()
            ));
            total = total.add(e.getProductAmount());
        }

        deliversVO.setGuestId(guestId);
        deliversVO.setGuestName(guest.getName());
        deliversVO.setDriverId(allDTOS.get(0).getDriverId());
        deliversVO.setDriverName(allDTOS.get(0).getDriverName());
        deliversVO.setDeliverDate(allDTOS.get(0).getDeliverDate());
        deliversVO.setOrderDate(allDTOS.get(0).getOrderDate());
        deliversVO.setState(allDTOS.get(0).getState());
        deliversVO.setTotal(total);
        deliversVO.setProducts(products);

        return ResultVOUtils.success(deliversVO);
    }


    //  TODO 更新api
    @GetMapping("/findDatesByGuestId")
    public ResultVO<List<Date>> findDatesByGuestId(
            @RequestParam String guestId
    ){
        List<Date> dates = deliverService.findDatesByGuestId(guestId);

        return ResultVOUtils.success(dates, "此客户暂无送货单");
    }

//    @GetMapping("/findOne")
//    public ResultVO findOne(
//            @RequestParam String guestId,
//            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date date
//    ){
//        Guest guest = guestService.findOne(guestId);
//        ManageUtils.ManageException(guest, ManageUtils.toErrorString("获取送货单失败", "此客户不存在"));
//
//        List<Deliver> delivers = deliverService.findByGuestIdAndDate(guestId, date);
//        ManageUtils.ManageException(delivers, ManageUtils.toErrorString("获取采购单失败", "未能查询到此送货单"));
//
//        Driver driver = driverService.findOne(delivers.get(0).getId().getDriverId());
//        ManageUtils.ManageException(guest, ManageUtils.toErrorString("获取送货单失败", "送货司机不存在"));
//
//        Map<String, Product> productMap = productService.findMap();
//
//        DeliversVO deliversVO = new DeliversVO();
//
//        List<ProductVO> products = delivers.stream().map( e -> {
//                    Product product = productMap.get(e.getId().getProductId());
//                    return new ProductVO(
//                            product.getId(), product.getName(), product.getUnit(),
//                            e.getPrice(), e.getNum(), e.getAmount(), e.getNote()
//                    );
//                }
//        ).collect(Collectors.toList());
//
//        deliversVO.setGuestId(guestId);
//        deliversVO.setGuestName(guest.getName());
//        deliversVO.setDriverId(driver.getId());
//        deliversVO.setDriverName(driver.getName());
//        deliversVO.setDate(date);
//        deliversVO.setState(delivers.get(0).getId().getState());
//        deliversVO.setProducts(products);
//
//        return ResultVOUtils.success(deliversVO);
//    }

//    @GetMapping("/findOneWithCategories")
//    public ResultVO<List<CategoryVO>> findCategories(
//            @RequestParam String guestId,
//            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date date
//    ){
//        List<Category> categories = categoryService.findAll();
//        List<Deliver> delivers = deliverService.findByGuestIdAndDate(guestId, date);
//        /*--- 产品数据 ---*/
//        Map<String, Product> productMap = productService.findMap();
//        /*------------ 2.数据拼装 -------------*/
//        List<CategoryVO> categoryVOS = new ArrayList<>();
//        for (Category category : categories){
//            CategoryVO categoryVO = new CategoryVO();
//            categoryVO.setCode(category.getCode());
//            categoryVO.setName(category.getName());
//            List<ProductVO> productVOS = new ArrayList<>();
//            for (Deliver deliver : delivers){
//                Product product = productMap.get(deliver.getId().getProductId());
//                if (product.getPCode().equals(category.getCode())){
//                    ProductVO productVO = new ProductVO();
//                    productVO.setId(deliver.getId().getProductId());
//                    productVO.setName(product.getName());
//                    productVO.setUnit(product.getUnit());
//                    productVO.setPrice(deliver.getPrice());
//                    productVO.setNum(deliver.getNum());
//                    productVO.setAmount(deliver.getAmount());
//                    productVO.setNote(deliver.getNote());
//                    productVOS.add(productVO);
//                }
//            }
//            categoryVO.setProducts(productVOS);
//            categoryVOS.add(categoryVO);
//        }
//        /*------------ 3.返回 -------------*/
//        return ResultVOUtils.success(categoryVOS);
//    }

//    @PostMapping("/delete")
//    public ResultVO delete(
//            @RequestParam String guestId,
//            @RequestParam Integer driverId,
//            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date date
//    ){
//        deliverService.delete(guestId, driverId, date);
//        return ResultVOUtils.success();
//    }
}
