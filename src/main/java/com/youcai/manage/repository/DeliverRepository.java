package com.youcai.manage.repository;

import com.youcai.manage.dataobject.Deliver;
import com.youcai.manage.dataobject.DeliverKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DeliverRepository extends JpaRepository<Deliver, DeliverKey> {
    List<Deliver> findByIdGuestId(String guestId);

    @Query(value = "select distinct guest_id from d_list", nativeQuery = true)
    List<String> findDistinctIdGuestId();

    @Query(value = "select distinct d_id from d_list", nativeQuery = true)
    List<Integer> findDistinctIdDriverId();
}
