package com.youcai.manage.repository;

import com.youcai.manage.dataobject.DeliverList;
import com.youcai.manage.dataobject.DeliverListKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliverListRepository extends JpaRepository<DeliverList, DeliverListKey> {

}
