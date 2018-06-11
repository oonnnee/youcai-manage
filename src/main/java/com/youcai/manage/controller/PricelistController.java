package com.youcai.manage.controller;

import com.youcai.manage.dataobject.*;
import com.youcai.manage.dto.PricelistDTO;
import com.youcai.manage.enums.ResultEnum;
import com.youcai.manage.exception.YoucaiException;
import com.youcai.manage.service.CategoryService;
import com.youcai.manage.service.GuestService;
import com.youcai.manage.service.PricelistService;
import com.youcai.manage.service.ProductService;
import com.youcai.manage.utils.ResultVOUtils;
import com.youcai.manage.utils.comparator.DateComparator;
import com.youcai.manage.vo.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/pricelist")
public class PricelistController {

    @Autowired
    private PricelistService pricelistService;

    @Autowired
    private GuestService guestService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;


    @GetMapping("/findPdateListByGuestIdLike")
    public ResultVO<Page<PricelistDateVO>> findPdateListByGuestIdLike(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String guestId
    ){
        /*------------ 1.准备 -------------*/
        // 分页
        page = page<0 ? 0:page;
        size = size<=0 ? 10:size;
        Pageable pageable = new PageRequest(page, size);

        /*------------ 2.查询 -------------*/
        List<PricelistDateVO> pricelistDateVOS = new ArrayList<>();
        Page<Guest> guestPage = guestService.findByIdLike(guestId, pageable);
        for (Guest guest : guestPage.getContent()){
            if (guest.getId().equals("admin")){
                continue;
            }
            List<Pricelist> pricelists = pricelistService.findById_GuestId(guest.getId());
            Set<Date> dates = new TreeSet<>(new DateComparator());
            for (Pricelist pricelist : pricelists){
                dates.add(pricelist.getId().getPdate());
            }
            PricelistDateVO pricelistVO = new PricelistDateVO(guest.getId(), guest.getName(), dates);
            pricelistDateVOS.add(pricelistVO);
        }

        Page<PricelistDateVO> pricelistDateVOPage = new PageImpl<PricelistDateVO>(pricelistDateVOS, pageable, guestPage.getTotalElements());
        return ResultVOUtils.success(pricelistDateVOPage);
    }

    @GetMapping("/findPdateListByGuestNameLike")
    public ResultVO<Page<PricelistDateVO>> findPdateListByGuestNameLike(
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
        List<PricelistDateVO> pricelistDateVOS = new ArrayList<>();
        Page<Guest> guestPage = guestService.findByNameLike(guestName, pageable);
        for (Guest guest : guestPage.getContent()){
            if (guest.getId().equals("admin")){
                continue;
            }
            List<Pricelist> pricelists = pricelistService.findById_GuestId(guest.getId());
            Set<Date> dates = new TreeSet<>(new DateComparator());
            for (Pricelist pricelist : pricelists){
                dates.add(pricelist.getId().getPdate());
            }
            PricelistDateVO pricelistVO = new PricelistDateVO(guest.getId(), guest.getName(), dates);
            pricelistDateVOS.add(pricelistVO);
        }

        Page<PricelistDateVO> pricelistDateVOPage = new PageImpl<PricelistDateVO>(pricelistDateVOS, pageable, guestPage.getTotalElements());
        return ResultVOUtils.success(pricelistDateVOPage);
    }


    @GetMapping("/pdateList")
    public ResultVO<Page<PricelistDateVO>> pdateList(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ){
        /*------------ 1.准备 -------------*/
        // 分页
        page = page<0 ? 0:page;
        size = size<=0 ? 10:size;
        Pageable pageable = new PageRequest(page, size);
        /*------------ 2.查询 -------------*/
        List<PricelistDateVO> pricelistDateVOS = new ArrayList<>();
        Page<Guest> guestPage = guestService.findAll(pageable);
        for (Guest guest : guestPage.getContent()){
            if (guest.getId().equals("admin")){
                continue;
            }
            List<Pricelist> pricelists = pricelistService.findById_GuestId(guest.getId());
            Set<Date> dates = new TreeSet<>(new DateComparator());
            for (Pricelist pricelist : pricelists){
                dates.add(pricelist.getId().getPdate());
            }
            PricelistDateVO pricelistVO = new PricelistDateVO(guest.getId(), guest.getName(), dates);
            pricelistDateVOS.add(pricelistVO);
        }
        Page<PricelistDateVO> pricelistDateVOPage = new PageImpl<PricelistDateVO>(pricelistDateVOS, pageable, guestPage.getTotalElements());
        return ResultVOUtils.success(pricelistDateVOPage);
    }

