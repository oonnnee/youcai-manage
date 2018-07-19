package com.youcai.manage.repository;

import com.youcai.manage.dataobject.Order;
import com.youcai.manage.dataobject.OrderKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, OrderKey> {
    List<Order> findByIdGuestId(String guestId);

    List<Order> findByIdGuestIdAndIdOdateAndIdState(String guestId, Date date, String state);

    @Query(value = "select distinct odate from orders where guest_id = ?1 order by odate desc", nativeQuery = true)
    List<Date> findDistinctIdOdateByIdGuestId(String guestId);

    @Query(value = "select distinct state from orders where guest_id = ?1 and odate = ?2 order by state asc", nativeQuery = true)
    List<String> findDistinctIdStateByIdGuestIdAndIdOdate(String guestId, Date date);

    @Query(value = "select distinct guest_id from orders", nativeQuery = true)
    List<String> findDistinctIdGuestId();

    @Query(value = "select distinct guest_id from orders where guest_id like ?1", nativeQuery = true)
    List<String> findDistinctIdGuestId(String guestId);

    @Query(value = "select count(distinct state) from orders where state like ?1", nativeQuery = true)
    Long countDistinctByIdStateLike(String state);

    @Query(value = "select distinct guest_id,odate,state from orders where state like ?1", nativeQuery = true)
    List<Object[]> findDistinctOdateAndGuestIdAndStateByStateLike(String state);

    @Modifying
    @Query(value = "update orders set state=?4 where guest_id=?1 and odate=?2 and state=?3", nativeQuery = true)
    void updateState(String guestId, Date date, String oldState, String newState);
}
