package com.youcai.manage.service.impl;

import com.youcai.manage.dataobject.Deliver;
import com.youcai.manage.dataobject.Guest;
import com.youcai.manage.dataobject.Product;
import com.youcai.manage.dto.excel.deliver.Export;
import com.youcai.manage.dto.excel.deliver.ProductExport;
import com.youcai.manage.enums.OrderEnum;
import com.youcai.manage.repository.DeliverRepository;
import com.youcai.manage.service.*;
import com.youcai.manage.vo.deliver.ListVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.*;

@Service
public class DeliverServiceImpl implements DeliverService {

    @Autowired
    private DeliverRepository deliverRepository;
    @Autowired
    private GuestService guestService;
    @Autowired
    private DriverService driverService;
    @Autowired
    private ProductService productService;
    @Autowired
    private OrderService orderService;

    @Override
    public List<Deliver> findByIdGuestId(String guestId) {
        return deliverRepository.findByIdGuestId(guestId);
    }

    @Override
    public List<Deliver> findByGuestIdAndDate(String guestId, Date date) {
        return deliverRepository.findByIdGuestIdAndIdDdate(guestId, date);
    }

//    @Override
//    @Transactional
//    public void delete(String guestId, Integer driverId, Date date) {
//        deliverRepository.delete(guestId, driverId, date);
//    }

    @Override
    @Transactional
    public void save(List<Deliver> delivers, String orderGuestId, Date orderDate) {
        orderService.updateState(orderGuestId, orderDate, OrderEnum.NEW.getState(), OrderEnum.DELIVERED.getState());
        deliverRepository.save(delivers);
    }

    @Override
    public Export getExcelExport(String guestId, Date date) {
        Export export = new Export();

        List<ProductExport> productExports = new ArrayList<>();
        BigDecimal amount = BigDecimal.ZERO;
        List<Deliver> delivers = this.findByGuestIdAndDate(guestId, date);
        Map<String, Product> productMap = productService.findMap();
        int index = 1;
        for (Deliver deliver : delivers){
            /*--- 产品 ---*/
            Product product = productMap.get(deliver.getId().getProductId());
            ProductExport productExport = new ProductExport();
            productExport.setIndex(index++);
            productExport.setName(product.getName());
            productExport.setNum(deliver.getNum());
            productExport.setUnit(product.getUnit());
            productExport.setPrice(deliver.getPrice());
            productExport.setAmount(deliver.getAmount());
            productExport.setNote(deliver.getNote());
            productExports.add(productExport);
            /*--- 送货单金额 ---*/
            amount = amount.add(deliver.getAmount());
        }

        export.setGuestName(guestService.findOne(guestId).getName());
        export.setDriver(driverService.findOne(delivers.get(0).getId().getDriverId()));
        export.setDate(date);
        export.setProductExports(productExports);
        export.setAmount(amount);
        return export;
    }

    @Override
    public Page<Guest> findGuestPage(Pageable pageable, String guestName) {
        List<String> guestIds = deliverRepository.findDistinctGuestId();
        if (guestName == null){
            return guestService.findByIdIn(guestIds, pageable);
        } else {
            return guestService.findByIdInAndNameLike(guestIds, guestName, pageable);
        }
    }

    @Override
    public List<Date> findDatesByGuestId(String guestId) {
        List<Date> dates = deliverRepository.findDistinctDateByGuestId(guestId);
        return dates;
    }

}
