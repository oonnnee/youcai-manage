package com.youcai.manage.dto.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewDTO {
    @JsonProperty("id")
    private String productId;
    private BigDecimal price;
    private BigDecimal num;
    private String note;

}
