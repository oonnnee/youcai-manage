package com.youcai.manage.excel;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.youcai.manage.dataobject.Order;
import com.youcai.manage.dto.excel.order.Export;
import com.youcai.manage.dto.excel.order.ProductExport;
import com.youcai.manage.service.OrderService;
import com.youcai.manage.utils.excel.order.ExportUtil;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class OrderExport {

    @Autowired
    private OrderService orderService;


    public static void main(String[] args){
        System.out.println(new BigDecimal(1.2).stripTrailingZeros().toString());
    }
}
