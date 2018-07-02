package com.youcai.manage.dto.excel.deliver;

import com.youcai.manage.dataobject.Driver;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class Export {
    private String id;
    private String guestName;
    private Driver driver;
    private Date date;
    private BigDecimal amount;
    private List<ProductExport> productExports;
}
