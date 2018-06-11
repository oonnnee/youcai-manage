package com.youcai.manage.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class FindByGuestIdAndPdateWithCategoryVO {

    private String categoryCode;

    private String categoryName;

    @JsonProperty("products")
    private List<FindByGuestIdAndPdateVO> findByGuestIdAndPdateVOS;
}
