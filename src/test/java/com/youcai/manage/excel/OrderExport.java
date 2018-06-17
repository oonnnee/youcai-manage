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
    
    @Test
    public void main() throws IOException, ParseException {
        Export export = orderService.getExcelExport("1442933609", new SimpleDateFormat("yyyy-MM-dd").parse("2018-06-17"));
        // create a new workbook
        HSSFWorkbook wb = new HSSFWorkbook();
        // create a sheet
        HSSFSheet sheet = wb.createSheet();
        // Row Cell CellStyle
        Row row = null;
        Cell cell = null;
        CellStyle cellStyle = null;
        Font font = null;
        int rowNumber = 0;
        // 默认设置
        ExportUtil.defaultSetting(sheet);
        /*------------ 头部区 -------------*/
        /*--- 采购单名 ---*/
        sheet.addMergedRegion(new CellRangeAddress(0,0,0,6));
        row = ExportUtil.createRow(rowNumber++, sheet);
        row.getCell(0).setCellValue(export.getGuestName()+"采购单");
        /*--- 采购单日期 ---*/
        sheet.addMergedRegion(new CellRangeAddress(1,1,0,6));
        row = ExportUtil.createRow(rowNumber++, sheet);
        row.getCell(0).setCellValue(new SimpleDateFormat("yyyy年 MM月 dd日").format(export.getDate()));
        /*------------ 内容区 -------------*/
        /*--- 表格头 ---*/
        row = ExportUtil.createRow(rowNumber++, sheet);
        String[] tableHeads = {"编号", "品名", "下单量", "单位", "单价", "金额", "备注"};
        for (int i=0; i<tableHeads.length; i++){
            row.getCell(i).setCellValue(tableHeads[i]);
        }
        /*--- 表格体 ---*/
        for (ProductExport productExport : export.getProductExports()){
            row = ExportUtil.createRow(rowNumber++, sheet);
            String[] tds = {
                    productExport.getIndex().toString(),
                    productExport.getName(),
                    productExport.getNum().stripTrailingZeros().toString(),
                    productExport.getUnit(),
                    productExport.getPrice().toString(),
                    productExport.getAmount().toString(),
                    productExport.getNote()
            };
            for (int i=0; i<tds.length; i++){
                row.getCell(i).setCellValue(tds[i]);
            }
        }
        /*--- 表格尾 ---*/
        row = ExportUtil.createRow(rowNumber++, sheet);
        row.getCell(4).setCellValue("合计：");
        row.getCell(5).setCellValue(export.getAmount().toString());
        /*------------ 输出 -------------*/
        FileOutputStream fos = new FileOutputStream("order.xls");
        wb.write(fos);
        fos.close();
    }

    public static void main(String[] args){
        System.out.println(new BigDecimal(1.2).stripTrailingZeros().toString());
    }
}
