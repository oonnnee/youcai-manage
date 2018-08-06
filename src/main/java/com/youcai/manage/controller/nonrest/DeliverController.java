package com.youcai.manage.controller.nonrest;

import com.youcai.manage.dto.excel.deliver.Export;
import com.youcai.manage.dto.excel.deliver.ProductExport;
import com.youcai.manage.service.DeliverService;
import com.youcai.manage.utils.excel.deliver.ExportUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@RequestMapping("/deliver")
public class DeliverController {
    @Autowired
    private DeliverService deliverService;

    @GetMapping("/export")
    public ResponseEntity<byte[]> orderExport(
            @RequestParam String guestId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date date
    ) throws IOException {
        Export export = deliverService.getExcelExport(guestId, date);
        // create a new workbook
        XSSFWorkbook wb = new XSSFWorkbook();
        // create a sheet
        XSSFSheet sheet = wb.createSheet();
        // Row Cell CellStyle
        Row row = null;
        Cell cell = null;
        CellStyle cellStyle = null;
        Font font = null;
        int rowNumber = 0;
        // 默认设置
        ExportUtil.defaultSetting(sheet);
        /*------------ 头部区 -------------*/
        /*--- 送货单 ---*/
        sheet.addMergedRegion(new CellRangeAddress(0,0,0,6));
        row = ExportUtil.createRow(rowNumber++, sheet);
        row.getCell(0).setCellValue("送货单");
        /*--- 送货单客户名&日期 ---*/
        sheet.addMergedRegion(new CellRangeAddress(1,1,0,6));
        row = ExportUtil.createRow(rowNumber++, sheet);
        row.getCell(0).setCellValue("客户："+export.getGuestName());
        sheet.addMergedRegion(new CellRangeAddress(2,2,0,6));
        row = ExportUtil.createRow(rowNumber++, sheet);
        row.getCell(0).setCellValue("采购日期："+new SimpleDateFormat("yyyy年 MM月 dd日").format(export.getOrderDate()));
        /*------------ 内容区 -------------*/
        /*--- 表格头 ---*/
        row = ExportUtil.createRow(rowNumber++, sheet);
        String[] tableHeads = {"编号", "品名", "数量", "单位", "单价（元）", "金额（元）", "备注"};
        for (int i=0; i<tableHeads.length; i++){
            row.getCell(i).setCellValue(tableHeads[i]);
        }
        /*--- 表格体 ---*/
        for (ProductExport productExport : export.getProductExports()){
            row = ExportUtil.createRow(rowNumber++, sheet);
            String[] tds = {
                    productExport.getIndex().toString(),
                    productExport.getName(),
                    productExport.getNum().stripTrailingZeros().toPlainString(),
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
        /*------------ 尾部区 -------------*/
        sheet.addMergedRegion(new CellRangeAddress(rowNumber,rowNumber,0,2));
        sheet.addMergedRegion(new CellRangeAddress(rowNumber,rowNumber,3,6));
        row = ExportUtil.createRowNoBorder(rowNumber++, sheet);
        row.getCell(0).getCellStyle().setAlignment(CellStyle.ALIGN_LEFT);
        row.getCell(0).setCellValue("送货人："+export.getDriver().getName());
        row.getCell(3).getCellStyle().setAlignment(CellStyle.ALIGN_LEFT);
        row.getCell(3).setCellValue("送货人电话："+export.getDriver().getMobile());
        sheet.addMergedRegion(new CellRangeAddress(rowNumber,rowNumber,0,2));
        sheet.addMergedRegion(new CellRangeAddress(rowNumber,rowNumber,3,6));
        row = ExportUtil.createRowNoBorder(rowNumber++, sheet);
        row.getCell(0).getCellStyle().setAlignment(CellStyle.ALIGN_LEFT);
        row.getCell(0).setCellValue("收货人：");
        row.getCell(3).getCellStyle().setAlignment(CellStyle.ALIGN_LEFT);
        row.getCell(3).setCellValue("出纳：");
        /*------------ 写入，返回 -------------*/
        HttpHeaders headers = new HttpHeaders();
        String filename = new String("送货单 ".getBytes("UTF-8"), "iso-8859-1")
                + guestId + " " + new SimpleDateFormat("yyyy-MM-dd").format(date)+".xlsx";
        headers.setContentDispositionFormData("attachment", filename);
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
        wb.write(outByteStream);
        return new ResponseEntity<byte[]>(outByteStream.toByteArray(), headers, HttpStatus.OK);
    }
}
