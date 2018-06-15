package com.youcai.manage.service;

import com.youcai.manage.dataobject.Deliver;
import com.youcai.manage.dataobject.Guest;
import com.youcai.manage.vo.deliver.ListVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;
import java.util.Set;

public interface DeliverService {
    List<Deliver> findByIdGuestId(String guestId);
    List<Deliver> findByGuestIdAndDriverIdAndDate(String guestId, Integer driverId, Date date);
    Set<ListVO> findListVOSet();
    Set<ListVO> findListVOSetByGuestName(String guestName);
    Set<ListVO> findListVOSetByDriverName(String driverName);
    void delete(String guestId, Integer driverId, Date date);
}
