package com.youcai.manage.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class FindByGuestIdAndPdateWithCategoryVO {

    private String code;

    private String name;

    @JsonProperty("products")
    private List<FindByGuestIdAndPdateVO> findByGuestIdAndPdateVOS;
}
