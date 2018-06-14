package com.youcai.manage.service.impl;

import com.youcai.manage.dataobject.Guest;
import com.youcai.manage.dataobject.Order;
import com.youcai.manage.repository.OrderRepository;
import com.youcai.manage.service.GuestService;
import com.youcai.manage.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private GuestService guestService;

    @Override
    public Page<Order> findAll(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }

    @Override
    public List<Order> findByIdGuestId(String guestId) {
        return orderRepository.findByIdGuestId(guestId);
    }

    @Override
    public List<Date> findDatesByGuestId(String guestId) {
        List<Date> dates = orderRepository.findDistinctIdOdateByIdGuestId(guestId);
        return dates;
    }

    @Override
    public List<Order> findByIdGuestIdAndIdDate(String guestId, Date date) {
        return orderRepository.findByIdGuestIdAndIdOdate(guestId, date);
    }

    @Override
    @Transactional
    public void delete(String guestId, Date date) {
        orderRepository.deleteByIdGuestIdAndIdOdate(guestId, date);
    }

    @Override
    public Page<Guest> findGuestPage(Pageable pageable) {
        List<String> guestIds = orderRepository.findDistinctIdGuestId();
        return guestService.findByIdIn(guestIds, pageable);
    }

    @Override
    public Page<Guest> findGuestPageByGuestIdLike(Pageable pageable, String guestId) {
        List<String> guestIds = orderRepository.findDistinctIdGuestId("%"+guestId+"%");
        return guestService.findByIdIn(guestIds, pageable);
    }

    @Override
    public Page<Guest> findGuestPageByGuestNameLike(Pageable pageable, String guestName) {
        List<String> guestIds = orderRepository.findDistinctIdGuestId();
        return guestService.findByIdInAndNameLike(guestIds, guestName, pageable);
    }
}
