package com.youcai.manage.dto.excel.deliver;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class Export {
    private String id;
    private String guestName;
    private String driverName;
    private Date date;
    private BigDecimal amount;
    private List<ProductExport> productExports;
}
