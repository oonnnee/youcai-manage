package com.youcai.manage.controller;

import com.youcai.manage.dataobject.Category;
import com.youcai.manage.dataobject.Guest;
import com.youcai.manage.dataobject.Product;
import com.youcai.manage.dto.order.AllDTO;
import com.youcai.manage.enums.OrderEnum;
import com.youcai.manage.service.CategoryService;
import com.youcai.manage.service.GuestService;
import com.youcai.manage.service.OrderService;
import com.youcai.manage.service.ProductService;
import com.youcai.manage.utils.ManageUtils;
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

import java.math.BigDecimal;
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
        Pageable pageable = ManageUtils.getPageable(page, size);

        Page<Guest> guestPage = orderService.findGuestPage(pageable);

        return ResultVOUtils.success(this.getListVOPage(guestPage, pageable));
    }

    @GetMapping("/findPageByGuestIdLike")
    public ResultVO<Page<ListVO>> listByGuestIdLike(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String guestId
    ){
        Pageable pageable = ManageUtils.getPageable(page, size);

        Page<Guest> guestIdPage = orderService.findGuestPageByGuestIdLike(pageable, guestId);

        return ResultVOUtils.success(this.getListVOPage(guestIdPage, pageable));
    }

    @GetMapping("/findPageByGuestNameLike")
    public ResultVO<Page<ListVO>> listByGuestNameLike(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String guestName
    ){
        Pageable pageable = ManageUtils.getPageable(page, size);

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

        return ResultVOUtils.success(dates, "此客户暂无采购");
    }

    // TODO 更新api
    @GetMapping("/findStatesByGuestIdAndDate")
    public ResultVO<List<String>> findStatesByGuestIdAndDate(
            @RequestParam String guestId,
            @RequestParam  @DateTimeFormat(pattern = "yyyy-MM-dd") Date date
    ){
        List<String> states = orderService.findStatesByGuestIdAndDate(guestId, date);

        return ResultVOUtils.success(states, "此客户暂无采购");
    }

    //  TODO 更新api
    @GetMapping("/findOne")
    public ResultVO findOne(
            @RequestParam String guestId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date date,
            @RequestParam String state
    ){
        Guest guest = guestService.findOne(guestId);
        ManageUtils.ManageException(guest, ManageUtils.toErrorString("获取采购单失败", "此客户不存在"));

        List<AllDTO> allDTOS = orderService.findAllWith(guestId, date, state);
        ManageUtils.ManageException(allDTOS, ManageUtils.toErrorString("获取采购单失败", "未能查询到此采购单"));

        OrdersVO ordersVO = new OrdersVO();

        BigDecimal total = BigDecimal.ZERO;

        List<ProductVO> products = new ArrayList<>();
        for (AllDTO e : allDTOS){
            if (!ManageUtils.isZero(e.getProductNum())){
                products.add(
                    new ProductVO(
                        e.getProductId(), e.getProductName(), e.getProductCategory(), e.getProductUnit(),
                        e.getProductPrice(), e.getProductNum(), e.getProductAmount(), e.getProductImgfile(),
                        e.getNote()
                    )
                );
                total = total.add(e.getProductAmount());
            }
        }

        ordersVO.setGuestId(guestId);
        ordersVO.setDate(date);
        ordersVO.setGuestName(guest.getName());
        ordersVO.setState(state);
        ordersVO.setTotal(total);
        ordersVO.setProducts(products);

        return ResultVOUtils.success(ordersVO);
    }

    //  TODO 更新api
    @GetMapping("/findOneWithZero")
    public ResultVO findOneWithZero(
            @RequestParam String guestId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date date
    ){
        String state = OrderEnum.NEW.getState();

        Guest guest = guestService.findOne(guestId);
        ManageUtils.ManageException(guest, ManageUtils.toErrorString("获取采购单失败", "此客户不存在"));

        List<AllDTO> allDTOS = orderService.findAllWith(guestId, date, state);
        ManageUtils.ManageException(allDTOS, ManageUtils.toErrorString("获取采购单失败", "未能查询到此采购单"));

        OrdersVO ordersVO = new OrdersVO();

        List<ProductVO> products = allDTOS.stream().map( e ->
                new ProductVO(
                        e.getProductId(), e.getProductName(), e.getProductCategory(), e.getProductUnit(),
                        e.getProductPrice(), e.getProductNum(), e.getProductAmount(), e.getProductImgfile(),
                        e.getNote()
                )
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
        return ResultVOUtils.success("退回采购单成功");
    }

    @PostMapping("/update")
    public ResultVO update(
            @RequestParam String guestId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date date,
            @RequestParam String products
    ){
        orderService.update(guestId, date, products);
        return ResultVOUtils.success("更新采购单成功");
    }
}


