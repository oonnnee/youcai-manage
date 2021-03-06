package com.youcai.manage.repository;

import com.youcai.manage.dataobject.Guest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GuestRepository extends JpaRepository<Guest, String> {

    Page<Guest> findByNameLike(String name, Pageable pageable);
    List<Guest> findByNameLike(String name);
    Page<Guest> findByIdLike(String id, Pageable pageable);
    Page<Guest> findByPhoneLike(String phone, Pageable pageable);

    Guest findById(String id);

    Long countBy();

    Page<Guest> findByIdIn(List<String> ids, Pageable pageable);

    Page<Guest> findByIdInAndNameLike(List<String> ids, String name, Pageable pageable);

    Guest findByPhone(String phone);
}
