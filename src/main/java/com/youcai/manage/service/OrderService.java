package com.youcai.manage.service;

import com.youcai.manage.dataobject.Guest;
import com.youcai.manage.dataobject.Order;
import com.youcai.manage.dto.excel.order.Export;
import com.youcai.manage.dto.order.AllDTO;
import com.youcai.manage.vo.order.PendingVO;
import com.youcai.manage.vo.stat.RangeVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

public interface OrderService {
    Page<Order> findAll(Pageable pageable);
    List<Order> findByIdGuestId(String guestId);
    List<Order> findByIdGuestIdAndIdDateAndIdState(String guestId, Date date, String state);
    List<Date> findDatesByGuestId(String guestId);
    List<String> findStatesByGuestIdAndDate(String guestId, Date date);
    Page<Guest> findGuestPage(Pageable pageable);
    Page<Guest> findGuestPageByGuestIdLike(Pageable pageable, String guestId);
    Page<Guest> findGuestPageByGuestNameLike(Pageable pageable, String guestName);
    Export getExcelExport(String guestId, Date date, String state);
    Long countByState(String state);
    List<PendingVO> findPendingList(String state);
    void updateState(String guestId, Date date, String oldState, String newState);

    boolean isGuestExist(String guestId);
    boolean isProductExist(String productId);

    List<Object[]> sumByStateAndDate(String state, Date startDate, Date endDate);
    List<Object[]> sumByStateAndDateNoOrder(String state, Date startDate, Date endDate);
    List<Object[]> sumByStateAndDay(String state, int startDay, int endDay);
    List<Object[]> sumByStateAndDateEqual(String state, Date date);
    List<Object[]> sumByStateAndMonth(String state, Integer month);

    List<AllDTO> findAllWith(String guestId, Date date, String state);

    void update(String guestId, Date date, String products);
}
