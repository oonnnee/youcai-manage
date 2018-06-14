package com.youcai.manage.repository;

import com.youcai.manage.dataobject.Order;
import com.youcai.manage.dataobject.OrderKey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, OrderKey> {
    List<Order> findByIdGuestId(String guestId);

    List<Order> findByIdGuestIdAndIdOdate(String guestId, Date date);

    @Query(value = "select distinct odate from orders where guest_id = ?1 order by odate desc", nativeQuery = true)
    List<Date> findDistinctIdOdateByIdGuestId(String guestId);

    @Modifying
    @Query(value = "delete from orders where guest_id=?1 and odate=?2", nativeQuery = true)
    void deleteByIdGuestIdAndIdOdate(String guestId, Date date);

    @Query(value = "select distinct guest_id from orders", nativeQuery = true)
    List<String> findDistinctIdGuestId();

    @Query(value = "select distinct guest_id from orders where guest_id like ?1", nativeQuery = true)
    List<String> findDistinctIdGuestId(String guestId);
}
