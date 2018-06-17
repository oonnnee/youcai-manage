package com.youcai.manage.service.impl;

import com.youcai.manage.dataobject.Deliver;
import com.youcai.manage.dataobject.Product;
import com.youcai.manage.dto.excel.deliver.Export;
import com.youcai.manage.dto.excel.deliver.ProductExport;
import com.youcai.manage.repository.DeliverRepository;
import com.youcai.manage.service.DeliverService;
import com.youcai.manage.service.DriverService;
import com.youcai.manage.service.GuestService;
import com.youcai.manage.service.ProductService;
import com.youcai.manage.vo.deliver.ListVO;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Override
    public List<Deliver> findByIdGuestId(String guestId) {
        return deliverRepository.findByIdGuestId(guestId);
    }

    @Override
    public Set<ListVO> findListVOSet() {
        List<Deliver> delivers = deliverRepository.findAll();
        Map<Integer, String> driverMap = driverService.findMap();
        Map<String, String> guestMap = guestService.findMap();
        Set<ListVO> listVOSet = new HashSet<>();
        for (Deliver deliver : delivers){
            Integer driverId = deliver.getId().getDriverId();
            String guestId = deliver.getId().getGuestId();
            ListVO listVO = new ListVO();
            listVO.setGuestId(guestId);
            listVO.setGuestName(guestMap.get(guestId));
            listVO.setDriverId(driverId);
            listVO.setDriverName(driverMap.get(driverId));
            listVO.setDate(deliver.getId().getDdate());
            listVOSet.add(listVO);
        }
        return listVOSet;
    }

    @Override
    public Set<ListVO> findListVOSetByDriverName(String driverName) {
        List<Deliver> delivers = deliverRepository.findAll();
        Map<Integer, String> driverMap = driverService.findMapByNameLike(driverName);
        Map<String, String> guestMap = guestService.findMap();
        Set<ListVO> listVOSet = new HashSet<>();
        for (Deliver deliver : delivers){
            if (driverMap.get(deliver.getId().getDriverId()) == null){
                continue;
            }
            Integer driverId = deliver.getId().getDriverId();
            String guestId = deliver.getId().getGuestId();
            ListVO listVO = new ListVO();
            listVO.setGuestId(guestId);
            listVO.setGuestName(guestMap.get(guestId));
            listVO.setDriverId(driverId);
            listVO.setDriverName(driverMap.get(driverId));
            listVO.setDate(deliver.getId().getDdate());
            listVOSet.add(listVO);
        }
        return listVOSet;
    }

    @Override
    public Set<ListVO> findListVOSetByGuestName(String guestName) {
        List<Deliver> delivers = deliverRepository.findAll();
        Map<Integer, String> driverMap = driverService.findMap();
        Map<String, String> guestMap = guestService.findMapByNameLike(guestName);
        Set<ListVO> listVOSet = new HashSet<>();
        for (Deliver deliver : delivers){
            if (guestMap.get(deliver.getId().getGuestId()) == null){
                continue;
            }
            Integer driverId = deliver.getId().getDriverId();
            String guestId = deliver.getId().getGuestId();
            ListVO listVO = new ListVO();
            listVO.setGuestId(guestId);
            listVO.setGuestName(guestMap.get(guestId));
            listVO.setDriverId(driverId);
            listVO.setDriverName(driverMap.get(driverId));
            listVO.setDate(deliver.getId().getDdate());
            listVOSet.add(listVO);
        }
        return listVOSet;
    }

    @Override
    public List<Deliver> findByGuestIdAndDriverIdAndDate(String guestId, Integer driverId, Date date) {
        return deliverRepository.findByIdGuestIdAndIdDriverIdAndIdDdate(guestId, driverId, date);
    }

    @Override
    @Transactional
    public void delete(String guestId, Integer driverId, Date date) {
        deliverRepository.delete(guestId, driverId, date);
    }

    @Override
    @Transactional
    public void save(List<Deliver> delivers) {
        deliverRepository.save(delivers);
    }

    @Override
    public Export getExcelExport(String guestId, Integer driverId, Date date) {
        Export export = new Export();
        /*------------ 客户名 -------------*/
        export.setGuestName(guestService.findOne(guestId).getName());
        /*------------ 司机名 -------------*/
        export.setDriverName(driverService.findOne(driverId).getName());
        /*------------ 日期 -------------*/
        export.setDate(date);
        /*------------ 产品&送货单金额 -------------*/
        List<ProductExport> productExports = new ArrayList<>();
        BigDecimal amount = BigDecimal.ZERO;
        List<Deliver> delivers = this.findByGuestIdAndDriverIdAndDate(guestId, driverId, date);
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
        /*--- 产品 ---*/
        export.setProductExports(productExports);
        /*--- 采购单金额 ---*/
        export.setAmount(amount);
        return export;
    }
}
