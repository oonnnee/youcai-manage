package com.youcai.manage.service.impl;

import com.youcai.manage.dataobject.Guest;
import com.youcai.manage.exception.ManageException;
import com.youcai.manage.repository.GuestRepository;
import com.youcai.manage.service.DeliverService;
import com.youcai.manage.service.GuestService;
import com.youcai.manage.service.OrderService;
import com.youcai.manage.service.PricelistService;
import com.youcai.manage.utils.EDSUtils;
import com.youcai.manage.utils.KeyUtils;
import com.youcai.manage.utils.ManageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GuestServiceImpl implements GuestService, UserDetailsService {

    private void checkPhone(String phone){
        ManageUtils.ManageException(this.isPhoneRepeat(phone), "该手机号已被注册");
    }
    private void checkPhone(String phone, String id){
        ManageUtils.ManageException(this.isPhoneRepeat(phone, id), "该手机号已被注册");
    }

    @Autowired
    private GuestRepository guestRepository;
    @Autowired
    private PricelistService pricelistService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private DeliverService deliverService;

    @Override
    @Transactional
    public Guest save(Guest guest) {
        this.checkPhone(guest.getPhone());

        guest.setId(KeyUtils.generate());
        guest.setPwd(EDSUtils.encryptBasedDes(guest.getPwd()));

        Guest saveResult = guestRepository.save(guest);
        ManageUtils.ManageException(saveResult, "新增客户失败，请稍后再试");

        return saveResult;
    }

    @Override
    @Transactional
    public Guest update(Guest guest) {
        Guest findResult = guestRepository.findOne(guest.getId());
        ManageUtils.ManageException(findResult, "此客户不存在");

        this.checkPhone(guest.getPhone(), guest.getId());

        guest.setPwd(findResult.getPwd());

        Guest updateResult = guestRepository.save(guest);
        ManageUtils.ManageException(updateResult, "更新客户失败，请稍后再试");

        return updateResult;
    }

    @Override
    @Transactional
    public void updatePwd(String id, String pwd) {
        Guest findResult = guestRepository.findOne(id);
        ManageUtils.ManageException(findResult, "此客户不存在");

        findResult.setPwd(EDSUtils.encryptBasedDes(pwd));

        ManageUtils.ManageException(guestRepository.save(findResult), "更新客户密码失败，请稍后再试");
    }

    @Override
    @Transactional
    public void delete(String id) {
        ManageUtils.ManageException(this.findOne(id), "删除客户失败\n原因：此客户不存在");
        ManageUtils.ManageException(pricelistService.isGuestExist(id), "删除客户失败\n原因：报价单中存在此客户");
        ManageUtils.ManageException(orderService.isGuestExist(id), "删除客户失败\n原因：采购单中存在此客户");
        ManageUtils.ManageException(deliverService.isGuestExist(id), "删除客户失败\n原因：送货单中存在此客户");

        guestRepository.delete(id);
    }

    @Override
    public Guest findOne(String id) {
        Guest result = guestRepository.findOne(id);
        return result;
    }

    @Override
    public Page<Guest> findAll(Pageable pageable) {
        Page<Guest> guestPage = guestRepository.findAll(pageable);
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
    public Page<Guest> findByPhoneLike(String phone, Pageable pageable) {
        if (StringUtils.isEmpty(phone)){
            return guestRepository.findAll(pageable);
        }
        Page<Guest> guestPage = guestRepository.findByPhoneLike("%" + phone + "%", pageable);
        return guestPage;
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

    @Override
    public Map<String, String> findMap() {
        List<Guest> guests = guestRepository.findAll();
        Map<String, String> map = new HashMap<>();
        for(Guest guest : guests){
            if (!guest.getId().equals("admin")){
                map.put(guest.getId(), guest.getName());
            }
        }
        return map;
    }

    @Override
    public Map<String, String> findMapByNameLike(String name){
        List<Guest> guests = guestRepository.findByNameLike("%" + name + "%");
        Map<String, String> map = new HashMap<>();
        for(Guest guest : guests){
            map.put(guest.getId(), guest.getName());
        }
        return map;
    }

    @Override
    public boolean isPhoneRepeat(String phone) {
        return guestRepository.findByPhone(phone)!=null;
    }
    @Override
    public boolean isPhoneRepeat(String phone, String id) {
        Guest guest = guestRepository.findByPhone(phone);
        return guest == null ?
                false : !guest.getId().equals(id);
    }
}
