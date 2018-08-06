package com.youcai.manage.vo.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class OrdersVO {

    private Date date;

    private String guestId;

    private String guestName;

    private String state;

    private BigDecimal total;

    @JsonProperty("products")
    private List<ProductVO> products;
}