    @PostMapping({"/save", "update"})
    public ResultVO save(
            @RequestParam String guestId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date pdate,
            @RequestParam String products
    ){
        List<PricelistDTO> pricelistDTOS = new ArrayList<>();
        try {
            pricelistDTOS = new Gson().fromJson(products,
                    new TypeToken<List<PricelistDTO>>() {
                    }.getType());
        } catch (Exception e) {
            throw new YoucaiException(ResultEnum.MANAGE_PRICELIST_SAVE_PRICES_PARSE_ERROR);
        }
        List<Pricelist> pricelists = pricelistDTOS.stream().map(e ->
                new Pricelist(new PricelistKey(pdate, guestId, e.getProductId()),
                        e.getPrice(), e.getNote())
        ).collect(Collectors.toList());
        pricelistService.save(pricelists);
        return ResultVOUtils.success();
    }

    @PostMapping("/delete")
    public ResultVO delete(
            @RequestParam String guestId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date pdate
    ){
        pricelistService.delete(guestId, pdate);
        return ResultVOUtils.success();
    }

    @GetMapping("/find")
    public ResultVO find(
            @RequestParam String guestId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date pdate
    ){
        List<PricelistDTO> pricelistDTOS = new ArrayList<>();

        List<Pricelist> pricelists = pricelistService.findById_GuestIdAndId_pdate(guestId, pdate);
        for (Pricelist pricelist : pricelists){
            String productId = pricelist.getId().getProductId();
            Product product = productService.findOne(productId);
            PricelistDTO pricelistDTO = new PricelistDTO();
            pricelistDTO.setProductId(productId);
            pricelistDTO.setProductCode(product.getPCode());
            pricelistDTO.setProductName(product.getName());
            pricelistDTO.setProductImg(product.getImgfile());
            pricelistDTO.setProductUnit(product.getUnit());
            pricelistDTO.setPrice(pricelist.getPrice());
            pricelistDTO.setNote(pricelist.getNote());
            pricelistDTOS.add(pricelistDTO);
        }

        PricelistVO pricelistVO = new PricelistVO();

        pricelistVO.setGuestId(guestId);
        pricelistVO.setPdate(pdate);
        pricelistVO.setPricelistDTOS(pricelistDTOS);

        return ResultVOUtils.success(pricelistVO);
    }

    @GetMapping("/findPdatesByGuestId")
    public ResultVO<List<Date>> findPdatesByGuestId(
            @RequestParam String guestId
    ){
        List<Date> dates = pricelistService.findPdatesByGuestId(guestId);
        return ResultVOUtils.success(dates);
    }

