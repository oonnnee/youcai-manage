package com.youcai.manage.service;

import com.youcai.manage.dataobject.Guest;
import com.youcai.manage.dataobject.Order;
import com.youcai.manage.dto.excel.order.Export;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

public interface OrderService {
    Page<Order> findAll(Pageable pageable);
    List<Order> findByIdGuestId(String guestId);
    List<Order> findByIdGuestIdAndIdDate(String guestId, Date date);
    List<Date> findDatesByGuestId(String guestId);
    void delete(String guestId, Date date);
    Page<Guest> findGuestPage(Pageable pageable);
    Page<Guest> findGuestPageByGuestIdLike(Pageable pageable, String guestId);
    Page<Guest> findGuestPageByGuestNameLike(Pageable pageable, String guestName);
    Export getExcelExport(String guestId, Date date);

}
