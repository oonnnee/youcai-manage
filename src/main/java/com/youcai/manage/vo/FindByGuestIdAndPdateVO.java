package com.youcai.manage.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class FindByGuestIdAndPdateVO {

    @JsonProperty("id")
    private String productId;

    @JsonProperty("name")
    private String productName;

    private BigDecimal price;

    private String note;
}
