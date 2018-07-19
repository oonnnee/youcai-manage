package com.youcai.manage.vo.deliver;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class DeliversVO {

    private String guestId;

    private String guestName;

    private Integer driverId;

    private String driverName;

    private Date date;

    private String state;

    @JsonProperty("products")
    private List<ProductVO> products;
}
