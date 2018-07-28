package com.youcai.manage.dto.pricelist;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AllDTO {
    private Date date;
    private String guestId;
    private String guestName;
    private String productId;
    private String productName;
    private String productCategory;
    private String productUnit;
    private BigDecimal productMarketPrice;
    private BigDecimal productGuestPrice;
    private String productImgfile;
    private String note;

    public AllDTO(Object o, Object o1, Object o2, Object o3, Object o4, Object o5, Object o6, Object o7, Object o8, Object o9, Object o10) {
        super();
        if (o instanceof  Date) this.date = (Date)o;
        if (o1 instanceof  String) this.guestId = (String)o1;
        if (o2 instanceof  String) this.guestName = (String)o2;
        if (o3 instanceof  String) this.productId = (String)o3;
        if (o4 instanceof  String) this.productName = (String)o4;
        if (o5 instanceof  String) this.productCategory = (String)o5;
        if (o6 instanceof  String) this.productUnit = (String)o6;
        if (o7 instanceof  BigDecimal) this.productMarketPrice = (BigDecimal)o7;
        if (o8 instanceof  BigDecimal) this.productGuestPrice = (BigDecimal)o8;
        if (o9 instanceof  String) this.productImgfile = (String)o9;
        if (o10 instanceof  String) this.note = (String)o10;
    }
}
