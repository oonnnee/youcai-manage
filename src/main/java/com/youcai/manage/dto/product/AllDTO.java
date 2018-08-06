package com.youcai.manage.dto.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AllDTO {
    private String category;
    private String id;
    private String name;
    private String unit;
    private BigDecimal price;
    private String imgfile;

    public AllDTO(Object o, Object o1, Object o2, Object o3, Object o4, Object o5) {
        super();
        if (o instanceof  String) this.category = (String)o;
        if (o1 instanceof  String) this.id = (String)o1;
        if (o2 instanceof  String) this.name = (String)o2;
        if (o3 instanceof  String) this.unit = (String)o3;
        if (o4 instanceof  BigDecimal) this.price = (BigDecimal)o4;
        if (o5 instanceof  String) this.imgfile = (String)o5;
    }
}
