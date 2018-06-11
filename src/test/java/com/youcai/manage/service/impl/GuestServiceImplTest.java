package com.youcai.manage.service.impl;

import com.youcai.manage.dataobject.Guest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GuestServiceImplTest {

    @Autowired
    private GuestServiceImpl guestService;

    @Test
    @Transactional
    public void basic(){
        Guest guest = new Guest();
        guest.setId("123");
        guest.setName("李达达");
        guest.setPwd("qing1016..");
        guest.setAddr("西安邮电大学");
        guest.setPhone("18829534050");
        guest.setLeader1("赵无极");
        guest.setMobile1("15348938791");
        guest.setLeader2("黛慕白");
        guest.setMobile2("15348932791");
        guest.setNote("this is a note");

        Guest result = null;

        /*------------ 1.增 -------------*/
        result = guestService.save(guest);

        /*------------ 2.改 -------------*/
        guest.setPwd("qing1016");
        result = guestService.save(guest);

        /*------------ 3.查 -------------*/
        result = guestService.findOne(result.getId());

        /*------------ 4.删 -------------*/
        guestService.delete(result.getId());
        result = guestService.findOne(result.getId());

    }

}