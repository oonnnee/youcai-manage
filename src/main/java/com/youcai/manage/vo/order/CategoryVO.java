package com.youcai.manage.vo.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryVO {
    private String code;
    private String name;
    private List<ProductVO> products;
}
