package com.youcai.manage.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PricelistDateVO {

    private String guestId;

    private String guestName;

    private Set<Date> dates;

}
