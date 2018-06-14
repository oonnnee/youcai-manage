package com.youcai.manage.service.impl;

import com.youcai.manage.dataobject.Deliver;
import com.youcai.manage.dataobject.Guest;
import com.youcai.manage.repository.DeliverRepository;
import com.youcai.manage.service.DeliverService;
import com.youcai.manage.service.GuestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeliverServiceImpl implements DeliverService {

    @Autowired
    private DeliverRepository deliverRepository;
    @Autowired
    private GuestService guestService;

    @Override
    public List<Deliver> findByIdGuestId(String guestId) {
        return deliverRepository.findByIdGuestId(guestId);
    }

    @Override
    public Page<Guest> findGuestPage(Pageable pageable) {
        List<String> guestIds = deliverRepository.findDistinctIdGuestId();
        return guestService.findByIdIn(guestIds, pageable);
    }
}
