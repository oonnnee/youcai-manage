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
    List<Deliver> findByIdGuestIdAndIdDriverIdAndIdDdate(String guestId, Integer driverId, Date date);
    @Modifying
    @Query(value = "delete from d_list where guest_id=?1 and d_id=?2 and ddate=?3", nativeQuery = true)
    void delete(String guestId, Integer driverId, Date date);
}
