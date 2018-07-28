package com.youcai.manage.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DeliverRepositoryTest {

    @Autowired
    private DeliverRepository deliverRepository;

    @Test
    public void test() throws ParseException {
        List<Object[]> objectss = deliverRepository.findAllWith("2133148826", new SimpleDateFormat("yyyy-MM-dd").parse("2018-07-21"));
    }
}