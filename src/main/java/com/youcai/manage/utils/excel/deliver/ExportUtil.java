package com.youcai.manage.utils.excel.deliver;

import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.Arrays;


public class ExportUtil {
    public static final short ROW_HEIGHT = (short)0x1C0;
    public static final String FONT_NAME = "宋体";
    public static final int COL_COUNT = 7;

    public static void defaultSetting(Sheet sheet){
        Integer[] widths = {3, 10, 5, 5, 5, 7, 10};
        for (int i=0; i<widths.length; i++){
            sheet.setColumnWidth(i, widths[i]*512);
        }
    }

    public static Row createRow(int rowNumber, HSSFSheet sheet){
        HSSFWorkbook workbook = sheet.getWorkbook();
        Row row = sheet.createRow(rowNumber);
        row.setHeight(ROW_HEIGHT);
        for (int i=0; i<COL_COUNT; i++){
            CellStyle cellStyle = workbook.createCellStyle();
            setDefaultCellStyle(cellStyle);
            setBorder(cellStyle);
            HSSFFont font = workbook.createFont();
            setDefaultFont(font);
            cellStyle.setFont(font);
            row.createCell(i).setCellStyle(cellStyle);
        }
        return row;
    }

    public static Row createRowNoBorder(int rowNumber, HSSFSheet sheet){
        HSSFWorkbook workbook = sheet.getWorkbook();
        Row row = sheet.createRow(rowNumber);
        row.setHeight(ROW_HEIGHT);
        CellStyle cellStyle = workbook.createCellStyle();
        setDefaultCellStyle(cellStyle);
        HSSFFont font = workbook.createFont();
        setDefaultFont(font);
        cellStyle.setFont(font);
        for (int i=0; i<COL_COUNT; i++){
            row.createCell(i).setCellStyle(cellStyle);
        }
        return row;
    }

    private static void setBorder(CellStyle cellStyle){
        cellStyle.setBorderBottom(CellStyle.BORDER_THIN);
        cellStyle.setBorderTop(CellStyle.BORDER_THIN);
        cellStyle.setBorderLeft(CellStyle.BORDER_THIN);
        cellStyle.setBorderRight(CellStyle.BORDER_THIN);
    }

    private static void setDefaultFont(HSSFFont font){
        font.setFontName(FONT_NAME);
        font.setFontHeightInPoints((short) 12);
    }

    private static void setDefaultCellStyle(CellStyle cellStyle){
        cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
    }
}
