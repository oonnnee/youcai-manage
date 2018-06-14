package com.youcai.manage.service.impl;

import com.youcai.manage.dataobject.Guest;
import com.youcai.manage.enums.ResultEnum;
import com.youcai.manage.exception.ManageException;
import com.youcai.manage.repository.GuestRepository;
import com.youcai.manage.service.GuestService;
import com.youcai.manage.utils.EDSUtils;
import com.youcai.manage.utils.KeyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class GuestServiceImpl implements GuestService, UserDetailsService {

    @Autowired
    private GuestRepository guestRepository;

    @Override
    @Transactional
    public Guest save(Guest guest) {
        guest.setId(KeyUtils.generate());
        guest.setPwd(EDSUtils.encryptBasedDes(guest.getPwd()));
        Guest saveResult = guestRepository.save(guest);
        if (saveResult == null){
            throw new ManageException(ResultEnum.MANAGE_GUEST_SAVE_ERROR);
        }
        return saveResult;
    }

    @Override
    @Transactional
    public Guest update(Guest guest) {
        /*------------ 1.准备 -------------*/
        // 填充密码
        Guest findResult = guestRepository.findOne(guest.getId());
        guest.setPwd(findResult.getPwd());
        /*------------ 2.更新 -------------*/
        Guest updateResult = guestRepository.save(guest);
        if (updateResult == null){
            throw new ManageException(ResultEnum.MANAGE_GUEST_UPDATE_ERROR);
        }
        /*------------ 3.结果处理 -------------*/
        /*------------ 4.返回结果 -------------*/
        return updateResult;
    }

    @Override
    @Transactional
    public void delete(String id) {
        guestRepository.delete(id);
    }

    @Override
    public Guest findOne(String id) {
        /*------------ 1.查询 -------------*/
        Guest result = guestRepository.findOne(id);
        /*------------ 2.结果处理 -------------*/
        /*------------ 3.返回结果 -------------*/
        return result;
    }

    @Override
    public Page<Guest> findAll(Pageable pageable) {
        /*------------ 1.查询 -------------*/
        Page<Guest> guestPage = guestRepository.findAll(pageable);
        /*------------ 2.结果处理 -------------*/
        /*------------ 3.返回结果 -------------*/
        return guestPage;
    }

    @Override
    public Page<Guest> findByNameLike(String name, Pageable pageable) {
        if (StringUtils.isEmpty(name)){
            return guestRepository.findAll(pageable);
        }
        Page<Guest> guestPage = guestRepository.findByNameLike("%" + name + "%", pageable);
        return guestPage;
    }

    @Override
    public Page<Guest> findByIdLike(String id, Pageable pageable) {
        if (StringUtils.isEmpty(id)){
            return guestRepository.findAll(pageable);
        }
        Page<Guest> guestPage = guestRepository.findByIdLike("%" + id + "%", pageable);
        return guestPage;
    }

    @Override
    @Transactional
    public void updatePwd(String id, String pwd) {
        /*------------ 1.查询用户 -------------*/
        Guest guest = guestRepository.findOne(id);
        /*------------ 2.更新密码 -------------*/
        guest.setPwd(EDSUtils.encryptBasedDes(pwd));
        guestRepository.save(guest);
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        if ("admin".equals(s) == false){
            throw new UsernameNotFoundException("用户名不存在");
        }
        Guest findResult = guestRepository.findById("admin");
        return findResult;
    }

    @Override
    public Long countAll() {
        return guestRepository.countBy()-1;
    }

    @Override
    public Page<Guest> findByIdIn(List<String> ids, Pageable pageable) {
        return guestRepository.findByIdIn(ids, pageable);
    }

    @Override
    public Page<Guest> findByIdInAndNameLike(List<String> ids, String name, Pageable pageable) {
        return guestRepository.findByIdInAndNameLike(ids, "%"+name+"%", pageable);
    }
}
