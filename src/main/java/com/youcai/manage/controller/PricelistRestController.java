package com.youcai.manage.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.youcai.manage.dataobject.Guest;
import com.youcai.manage.dataobject.Pricelist;
import com.youcai.manage.dataobject.PricelistKey;
import com.youcai.manage.dto.pricelist.AllDTO;
import com.youcai.manage.dto.pricelist.ProductDTO;
import com.youcai.manage.exception.ManageException;
import com.youcai.manage.service.CategoryService;
import com.youcai.manage.service.GuestService;
import com.youcai.manage.service.PricelistService;
import com.youcai.manage.service.ProductService;
import com.youcai.manage.utils.ManageUtils;
import com.youcai.manage.utils.ResultVOUtils;
import com.youcai.manage.utils.comparator.DateComparator;
import com.youcai.manage.vo.PricelistDateVO;
import com.youcai.manage.vo.ResultVO;
import com.youcai.manage.vo.pricelist.PricelistsVO;
import com.youcai.manage.vo.pricelist.ProductVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/pricelist")
public class PricelistRestController {

    @Autowired
    private PricelistService pricelistService;

    @Autowired
    private GuestService guestService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;


    @GetMapping("/findPageByGuestIdLike")
    public ResultVO<Page<PricelistDateVO>> listByGuestIdLike(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String guestId
    ) {
        Pageable pageable = ManageUtils.getPageable(page, size);

        Page<Guest> guestPage = guestService.findByIdLike(guestId, pageable);

        return ResultVOUtils.success(this.getPricelistDateVO(guestPage, pageable));
    }

    @GetMapping("/findPageByGuestNameLike")
    public ResultVO<Page<PricelistDateVO>> listByGuestNameLike(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String guestName
    ) {
        Pageable pageable = ManageUtils.getPageable(page, size);

        Page<Guest> guestPage = guestService.findByNameLike(guestName, pageable);

        return ResultVOUtils.success(this.getPricelistDateVO(guestPage, pageable));
    }


    @GetMapping("/findPage")
    public ResultVO<Page<PricelistDateVO>> list(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        Pageable pageable = ManageUtils.getPageable(page, size);

        Page<Guest> guestPage = guestService.findAll(pageable);

        return ResultVOUtils.success(this.getPricelistDateVO(guestPage, pageable));
    }

    private Page<PricelistDateVO> getPricelistDateVO(Page<Guest> guestPage, Pageable pageable) {
        List<PricelistDateVO> pricelistDateVOS = new ArrayList<>();
        for (Guest guest : guestPage.getContent()) {
            if (guest.getId().equals("admin")) {
                continue;
            }
            List<Pricelist> pricelists = pricelistService.findById_GuestId(guest.getId());
            Set<Date> dates = new TreeSet<>(new DateComparator());
            for (Pricelist pricelist : pricelists) {
                dates.add(pricelist.getId().getPdate());
            }
            PricelistDateVO pricelistVO = new PricelistDateVO(guest.getId(), guest.getName(), dates);
            pricelistDateVOS.add(pricelistVO);
        }
        Page<PricelistDateVO> pricelistDateVOPage = new PageImpl<PricelistDateVO>(pricelistDateVOS, pageable, guestPage.getTotalElements());
        return pricelistDateVOPage;
    }


    @PostMapping("/save")
    public ResultVO save(
            @RequestParam String guestId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date date,
            @RequestParam String products
    ) {
        List<ProductDTO> productDTOS;
        try {
            productDTOS = new Gson().fromJson(products,
                    new TypeToken<List<ProductDTO>>() {
                    }.getType());
        } catch (Exception e) {
            throw new ManageException(ManageUtils.toErrorString("新增报价失败", "产品参数错误"));
        }

        ManageUtils.ManageException(
                CollectionUtils.isEmpty(productDTOS),
                ManageUtils.toErrorString("新增报价失败", "报价产品为空")
        );

        List<Pricelist> pricelists = productDTOS.stream().map(e ->
                new Pricelist(new PricelistKey(date, guestId, e.getId()),
                        e.getPrice(), e.getNote())
        ).collect(Collectors.toList());

        pricelistService.save(pricelists);

        return ResultVOUtils.success("新增报价成功");
    }

    @PostMapping("/update")
    public ResultVO update(
            @RequestParam String guestId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date date,
            @RequestParam String products
    ) {
        List<ProductDTO> productDTOS;
        try {
            productDTOS = new Gson().fromJson(products,
                    new TypeToken<List<ProductDTO>>() {
                    }.getType());
        } catch (Exception e) {
            throw new ManageException(ManageUtils.toErrorString("更新报价失败", "产品参数错误"));
        }

        ManageUtils.ManageException(
                CollectionUtils.isEmpty(productDTOS),
                ManageUtils.toErrorString("更新报价失败", "报价产品为空")
        );

        List<Pricelist> pricelists = productDTOS.stream().map(e ->
                new Pricelist(new PricelistKey(date, guestId, e.getId()),
                        e.getPrice(), e.getNote())
        ).collect(Collectors.toList());

        pricelistService.update(pricelists);

        return ResultVOUtils.success("更新报价成功");
    }

