package com.youcai.manage.vo;

import com.youcai.manage.dataobject.Product;
import lombok.Data;

import java.util.List;

@Data
public class CategoryWithProductsVO {

    private String categoryCode;
    private String categoryName;
    private String note;

    private List<Product> products;

}
