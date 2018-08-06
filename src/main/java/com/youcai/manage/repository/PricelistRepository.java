package com.youcai.manage.repository;

import com.youcai.manage.dataobject.Pricelist;
import com.youcai.manage.dataobject.PricelistKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Transactional
public interface PricelistRepository extends JpaRepository<Pricelist, PricelistKey> {

    List<Pricelist> findById_GuestId(String guestId);

    List<Pricelist> findById_GuestIdAndId_Pdate(String guestId, Date pdate);

    @Modifying
    @Query("delete from Pricelist where guest_id=?1 and pdate=?2")
    void deleteById_GuestIdAndId_Pdate(String guestId, Date pdate);

    @Query(value = "select distinct pdate from pricelist where guest_id = ?1 order by pdate desc", nativeQuery = true)
    List<Date> findDistinctId_PdateById_GuestId(String guestId);

    @Query(value = "select 1 from pricelist where guest_id = ?1 limit 1", nativeQuery = true)
    String find(String guestId);
    @Query(value = "select 1 from pricelist where product_id = ?1 limit 1", nativeQuery = true)
    String findWithProductId(String productId);
    @Query(value = "select 1 from pricelist where guest_id = ?1 and pdate = ?2 limit 1", nativeQuery = true)
    String findWithGuestIdAndDate(String guestId, Date date);

    @Query(value = "SELECT pri.pdate as date, pri.guest_id, g.name as guest_name, pri.product_id, " +
            "pro.name as product_name, c.name as product_category, pro.unit as product_unit, " +
            "pro.price as marketPrice, pri.price as guestPrice, pro.imgfile as product_img, " +
            "pri.note FROM pricelist pri LEFT JOIN product pro ON pri.product_id = pro.id " +
            "LEFT JOIN guest g on pri.guest_id = g.id LEFT JOIN category c ON c.code= pro.p_code " +
            "WHERE pri.guest_id=?1 AND pri.pdate=?2 ORDER BY pro.p_code", nativeQuery = true)
    List<Object[]> findAllWith(String guestId, Date date);

}

