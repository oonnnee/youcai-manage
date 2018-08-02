package com.youcai.manage.repository;

import com.youcai.manage.dataobject.Deliver;
import com.youcai.manage.dataobject.DeliverKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface DeliverRepository extends JpaRepository<Deliver, DeliverKey> {
    List<Deliver> findByIdGuestId(String guestId);

    List<Deliver> findByIdGuestIdAndIdDdate(String guestId, Date date);

    @Query(value = "select distinct guest_id from d_list", nativeQuery = true)
    List<String> findDistinctGuestId();

    @Query(value = "select distinct ddate from d_list where guest_id = ?1 order by ddate desc", nativeQuery = true)
    List<Date> findDistinctDateByGuestId(String guestId);

    @Query(value = "select 1 from d_list where guest_id = ?1 limit 1", nativeQuery = true)
    String find(String guestId);
    @Query(value = "select 1 from d_list where product_id = ?1 limit 1", nativeQuery = true)
    String findWithProductId(String productId);
    @Query(value = "select 1 from d_list where d_id = ?1 limit 1", nativeQuery = true)
    String findWithDriverId(Integer driverId);

    @Query(value = "select 1 from d_list where d_id = ?1 and state = ?2 limit 1", nativeQuery = true)
    String findWithDriverIdAndState(Integer driverId, String state);

    @Query(value = "SELECT\n" +
            "\td.ddate AS date,\n" +
            "\td.state,\n" +
            "\tdriver.id AS driver_id,\n" +
            "\tdriver.NAME AS driver_name,\n" +
            "\td.guest_id,\n" +
            "\tg.NAME AS guest_name,\n" +
            "\td.product_id,\n" +
            "\tp.NAME AS product_name,\n" +
            "\tc.NAME AS product_category,\n" +
            "\tp.unit AS product_unit,\n" +
            "\td.price AS product_price,\n" +
            "\td.num AS product_num,\n" +
            "\td.amount AS product_amount,\n" +
            "\tp.imgfile AS product_img,\n" +
            "\td.note \n" +
            "FROM\n" +
            "\td_list d\n" +
            "\tLEFT JOIN product p ON d.product_id = p.id\n" +
            "\tLEFT JOIN guest g ON d.guest_id = g.id\n" +
            "\tLEFT JOIN category c ON c.CODE = p.p_code\n" +
            "\tLEFT JOIN driver ON driver.id = d.d_id \n" +
            "WHERE\n" +
            "\td.guest_id = ?1 \n" +
            "\tAND d.ddate = ?2 \n" +
            "ORDER BY\n" +
            "\tp.p_code", nativeQuery = true)
    List<Object[]> findAllWith(String guestId, Date date);

//    @Modifying
//    @Query(value = "delete from d_list where guest_id=?1 and d_id=?2 and ddate=?3", nativeQuery = true)
//    void delete(String guestId, Integer driverId, Date date);

}
