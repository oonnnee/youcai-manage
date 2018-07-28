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

    @Query(value = "select 1 from orders where guest_id = ?1 limit 1", nativeQuery = true)
    String find(String guestId);
    @Query(value = "select 1 from orders where product_id = ?1 limit 1", nativeQuery = true)
    String findWithProductId(String productId);

    @Query(value = "select guest_id, SUM(amount) from orders where state = ?1 and odate BETWEEN ?2 and ?3 GROUP BY guest_id ORDER BY SUM(amount) desc", nativeQuery = true)
    List<Object[]> sumByStateAndDate(String state, Date startDate, Date endDate);
    @Query(value = "select guest_id, SUM(amount) from orders where state = ?1 and odate >= ?2 and odate < ?3 GROUP BY guest_id", nativeQuery = true)
    List<Object[]> sumByStateAndDateNoOrder(String state, Date startDate, Date endDate);
    @Query(value = "select guest_id, SUM(amount) from orders where state = ?1 and odate = ?2 GROUP BY guest_id", nativeQuery = true)
    List<Object[]> sumByStateAndDateEqual(String state, Date date);
    @Query(value = "select guest_id, SUM(amount) from orders where state = ?1 and odate like ?2 GROUP BY guest_id", nativeQuery = true)
    List<Object[]> sumByStateAndMonth(String state, String Date);

    @Query(value = "SELECT o.odate as date, o.state, o.guest_id, g.name as guest_name, o.product_id, " +
            "p.name as product_name, c.name as product_category, p.unit as product_unit, o.price as product_price, " +
            "o.num as product_num, o.amount as product_amount, p.imgfile as product_img, o.note " +
            "FROM orders o LEFT JOIN product p ON o.product_id = p.id LEFT JOIN guest g on o.guest_id = g.id " +
            "LEFT JOIN category c ON c.code= p.p_code WHERE o.guest_id=?1 AND o.odate=?2 " +
            "AND o.state=?3 ORDER BY p.p_code", nativeQuery = true)
    List<Object[]> findAllWith(String guestId, Date date, String state);

}