    @GetMapping("/findByGuestIdAndPdateWithCategory")
    public ResultVO<List<FindByGuestIdAndPdateWithCategoryVO>> findByGuestIdAndPdateWithCategory(
            @RequestParam String guestId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date pdate
    ){
        /*------------ 1.查询数据 -------------*/
        /*--- 产品大类数据 ---*/
        List<Category> categories = categoryService.findAll();
        /*--- 报价数据 ---*/
        List<Pricelist> pricelists = pricelistService.findById_GuestIdAndId_pdate(guestId, pdate);
        /*--- 产品数据 ---*/
        Map<String, Product> productMap = productService.findMap();
        /*------------ 2.数据拼装 -------------*/
        List<FindByGuestIdAndPdateWithCategoryVO> findByGuestIdAndPdateWithCategoryVOS = new ArrayList<>();
        for (Category category : categories){
            FindByGuestIdAndPdateWithCategoryVO findByGuestIdAndPdateWithCategoryVO = new FindByGuestIdAndPdateWithCategoryVO();
            findByGuestIdAndPdateWithCategoryVO.setCategoryCode(category.getCode());
            findByGuestIdAndPdateWithCategoryVO.setCategoryName(category.getName());
            List<FindByGuestIdAndPdateVO> findByGuestIdAndPdateVOS = new ArrayList<>();
            for (Pricelist pricelist : pricelists){
                Product product = productMap.get(pricelist.getId().getProductId());
                if (product.getPCode().equals(category.getCode())){
                    FindByGuestIdAndPdateVO findByGuestIdAndPdateVO = new FindByGuestIdAndPdateVO();
                    findByGuestIdAndPdateVO.setProductId(pricelist.getId().getProductId());
                    findByGuestIdAndPdateVO.setProductName(product.getName());
                    findByGuestIdAndPdateVO.setPrice(pricelist.getPrice());
                    findByGuestIdAndPdateVO.setNote(pricelist.getNote());
                    findByGuestIdAndPdateVOS.add(findByGuestIdAndPdateVO);
                }
            }
            findByGuestIdAndPdateWithCategoryVO.setFindByGuestIdAndPdateVOS(findByGuestIdAndPdateVOS);
            findByGuestIdAndPdateWithCategoryVOS.add(findByGuestIdAndPdateWithCategoryVO);
        }
        /*------------ 3.返回 -------------*/
        return ResultVOUtils.success(findByGuestIdAndPdateWithCategoryVOS);
    }

//    @GetMapping("/findFullByGuestIdAndPdateWithCategory")
//    public ResultVO<List<FindByGuestIdAndPdateWithCategoryVO>> findFullByGuestIdAndPdateWithCategory(
//            @RequestParam String guestId,
//            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date pdate
//    ){
//        /*------------ 1.查询数据 -------------*/
//        /*--- 产品大类数据 ---*/
//        List<Category> categories = categoryService.findAll();
//        /*--- 报价数据 ---*/
//        List<Pricelist> pricelists = pricelistService.findById_GuestIdAndId_pdate(guestId, pdate);
//        Map<String, Pricelist> pricelistMap = pricelistService.findProductIdMap(guestId, pdate);
//        /*--- 产品数据 ---*/
//        Map<String, Product> productMap = productService.findMap();
//        List<Product> products = productService.findAll();
//        /*------------ 2.数据拼装 -------------*/
//        List<FindByGuestIdAndPdateWithCategoryVO> findByGuestIdAndPdateWithCategoryVOS = new ArrayList<>();
//        for (Category category : categories){
//            FindByGuestIdAndPdateWithCategoryVO findByGuestIdAndPdateWithCategoryVO = new FindByGuestIdAndPdateWithCategoryVO();
//            findByGuestIdAndPdateWithCategoryVO.setCategoryCode(category.getCode());
//            findByGuestIdAndPdateWithCategoryVO.setCategoryName(category.getName());
//            List<FindByGuestIdAndPdateVO> findByGuestIdAndPdateVOS = new ArrayList<>();
//            for (Product product : products){
//                if (product.getPCode().equals(category.getCode())){
//                    FindByGuestIdAndPdateVO findByGuestIdAndPdateVO = new FindByGuestIdAndPdateVO();
//                    findByGuestIdAndPdateVO.setProductId(product.getId());
//                    findByGuestIdAndPdateVO.setProductName(product.getName());
//                    findByGuestIdAndPdateVO.setPrice(BigDecimal.ZERO);
//                    findByGuestIdAndPdateVO.setNote("");
//                    if (pricelistMap.containsKey(product.getId())){
//                        findByGuestIdAndPdateVO.setPrice(pricelistMap.get(product.getId()).getPrice());
//                        findByGuestIdAndPdateVO.setNote(pricelistMap.get(product.getId()).getNote());
//                    }
//                    findByGuestIdAndPdateVOS.add(findByGuestIdAndPdateVO);
//                }
//
//            }
//            findByGuestIdAndPdateWithCategoryVO.setFindByGuestIdAndPdateVOS(findByGuestIdAndPdateVOS);
//            findByGuestIdAndPdateWithCategoryVOS.add(findByGuestIdAndPdateWithCategoryVO);
//        }
//        /*------------ 3.返回 -------------*/
//        return ResultVOUtils.success(findByGuestIdAndPdateWithCategoryVOS);
//    }
}
