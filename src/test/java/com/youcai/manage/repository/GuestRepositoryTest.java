package com.youcai.manage.repository;

import com.youcai.manage.dataobject.Guest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GuestRepositoryTest {

    private static final String NAME = "["+GuestRepositoryTest.class.getName()+"] ";

    @Autowired
    private GuestRepository guestRepository;

    @Test
    @Transactional
    public void test(){
        Guest result = null;

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

        result = guestRepository.save(guest);
        assertTrue(NAME+"save", result.getId().equals("123"));

        guest.setPwd("qing1016");
        result = guestRepository.save(guest);
        assertTrue(NAME+"update", result.getPwd().equals("qing1016"));

        result = guestRepository.findOne("123");
        assertTrue(NAME+"findOne", result.getId().equals("123"));

        guestRepository.delete("123");
        result = guestRepository.findOne("123");
        assertTrue(NAME+"delete", result == null);
    }

    @Test
    public void delete(){
        try {

        guestRepository.delete("1");
        }catch (Exception  e){}
    }

    @Test
    public void countBy(){
        Long count = guestRepository.countBy();
        System.out.println(count);
    }
}