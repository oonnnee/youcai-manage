package com.youcai.manage.repository;

import com.youcai.manage.dataobject.DeliverList;
import com.youcai.manage.dataobject.DeliverListKey;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Date;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class DeliverListRepositoryTest {

    @Autowired
    private DeliverListRepository deliverListRepository;

    @Test
    public void save(){
        DeliverList deliverList = new DeliverList();
        deliverList.setId(new DeliverListKey(1, "1", new Date(), "1"));
        deliverList.setPrice(new BigDecimal(1.2));
        deliverList.setNum(new BigDecimal(30));
        deliverList.setAmount(deliverList.getPrice().multiply(deliverList.getNum()));
        deliverList.setNote("123");
        DeliverList result = deliverListRepository.save(deliverList);
        System.out.println(result);
    }

}