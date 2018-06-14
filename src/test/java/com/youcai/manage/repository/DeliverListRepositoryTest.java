package com.youcai.manage.repository;

import com.youcai.manage.dataobject.Deliver;
import com.youcai.manage.dataobject.DeliverKey;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Date;

@SpringBootTest
@RunWith(SpringRunner.class)
public class DeliverListRepositoryTest {

    @Autowired
    private DeliverRepository deliverListRepository;

    @Test
    public void save(){
        Deliver deliverList = new Deliver();
        deliverList.setId(new DeliverKey(1, "1", new Date(), "1"));
        deliverList.setPrice(new BigDecimal(1.2));
        deliverList.setNum(new BigDecimal(30));
        deliverList.setAmount(deliverList.getPrice().multiply(deliverList.getNum()));
        deliverList.setNote("123");
        Deliver result = deliverListRepository.save(deliverList);
        System.out.println(result);
    }

}