package com.youcai.manage.service.impl;

import com.youcai.manage.dataobject.Deliver;
import com.youcai.manage.dataobject.Guest;
import com.youcai.manage.dataobject.Product;
import com.youcai.manage.dto.deliver.AllDTO;
import com.youcai.manage.dto.excel.deliver.Export;
import com.youcai.manage.dto.excel.deliver.ProductExport;
import com.youcai.manage.enums.DeliverEnum;
import com.youcai.manage.enums.OrderEnum;
import com.youcai.manage.repository.DeliverRepository;
import com.youcai.manage.service.*;
import com.youcai.manage.transform.DeliverTransform;
import com.youcai.manage.utils.ManageUtils;
import com.youcai.manage.utils.comparator.DateComparator;
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
        return deliverRepository.findByIdGuestIdAndIdOrderDate(guestId, date);
    }

    @Override
    @Transactional
    public void save(List<Deliver> delivers, String orderGuestId, Date orderDate) {
        List<String> states = orderService.findStatesByGuestIdAndDate(orderGuestId, orderDate);
        ManageUtils.ManageException(
                !states.contains(OrderEnum.NEW.getState()),
                ManageUtils.toErrorString("创建送货单失败", "采购单状态异常")
        );

        orderService.updateState(orderGuestId, orderDate, OrderEnum.NEW.getState(), OrderEnum.DELIVERED.getState());

        List<Deliver> saveResult = deliverRepository.save(delivers);
        ManageUtils.ManageException(saveResult, ManageUtils.toErrorString("创建送货单失败", "服务器繁忙，请稍后再试"));
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
        export.setOrderDate(delivers.get(0).getId().getOrderDate());
        export.setDeliverDate(delivers.get(0).getId().getDdate());
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

    @Override
    public boolean isGuestExist(String guestId) {
        return deliverRepository.find(guestId) != null;
    }
    @Override
    public boolean isProductExist(String productId) {
        return deliverRepository.findWithProductId(productId) != null;
    }
    @Override
    public boolean isDriverExist(Integer driverId) {
        return deliverRepository.findWithDriverId(driverId) != null;
    }

    @Override
    public List<AllDTO> findAllWith(String guestId, Date date) {
        List<Object[]> objectss = deliverRepository.findAllWith(guestId, date);

        List<AllDTO> allDTOS = DeliverTransform.objectssToAllDTOS(objectss);

        return allDTOS;
    }

    @Override
    public boolean isDriverIdle(Integer driverId) {
        return deliverRepository.findWithDriverIdAndState(driverId, DeliverEnum.DELIVERING.getState()) == null;
    }

    @Override
    public List<ListVO> findList() {
        List<Object[]> objectss = deliverRepository.findDistinctDate();
        ManageUtils.HintException(objectss, "暂无送货单");
        List<ListVO> listVOS = DeliverTransform.objectssToListVOS(objectss);

        Map<String, TreeSet<Date>> map = new HashMap<>();

        for (ListVO listVO : listVOS){
            TreeSet<Date> dateSet = map.get(listVO.getGuestId());

            if (dateSet == null){
                dateSet = new TreeSet<>(new DateComparator());
                map.put(listVO.getGuestId(), dateSet);
            }

            dateSet.add(listVO.getOrderDate());
        }

        Iterator<ListVO> it = listVOS.iterator();
        while(it.hasNext()){
            ListVO listVO = it.next();
            TreeSet<Date> dateSet = map.get(listVO.getGuestId());
            if(!listVO.getOrderDate().equals(dateSet.first())){
                it.remove();
            }
        }


        Map<String, String> guestMap = guestService.findMap();

        for (ListVO listVO : listVOS){
            listVO.setGuestName(guestMap.get(listVO.getGuestId()));
        }

        return listVOS;
    }

    //    @Override
//    @Transactional
//    public void delete(String guestId, Integer driverId, Date date) {
//        deliverRepository.delete(guestId, driverId, date);
//    }
}
