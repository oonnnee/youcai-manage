package com.youcai.manage.dto.deliver;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AllDTO {
    private Date deliverDate;
    private Date orderDate;
    private String state;
    private Integer driverId;
    private String driverName;
    private String guestId;
    private String guestName;
    private String productId;
    private String productName;
    private String productCategory;
    private String productUnit;
    private BigDecimal productPrice;
    private BigDecimal productNum;
    private BigDecimal productAmount;
    private String productImgfile;
    private String note;

    public AllDTO(Object o, Object oo1, Object o1, Object oo2, Object oo3, Object o2, Object o3, Object o4, Object o5, Object o6, Object o7, Object o8, Object o9, Object o10, Object o11, Object o12) {
        super();
        if (o instanceof  Date) this.deliverDate = (Date)o;

        if (oo1 instanceof  Date) this.orderDate = (Date)oo1;

        if (o1 instanceof  String) this.state = (String)o1;

        if (oo2 instanceof  Integer) this.driverId = (Integer)oo2;
        if (oo3 instanceof  String) this.driverName = (String)oo3;

        if (o2 instanceof  String) this.guestId = (String)o2;
        if (o3 instanceof  String) this.guestName = (String)o3;
        if (o4 instanceof  String) this.productId = (String)o4;
        if (o5 instanceof  String) this.productName = (String)o5;
        if (o6 instanceof  String) this.productCategory = (String)o6;
        if (o7 instanceof  String) this.productUnit = (String)o7;
        if (o8 instanceof  BigDecimal) this.productPrice = (BigDecimal)o8;
        if (o9 instanceof  BigDecimal) this.productNum = (BigDecimal)o9;
        if (o10 instanceof  BigDecimal) this.productAmount = (BigDecimal)o10;
        if (o11 instanceof  String) this.productImgfile = (String)o11;
        if (o12 instanceof  String) this.note = (String)o12;
    }
}
