package com.youcai.manage.repository;

import com.youcai.manage.dataobject.Order;
import com.youcai.manage.dataobject.OrderKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, OrderKey> {
}
