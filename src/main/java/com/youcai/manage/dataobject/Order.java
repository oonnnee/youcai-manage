package com.youcai.manage.dataobject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * 客户采购单
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class Order {

    /*--- 联合主键：采购时间、采购客户id、采购产品id ---*/
    @EmbeddedId
    private OrderKey id;

    private BigDecimal price;

    private BigDecimal num;

    private BigDecimal amount;

    private String note;
}
