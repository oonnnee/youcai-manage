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
    }

}