package com.youcai.manage.utils.excel.order;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.util.ResourceUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;


public class ExportUtil {

    /*------------ 文件 -------------*/
    public static final String LOGO_IMAGE_NAME = "classpath:img/logo.png";

    /*------------ 单元格 -------------*/
    public static final short CELL_HEIGHT_SM = (short)0x140;
    public static final short CELL_HEIGHT_MID = (short)0x1B0;
    public static final short CELL_HEIGHT_LG = (short)0x210;

    public static final int CELL_WIDTH_DEFAULT = 11;

    /*------------ 样式 -------------*/
    public static final String FONT_NAME = "宋体";

    public static final int COL_COUNT = 11;

    public static void main(String args[]){
        String[] split = "work.png".split("\\.");
        System.out.println(Arrays.toString(split));
    }

    public static void defaultSetting(Sheet sheet){
        sheet.setDefaultColumnWidth(CELL_WIDTH_DEFAULT);
    }

    public static Row createRow(int rowNumber, HSSFSheet sheet, int rowHeight){
        HSSFWorkbook workbook = sheet.getWorkbook();
        Row row = sheet.createRow(rowNumber);
        row.setHeight((short)rowHeight);
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

    public static Row createRow(int rowNumber, HSSFSheet sheet){
        HSSFWorkbook workbook = sheet.getWorkbook();
        Row row = sheet.createRow(rowNumber);
        row.setHeight(CELL_HEIGHT_SM);
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


    public static Row createRowNoBorder(int rowNumber, HSSFSheet sheet, int rowHeight){
        HSSFWorkbook workbook = sheet.getWorkbook();
        Row row = sheet.createRow(rowNumber);
        row.setHeight((short)rowHeight);
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

    public static Row createRowNoBorder(int rowNumber, HSSFSheet sheet){
        HSSFWorkbook workbook = sheet.getWorkbook();
        Row row = sheet.createRow(rowNumber);
        row.setHeight(CELL_HEIGHT_SM);
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

    public static void insertImage(HSSFSheet sheet, String img, HSSFClientAnchor anchor) throws IOException {
        String[] parts = img.split("\\.");
        String suffix = parts[parts.length-1];
        Workbook workbook = sheet.getWorkbook();
        BufferedImage bufferImg = null;
        ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
        bufferImg = ImageIO.read(ResourceUtils.getFile(img));
        ImageIO.write(bufferImg, suffix, byteArrayOut);
        HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
        patriarch.createPicture(anchor, workbook.addPicture(byteArrayOut.toByteArray(), ExportUtil.pictureType(suffix)));
    }

    private static void setBorder(CellStyle cellStyle){
        cellStyle.setBorderBottom(CellStyle.BORDER_THIN);
        cellStyle.setBorderTop(CellStyle.BORDER_THIN);
        cellStyle.setBorderLeft(CellStyle.BORDER_THIN);
        cellStyle.setBorderRight(CellStyle.BORDER_THIN);
    }

    private static int pictureType(String suffix){
        int pictureType = 0;
        if (suffix.equalsIgnoreCase("JPEG") ||
                suffix.equalsIgnoreCase("JPG") ){
            pictureType = HSSFWorkbook.PICTURE_TYPE_JPEG;
        }else if (suffix.equalsIgnoreCase("PNG")){
            pictureType = HSSFWorkbook.PICTURE_TYPE_PNG;
        }
        return pictureType;
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
