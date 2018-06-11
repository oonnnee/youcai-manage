package com.youcai.manage.dto.excel.pricelist;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class Export {
    private String id;
    private Date pdate;
    private Integer expire;
    private List<CategoryExport> categoryExports;
}
