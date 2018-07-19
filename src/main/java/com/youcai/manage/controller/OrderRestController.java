package com.youcai.manage.controller;

import com.youcai.manage.dataobject.Category;
import com.youcai.manage.dataobject.Guest;
import com.youcai.manage.dataobject.Product;
import com.youcai.manage.enums.OrderEnum;
import com.youcai.manage.service.CategoryService;
import com.youcai.manage.service.GuestService;
import com.youcai.manage.service.OrderService;
import com.youcai.manage.service.ProductService;
import com.youcai.manage.utils.OrderUtils;
import com.youcai.manage.utils.ResultVOUtils;
import com.youcai.manage.utils.comparator.DateComparator;
import com.youcai.manage.vo.ResultVO;
import com.youcai.manage.vo.order.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import com.youcai.manage.dataobject.Order;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/order")
public class OrderRestController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private GuestService guestService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductService productService;
    
    @GetMapping("/findPage")
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
        Page<Guest> guestPage = orderService.findGuestPage(pageable);
        return ResultVOUtils.success(this.getListVOPage(guestPage, pageable));
    }

    @GetMapping("/findPageByGuestIdLike")
    public ResultVO<Page<ListVO>> listByGuestIdLike(
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
        Page<Guest> guestIdPage = orderService.findGuestPageByGuestIdLike(pageable, guestId);
        return ResultVOUtils.success(this.getListVOPage(guestIdPage, pageable));
    }

    @GetMapping("/findPageByGuestNameLike")
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
        Page<Guest> guestIdPage = orderService.findGuestPageByGuestNameLike(pageable, guestName);
        return ResultVOUtils.success(this.getListVOPage(guestIdPage, pageable));
    }

    private Page<ListVO> getListVOPage(Page<Guest> guestPage, Pageable pageable){
        List<ListVO> listVOS = new ArrayList<>();
        for (Guest guest : guestPage.getContent()){
            List<Order> orders = orderService.findByIdGuestId(guest.getId());
            Set<Date> dates = new TreeSet<>(new DateComparator());
            for (Order order : orders){
                dates.add(order.getId().getOdate());
            }
            ListVO listVO = new ListVO(guest.getId(), guest.getName(), dates);
            listVOS.add(listVO);
        }
        Page<ListVO> listVOPage = new PageImpl<ListVO>(listVOS, pageable, guestPage.getTotalElements());
        return listVOPage;
    }

    @GetMapping("/findDatesByGuestId")
    public ResultVO<List<Date>> findDatesByGuestId(
            @RequestParam String guestId
    ){
        List<Date> dates = orderService.findDatesByGuestId(guestId);
        return ResultVOUtils.success(dates);
    }

    // TODO 更新api
    @GetMapping("/findStatesByGuestIdAndDate")
    public ResultVO<List<String>> findStatesByGuestIdAndDate(
            @RequestParam String guestId,
            @RequestParam  @DateTimeFormat(pattern = "yyyy-MM-dd") Date date
    ){
        List<String> states = orderService.findStatesByGuestIdAndDate(guestId, date);
        return ResultVOUtils.success(states);
    }

    // TODO 更新api
    @GetMapping("/findOneWithCategories")
    public ResultVO<List<CategoryVO>> findCategories(
            @RequestParam String guestId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date date,
            @RequestParam String state
    ){
        /*------------ 1.查询数据 -------------*/
        /*--- 产品大类数据 ---*/
        List<Category> categories = categoryService.findAll();
        /*--- 采购数据 ---*/
        List<Order> orders = orderService.findByIdGuestIdAndIdDateAndIdState(guestId, date, state);
        /*--- 产品数据 ---*/
        Map<String, Product> productMap = productService.findMap();
        /*------------ 2.数据拼装 -------------*/
        List<CategoryVO> categoryVOS = new ArrayList<>();
        for (Category category : categories){
            CategoryVO categoryVO = new CategoryVO();
            categoryVO.setCode(category.getCode());
            categoryVO.setName(category.getName());
            List<ProductVO> productVOS = new ArrayList<>();
            for (Order order : orders){
                Product product = productMap.get(order.getId().getProductId());
                if (product.getPCode().equals(category.getCode())){
                    ProductVO productVO = new ProductVO();
                    productVO.setId(order.getId().getProductId());
                    productVO.setName(product.getName());
                    productVO.setUnit(product.getUnit());
                    productVO.setPrice(order.getPrice());
                    productVO.setNum(order.getNum());
                    productVO.setAmount(order.getAmount());
                    productVO.setNote(order.getNote());
                    productVOS.add(productVO);
                }
            }
            categoryVO.setProducts(productVOS);
            categoryVOS.add(categoryVO);
        }
        /*------------ 3.返回 -------------*/
        return ResultVOUtils.success(categoryVOS);
    }

    //  TODO 更新api
    @GetMapping("/findOne")
    public ResultVO findOne(
            @RequestParam String guestId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date date,
            @RequestParam String state
    ){
        Guest guest = guestService.findOne(guestId);
        if (guest == null){
            return ResultVOUtils.error("未查询到此客户");
        }
        List<Order> orders = orderService.findByIdGuestIdAndIdDateAndIdState(guestId, date, state);
        if (CollectionUtils.isEmpty(orders)){
            return ResultVOUtils.error("未查询到此采购单");
        }
        Map<String, Product> productMap = productService.findMap();

        OrdersVO ordersVO = new OrdersVO();

        List<ProductVO> products = orders.stream().map( e -> {
                Product product = productMap.get(e.getId().getProductId());
                return new ProductVO(
                        product.getId(), product.getName(), product.getUnit(),
                        e.getPrice(), e.getNum(), e.getAmount(), e.getNote()
                );
            }
        ).collect(Collectors.toList());

        ordersVO.setGuestId(guestId);
        ordersVO.setDate(date);
        ordersVO.setGuestName(guest.getName());
        ordersVO.setState(state);
        ordersVO.setProducts(products);

        return ResultVOUtils.success(ordersVO);
    }

    // TODO update api
    @GetMapping("countByState")
    public ResultVO countByState(@RequestParam String state){
        Long count = orderService.countByState(state);
        return ResultVOUtils.success(count);
    }

    // TODO update api
    @GetMapping("findPendingList")
    public ResultVO findPendingList(
            @RequestParam(required = false) String state
    ){
        if (state == null){
            state = OrderEnum.PENDING.getState();
        }
        List<PendingVO> pendingVOS = orderService.findPendingList(state);
        return ResultVOUtils.success(pendingVOS);
    }

    // TODO update api
    @PostMapping("/back")
    public ResultVO back(
            @RequestParam String guestId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date date,
            @RequestParam String state
    ){
        orderService.updateState(guestId, date, state, OrderUtils.getStateBacked());
        return ResultVOUtils.success();
    }
}
