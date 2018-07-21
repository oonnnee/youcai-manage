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


//    @Modifying
//    @Query(value = "delete from d_list where guest_id=?1 and d_id=?2 and ddate=?3", nativeQuery = true)
//    void delete(String guestId, Integer driverId, Date date);

}
