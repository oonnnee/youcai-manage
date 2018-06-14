package com.youcai.manage.service;

import com.youcai.manage.dataobject.Deliver;
import com.youcai.manage.dataobject.Guest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DeliverService {
    List<Deliver> findByIdGuestId(String guestId);
    Page<Guest> findGuestPage(Pageable pageable);
    Page<Guest> findGuestPageByGuestNameLike(Pageable pageable, String guestName);
    Page<Guest> findGuestPageByDriverNameLike(Pageable pageable, String guestName);
}
