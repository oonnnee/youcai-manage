package com.youcai.manage.vo.pricelist;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class PricelistsVO {

    private Date date;

    private String guestId;

    private String guestName;

    @JsonProperty("products")
    private List<ProductVO> products;
}
