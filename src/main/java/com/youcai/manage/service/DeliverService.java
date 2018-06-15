package com.youcai.manage.service;

import com.youcai.manage.dataobject.Deliver;
import com.youcai.manage.dataobject.Guest;
import com.youcai.manage.dto.deliver.ListDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DeliverService {
    List<Deliver> findByIdGuestId(String guestId);
    List<ListDTO> findListDTOS();
    List<ListDTO> findListDTOSByGuestName(String guestName);
    List<ListDTO> findListDTOSByDriverName(String driverName);
}