    @PostMapping("/delete")
    public ResultVO delete(
            @RequestParam String guestId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date date
    ) {
        pricelistService.delete(guestId, date);

        return ResultVOUtils.success("删除报价成功");
    }

    @GetMapping("/findDatesByGuestId")
    public ResultVO<List<Date>> findDatesByGuestId(
            @RequestParam String guestId
    ) {
        List<Date> dates = pricelistService.findPdatesByGuestId(guestId);

        return ResultVOUtils.success(dates, "此客户暂无报价单");
    }

    @GetMapping("/findOne")
    public ResultVO findOne(
            @RequestParam String guestId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date date
    ) {
        Guest guest = guestService.findOne(guestId);
        ManageUtils.ManageException(guest, ManageUtils.toErrorString("获取报价单失败", "此客户不存在"));

        List<AllDTO> allDTOS = pricelistService.findAllWith(guestId, date);
        ManageUtils.ManageException(allDTOS, ManageUtils.toErrorString("获取报价单失败", "此客户当天不存在报价"));

        PricelistsVO pricelistsVO = new PricelistsVO();

        List<ProductVO> products = allDTOS.stream().map(e ->
                new ProductVO(
                        e.getProductId(), e.getProductName(), e.getProductCategory(), e.getProductUnit(),
                        e.getProductMarketPrice(), e.getProductGuestPrice(), e.getProductImgfile(), e.getNote()
                )
        ).collect(Collectors.toList());

        pricelistsVO.setGuestId(guestId);
        pricelistsVO.setDate(date);
        pricelistsVO.setGuestName(guest.getName());
        pricelistsVO.setProducts(products);

        return ResultVOUtils.success(pricelistsVO);
    }
}

//    @GetMapping("/findOne")
//    public ResultVO findOne(
//            @RequestParam String guestId,
//            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date date
//    ) {
//        Guest guest = guestService.findOne(guestId);
//        ManageUtils.ManageException(guest, ManageUtils.toErrorString("获取报价单失败", "此客户不存在"));
//
//        List<Pricelist> pricelists = pricelistService.findById_GuestIdAndId_pdate(guestId, date);
//        ManageUtils.ManageException(pricelists, ManageUtils.toErrorString("获取报价单失败", "此客户当天不存在报价"));
//
//        Map<String, Product> productMap = productService.findMap();
//
//        PricelistsVO pricelistsVO = new PricelistsVO();
//
//        List<ProductVO> products = pricelists.stream().map(e ->
//                new ProductVO(
//                        e.getId().getProductId(), productMap.get(e.getId().getProductId()).getName(),
//                        e.getPrice(), e.getNote()
//                )
//        ).collect(Collectors.toList());
//
//        pricelistsVO.setGuestId(guestId);
//        pricelistsVO.setDate(date);
//        pricelistsVO.setGuestName(guest.getName());
//        pricelistsVO.setProducts(products);
//
//        return ResultVOUtils.success(pricelistsVO);
//    }

//    @GetMapping("/findOneWithCategories")
//    public ResultVO<List<FindByGuestIdAndPdateWithCategoryVO>> findCategories(
//            @RequestParam String guestId,
//            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date date
//    ){
//        List<Category> categories = categoryService.findAll();
//        List<Pricelist> pricelists = pricelistService.findById_GuestIdAndId_pdate(guestId, date);
//        Map<String, Product> productMap = productService.findMap();
//
//        List<FindByGuestIdAndPdateWithCategoryVO> findByGuestIdAndPdateWithCategoryVOS = new ArrayList<>();
//        for (Category category : categories){
//            FindByGuestIdAndPdateWithCategoryVO findByGuestIdAndPdateWithCategoryVO = new FindByGuestIdAndPdateWithCategoryVO();
//            findByGuestIdAndPdateWithCategoryVO.setCode(category.getCode());
//            findByGuestIdAndPdateWithCategoryVO.setName(category.getName());
//            List<FindByGuestIdAndPdateVO> findByGuestIdAndPdateVOS = new ArrayList<>();
//            for (Pricelist pricelist : pricelists){
//                Product product = productMap.get(pricelist.getId().getProductId());
//                if (product.getPCode().equals(category.getCode())){
//                    FindByGuestIdAndPdateVO findByGuestIdAndPdateVO = new FindByGuestIdAndPdateVO();
//                    findByGuestIdAndPdateVO.setProductId(pricelist.getId().getProductId());
//                    findByGuestIdAndPdateVO.setProductName(product.getName());
//                    findByGuestIdAndPdateVO.setPrice(pricelist.getPrice());
//                    findByGuestIdAndPdateVO.setNote(pricelist.getNote());
//                    findByGuestIdAndPdateVOS.add(findByGuestIdAndPdateVO);
//                }
//            }
//            findByGuestIdAndPdateWithCategoryVO.setFindByGuestIdAndPdateVOS(findByGuestIdAndPdateVOS);
//            findByGuestIdAndPdateWithCategoryVOS.add(findByGuestIdAndPdateWithCategoryVO);
//        }
//
//        return ResultVOUtils.success(findByGuestIdAndPdateWithCategoryVOS);
//    }
