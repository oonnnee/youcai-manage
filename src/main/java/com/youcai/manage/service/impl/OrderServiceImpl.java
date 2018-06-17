package com.youcai.manage.service.impl;

import com.youcai.manage.dataobject.Guest;
import com.youcai.manage.dataobject.Order;
import com.youcai.manage.dataobject.Product;
import com.youcai.manage.dto.excel.order.Export;
import com.youcai.manage.dto.excel.order.ProductExport;
import com.youcai.manage.repository.OrderRepository;
import com.youcai.manage.service.GuestService;
import com.youcai.manage.service.OrderService;
import com.youcai.manage.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private GuestService guestService;
    @Autowired
    private ProductService productService;

    @Override
    public Page<Order> findAll(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }

    @Override
    public List<Order> findByIdGuestId(String guestId) {
        return orderRepository.findByIdGuestId(guestId);
    }

    @Override
    public List<Date> findDatesByGuestId(String guestId) {
        List<Date> dates = orderRepository.findDistinctIdOdateByIdGuestId(guestId);
        return dates;
    }

    @Override
    public List<Order> findByIdGuestIdAndIdDate(String guestId, Date date) {
        return orderRepository.findByIdGuestIdAndIdOdate(guestId, date);
    }

    @Override
    @Transactional
    public void delete(String guestId, Date date) {
        orderRepository.deleteByIdGuestIdAndIdOdate(guestId, date);
    }

    @Override
    public Page<Guest> findGuestPage(Pageable pageable) {
        List<String> guestIds = orderRepository.findDistinctIdGuestId();
        return guestService.findByIdIn(guestIds, pageable);
    }

    @Override
    public Page<Guest> findGuestPageByGuestIdLike(Pageable pageable, String guestId) {
        List<String> guestIds = orderRepository.findDistinctIdGuestId("%"+guestId+"%");
        return guestService.findByIdIn(guestIds, pageable);
    }

    @Override
    public Page<Guest> findGuestPageByGuestNameLike(Pageable pageable, String guestName) {
        List<String> guestIds = orderRepository.findDistinctIdGuestId();
        return guestService.findByIdInAndNameLike(guestIds, guestName, pageable);
    }

    @Override
    public Export getExcelExport(String guestId, Date date) {
        Export export = new Export();
        /*------------ 客户名 -------------*/
        export.setGuestName(guestService.findOne(guestId).getName());
        /*------------ 日期 -------------*/
        export.setDate(date);
        /*------------ 产品&采购单金额 -------------*/
        List<ProductExport> productExports = new ArrayList<>();
        BigDecimal amount = BigDecimal.ZERO;
        List<Order> orders = this.findByIdGuestIdAndIdDate(guestId, date);
        Map<String, Product> productMap = productService.findMap();
        int index = 1;
        for (Order order : orders){
            /*--- 产品 ---*/
            Product product = productMap.get(order.getId().getProductId());
            ProductExport productExport = new ProductExport();
            productExport.setIndex(index++);
            productExport.setName(product.getName());
            productExport.setNum(order.getNum());
            productExport.setUnit(product.getUnit());
            productExport.setPrice(order.getPrice());
            productExport.setAmount(order.getAmount());
            productExport.setNote(order.getNote());
            productExports.add(productExport);
            /*--- 采购单金额 ---*/
            amount = amount.add(order.getAmount());
        }
        /*--- 产品 ---*/
        export.setProductExports(productExports);
        /*--- 采购单金额 ---*/
        export.setAmount(amount);
        return export;
    }
}
