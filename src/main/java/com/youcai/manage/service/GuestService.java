package com.youcai.manage.service;

import com.youcai.manage.dataobject.Guest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;


public interface GuestService {

    Guest save(Guest guest);

    void delete(String id);

    Guest findOne(String id);

    Page<Guest> findAll(Pageable pageable);

    /*------------ 更新用户，不包括密码 -------------*/
    Guest update(Guest guest);

    /*------------ 更新用户密码 -------------*/
    void updatePwd(String id, String pwd);

    Page<Guest> findByNameLike(String name, Pageable pageable);

    Page<Guest> findByIdLike(String id, Pageable pageable);

    Long countAll();

    Page<Guest> findByIdIn(List<String> ids, Pageable pageable);

    Page<Guest> findByIdInAndNameLike(List<String> ids, String name, Pageable pageable);

    Map<String, String> findMap();

    Map<String, String> findMapByNameLike(String name);

    boolean isPhoneRepeat(String phone);
    boolean isPhoneRepeat(String phone, String id);
}
