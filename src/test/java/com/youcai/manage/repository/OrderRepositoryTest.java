package com.youcai.manage.repository;

import com.youcai.manage.dataobject.Order;
import com.youcai.manage.dataobject.OrderKey;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderRepositoryTest {

    private static final String NAME = "["+OrderRepositoryTest.class.getName()+"]"+" ";

    @Autowired
    private OrderRepository orderRepository;


    @Test
    @Transactional
    public void test() throws ParseException {
        Order order = new Order();
        OrderKey id = new OrderKey();
        id.setOdate(new Date());
        id.setGuestId("123");
        id.setProductId("123");
        order.setId(id);
        order.setPrice(new BigDecimal(2.8));
        order.setNum(new BigDecimal(100));
        order.setAmount(new BigDecimal(2.8*100));
        order.setNote("note");

        Order result = null;

        result = orderRepository.save(order);
        assertTrue(NAME+"save",result != null);

        order.setNote("hi,note");
        result = orderRepository.save(order);
        assertTrue(NAME+"update",result.getNote().equals("hi,note"));

        result = orderRepository.findOne(id);
        assertTrue(NAME+"findOne",result != null);

        orderRepository.delete(id);
        result = orderRepository.findOne(id);
        assertTrue(NAME+"delete",result == null);

    }

    @Test
    public void findDistinctIdGuestId(){
        List<String> strings = orderRepository.findDistinctIdGuestId();
    }

    @Test
    public void countDistinctByIdStateStartsWith(){
        Long count = orderRepository.countDistinctByIdStateLike("1%");
        System.out.println(count);
    }

    @Test
    public void findByIdStateStartsWith(){
        List orders = orderRepository.findDistinctOdateAndGuestIdAndStateByStateLike("1%");
    }
}