package com.youcai.manage.controller.nonrest;

import com.youcai.manage.dto.excel.pricelist.CategoryExport;
import com.youcai.manage.dto.excel.pricelist.Export;
import com.youcai.manage.dto.excel.pricelist.ProductExport;
import com.youcai.manage.service.PricelistService;
import com.youcai.manage.utils.excel.pricelist.ExportUtil;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
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
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/pricelist")
public class PricelistController {

    @Autowired
    private PricelistService pricelistService;

    @GetMapping("/export")
    public ResponseEntity<byte[]> pricelistExport(
            @RequestParam String guestId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date date
    ) throws IOException {
        Export export = pricelistService.getExcelExport(guestId, date);
        export.setId("yc-160546164");

        // create a new workbook
        HSSFWorkbook wb = new HSSFWorkbook();
        // create a sheet
        HSSFSheet sheet = wb.createSheet();
        // Row Cell CellStyle
        Row row = null;
        Cell cell = null;
        CellStyle cellStyle = null;
        Font font = null;

        // 默认设置
        ExportUtil.defaultSetting(sheet);

        // build header
        // 第 1 2 3 行
        sheet.addMergedRegion(new CellRangeAddress(0, 2, 0, 1));
        ExportUtil.insertImage(sheet, ExportUtil.LOGO_IMAGE_NAME, new HSSFClientAnchor(300, 0, 450, 200,(short) 0, 0, (short) 1, 2));

        sheet.addMergedRegion(new CellRangeAddress(0, 0, 2, 10));
        row = ExportUtil.createRowNoBorder(0, sheet, ExportUtil.CELL_HEIGHT_LG);
        cell = row.getCell(2);
        cell.getCellStyle().setAlignment(CellStyle.ALIGN_GENERAL);
        font = wb.createFont();
        font.setFontName(ExportUtil.FONT_NAME);
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        font.setFontHeightInPoints((short) 14);
        cell.getCellStyle().setFont(font);
        cell.setCellValue("广东优菜农业发展有限公司");

        sheet.addMergedRegion(new CellRangeAddress(1, 1, 2, 10));
        row = ExportUtil.createRowNoBorder(1, sheet, ExportUtil.CELL_HEIGHT_MID);
        cell = row.getCell(2);
        cell.getCellStyle().setAlignment(CellStyle.ALIGN_GENERAL);
        cell.setCellValue("公司地址：东莞市道滘镇蔡白农贸市场27/28/29号一层铺面及二层办公室");

        sheet.addMergedRegion(new CellRangeAddress(2, 2, 2, 10));
        row = ExportUtil.createRowNoBorder(2, sheet, ExportUtil.CELL_HEIGHT_MID);
        cell = row.getCell(2);
        cell.getCellStyle().setAlignment(CellStyle.ALIGN_GENERAL);
        cell.setCellValue("经营范围：农产品的种植与销售；销售：蔬菜；货物进出口；技术进出口；承包食堂");

        // 第 4 5 行
        for (int i=3; i<=4; i++){
            sheet.addMergedRegion(new CellRangeAddress(i, i, 0, 1));
            sheet.addMergedRegion(new CellRangeAddress(i, i, 2, 3));
            sheet.addMergedRegion(new CellRangeAddress(i, i, 4, 5));
            sheet.addMergedRegion(new CellRangeAddress(i, i, 6, 10));
        }
        row = ExportUtil.createRow(3, sheet, ExportUtil.CELL_HEIGHT_MID);
        cell = row.getCell(0);
        cell.setCellValue("联系人");
        cell = row.getCell(2);
        cell.setCellValue("报价单号");
        cell = row.getCell(4);
        cell.setCellValue("联系电话");
        cell = row.getCell(6);
        cell.setCellValue("电子邮件");
        row = ExportUtil.createRow(4, sheet, ExportUtil.CELL_HEIGHT_MID);
        cell = row.getCell(2);
        cell.setCellValue(export.getId());

        // build body
        // 第 6 行
        ExportUtil.createRowNoBorder(5, sheet);
        sheet.addMergedRegion(new CellRangeAddress(5, 5, 0, 10));
        // 第 7 行
        sheet.addMergedRegion(new CellRangeAddress(6, 6, 0, 10));
        row = ExportUtil.createRow(6, sheet, ExportUtil.CELL_HEIGHT_MID);
        cell = row.getCell(0);
        cell.getCellStyle().setFillForegroundColor(IndexedColors.LIGHT_TURQUOISE.getIndex());
        cell.getCellStyle().setFillPattern(CellStyle.SOLID_FOREGROUND);
        font = wb.createFont();
        font.setFontName(ExportUtil.FONT_NAME);
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        font.setFontHeightInPoints((short) 12);
        cell.getCellStyle().setFont(font);
        cell.setCellValue(
                new SimpleDateFormat("yyyy年MM月dd日").format(export.getPdate())
                        + "报价清单（有效期：" + export.getExpire() + "天）");
        // 第 8 行
        row = ExportUtil.createRow(7, sheet);
        for (int i=0; i<5; i++){
            cell = row.getCell(1+2*i);
            cell.setCellValue("名称");
            cell = row.getCell(2+2*i);
            cell.setCellValue("单价/元");
        }
        // 报价部分
        int firstRow = 0;
        int lastRow = 6;
        for (CategoryExport categoryExport : export.getCategoryExports()){
            int rowCount = (categoryExport.getProductExports().size()-1) / 5 + 1;
            firstRow = lastRow + 2;
            lastRow = firstRow + rowCount - 1;
            sheet.addMergedRegion(new CellRangeAddress(firstRow, lastRow, 0, 0));
            for (int i=firstRow; i<=lastRow; i++){
                row = ExportUtil.createRow(i, sheet);
            }
            row = sheet.getRow(firstRow);
            cell = row.getCell(0);
            cell.setCellValue(categoryExport.getCategoryName());
            List<ProductExport> productExports = categoryExport.getProductExports();
            int rowIndex = firstRow;
            int colIndex = 1;
            for (ProductExport productExport : productExports){
                row = sheet.getRow(rowIndex);
                cell = row.getCell(colIndex);
                cell.setCellValue(productExport.getName());
                font = wb.createFont();
                font.setFontName(ExportUtil.FONT_NAME);
                if (productExport.getName().length() > 8){
                    font.setFontHeightInPoints((short)6);
                    cell.getCellStyle().setFont(font);
                }else if (productExport.getName().length() > 5){
                    font.setFontHeightInPoints((short)8);
                    cell.getCellStyle().setFont(font);
                }
                cell = row.getCell(colIndex+1);
                String price = productExport.getPrice().subtract(BigDecimal.ZERO).abs()
                        .compareTo(new BigDecimal(0.01)) == -1 ?
                        "暂无" : productExport.getPrice().toString();
                cell.setCellValue(price);
                rowIndex++;
                if (rowIndex > lastRow){
                    rowIndex = firstRow;
                    colIndex += 2;
                }
            }
        }

        // footer
        int rowIndex = lastRow + 2;
        sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 0, 10));
        row = ExportUtil.createRowNoBorder(rowIndex, sheet);
        cell = row.getCell(0);
        cell.getCellStyle().setAlignment(CellStyle.ALIGN_GENERAL);
        cell.setCellValue("说明：");
        rowIndex++;

        sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 0, 10));
        row = ExportUtil.createRowNoBorder(rowIndex, sheet);
        cell = row.getCell(0);
        cell.getCellStyle().setAlignment(CellStyle.ALIGN_GENERAL);
        cell.setCellValue("1.报价日期:"
                +new SimpleDateFormat("yyyy/MM/dd").format(export.getPdate()));
        rowIndex++;

        sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 0, 10));
        row = ExportUtil.createRowNoBorder(rowIndex, sheet);
        cell = row.getCell(0);
        cell.getCellStyle().setAlignment(CellStyle.ALIGN_GENERAL);
        cell.setCellValue("2.以上价格不含税,如需开票另行商议。如有新菜品，则价格以实际送货单所标当天价格为准。");
        rowIndex++;

        sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 0, 10));
        row = ExportUtil.createRowNoBorder(rowIndex, sheet);
        cell = row.getCell(0);
        cell.getCellStyle().setAlignment(CellStyle.ALIGN_GENERAL);
        cell.setCellValue("3.价格会随行就市正常波动，一般7-10天进行更新，以最新报价为准。");
        /*------------ 返回 -------------*/
        HttpHeaders headers = new HttpHeaders();
        String filename = new String("报价单 ".getBytes("UTF-8"), "iso-8859-1")
                + guestId + " " + new SimpleDateFormat("yyyy-MM-dd").format(date)+".xls";
        headers.setContentDispositionFormData("attachment", filename);
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
        wb.write(outByteStream);
        return new ResponseEntity<byte[]>(outByteStream.toByteArray(), headers, HttpStatus.OK);
    }
}
