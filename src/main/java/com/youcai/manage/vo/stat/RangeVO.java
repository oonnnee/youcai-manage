package com.youcai.manage.vo.stat;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RangeVO {
    @JsonProperty("name")
    private String guestName;

    private BigDecimal total;
}
