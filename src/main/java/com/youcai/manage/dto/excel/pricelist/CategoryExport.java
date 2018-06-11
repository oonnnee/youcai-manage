package com.youcai.manage.dto.excel.pricelist;

import lombok.Data;

import java.util.List;

@Data
public class CategoryExport {
    private String categoryName;
    private List<ProductExport> productExports;
}
