package com.youcai.manage.service;

import com.youcai.manage.dataobject.Deliver;
import com.youcai.manage.dataobject.Guest;
import com.youcai.manage.dto.excel.deliver.Export;
import com.youcai.manage.vo.deliver.ListVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;
import java.util.Set;

public interface DeliverService {
    List<Deliver> findByIdGuestId(String guestId);
    List<Deliver> findByGuestIdAndDate(String guestId, Date date);
//    void delete(String guestId, Integer driverId, Date date);
    void save(List<Deliver> delivers, String orderGuestId, Date orderDate);
    Export getExcelExport(String guestId, Date date);
    Page<Guest> findGuestPage(Pageable pageable, String guestName);
    List<Date> findDatesByGuestId(String guestId);

    boolean isGuestExist(String guestId);
    boolean isProductExist(String productId);
    boolean isDriverExist(Integer driverId);
}
