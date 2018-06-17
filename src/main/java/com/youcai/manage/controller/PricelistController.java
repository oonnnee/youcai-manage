package com.youcai.manage.controller;

import com.youcai.manage.dataobject.*;
import com.youcai.manage.dto.PricelistDTO;
import com.youcai.manage.dto.excel.pricelist.CategoryExport;
import com.youcai.manage.dto.excel.pricelist.Export;
import com.youcai.manage.dto.excel.pricelist.ProductExport;
import com.youcai.manage.enums.ResultEnum;
import com.youcai.manage.exception.ManageException;
import com.youcai.manage.service.CategoryService;
import com.youcai.manage.service.GuestService;
import com.youcai.manage.service.PricelistService;
import com.youcai.manage.service.ProductService;
import com.youcai.manage.utils.ResultVOUtils;
import com.youcai.manage.utils.comparator.DateComparator;
import com.youcai.manage.utils.excel.pricelist.ExportUtil;
import com.youcai.manage.vo.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/pricelist")
public class PricelistController {

    @Autowired
    private PricelistService pricelistService;

    @Autowired
    private GuestService guestService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;


    @GetMapping("/listByGuestIdLike")
    public ResultVO<Page<PricelistDateVO>> listByGuestIdLike(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String guestId
    ){
        /*------------ 1.准备 -------------*/
        // 分页
        page = page<0 ? 0:page;
        size = size<=0 ? 10:size;
        Pageable pageable = new PageRequest(page, size);

        /*------------ 2.查询 -------------*/
        Page<Guest> guestPage = guestService.findByIdLike(guestId, pageable);
        return ResultVOUtils.success(this.getPricelistDateVO(guestPage, pageable));
    }

    @GetMapping("/listByGuestNameLike")
    public ResultVO<Page<PricelistDateVO>> listByGuestNameLike(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String guestName
    ){
        /*------------ 1.准备 -------------*/
        // 分页
        page = page<0 ? 0:page;
        size = size<=0 ? 10:size;
        Pageable pageable = new PageRequest(page, size);

        /*------------ 2.查询 -------------*/
        Page<Guest> guestPage = guestService.findByNameLike(guestName, pageable);
        return ResultVOUtils.success(this.getPricelistDateVO(guestPage, pageable));
    }


    @GetMapping("/list")
    public ResultVO<Page<PricelistDateVO>> list(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ){
        /*------------ 1.准备 -------------*/
        // 分页
        page = page<0 ? 0:page;
        size = size<=0 ? 10:size;
        Pageable pageable = new PageRequest(page, size);
        /*------------ 2.查询 -------------*/
        Page<Guest> guestPage = guestService.findAll(pageable);
        return ResultVOUtils.success(this.getPricelistDateVO(guestPage, pageable));
    }

    private Page<PricelistDateVO> getPricelistDateVO(Page<Guest> guestPage, Pageable pageable){
        List<PricelistDateVO> pricelistDateVOS = new ArrayList<>();
        for (Guest guest : guestPage.getContent()){
            if (guest.getId().equals("admin")){
                continue;
            }
            List<Pricelist> pricelists = pricelistService.findById_GuestId(guest.getId());
            Set<Date> dates = new TreeSet<>(new DateComparator());
            for (Pricelist pricelist : pricelists){
                dates.add(pricelist.getId().getPdate());
            }
            PricelistDateVO pricelistVO = new PricelistDateVO(guest.getId(), guest.getName(), dates);
            pricelistDateVOS.add(pricelistVO);
        }
        Page<PricelistDateVO> pricelistDateVOPage = new PageImpl<PricelistDateVO>(pricelistDateVOS, pageable, guestPage.getTotalElements());
        return pricelistDateVOPage;
    }

    @PostMapping({"/save", "update"})
    public ResultVO save(
            @RequestParam String guestId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date date,
            @RequestParam String products
    ){
        List<PricelistDTO> pricelistDTOS = new ArrayList<>();
        try {
            pricelistDTOS = new Gson().fromJson(products,
                    new TypeToken<List<PricelistDTO>>() {
                    }.getType());
        } catch (Exception e) {
            throw new ManageException(ResultEnum.MANAGE_PRICELIST_SAVE_JSON_PARSE_ERROR);
        }
        List<Pricelist> pricelists = pricelistDTOS.stream().map(e ->
                new Pricelist(new PricelistKey(date, guestId, e.getProductId()),
                        e.getPrice(), e.getNote())
        ).collect(Collectors.toList());
        pricelistService.save(pricelists);
        return ResultVOUtils.success();
    }

    @PostMapping("/delete")
    public ResultVO delete(
            @RequestParam String guestId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date date
    ){
        pricelistService.delete(guestId, date);
        return ResultVOUtils.success();
    }

    @GetMapping("/find")
    public ResultVO find(
            @RequestParam String guestId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date date
    ){
        List<PricelistDTO> pricelistDTOS = new ArrayList<>();

        List<Pricelist> pricelists = pricelistService.findById_GuestIdAndId_pdate(guestId, date);
        for (Pricelist pricelist : pricelists){
            String productId = pricelist.getId().getProductId();
            Product product = productService.findOne(productId);
            PricelistDTO pricelistDTO = new PricelistDTO();
            pricelistDTO.setProductId(productId);
            pricelistDTO.setProductCode(product.getPCode());
            pricelistDTO.setProductName(product.getName());
            pricelistDTO.setProductImg(product.getImgfile());
            pricelistDTO.setProductUnit(product.getUnit());
            pricelistDTO.setPrice(pricelist.getPrice());
            pricelistDTO.setNote(pricelist.getNote());
            pricelistDTOS.add(pricelistDTO);
        }

        PricelistVO pricelistVO = new PricelistVO();

        pricelistVO.setGuestId(guestId);
        pricelistVO.setPdate(date);
        pricelistVO.setPricelistDTOS(pricelistDTOS);

        return ResultVOUtils.success(pricelistVO);
    }

    @GetMapping("/findDatesByGuestId")
    public ResultVO<List<Date>> findDatesByGuestId(
            @RequestParam String guestId
    ){
        List<Date> dates = pricelistService.findPdatesByGuestId(guestId);
        return ResultVOUtils.success(dates);
    }

    @GetMapping("/findCategories")
    public ResultVO<List<FindByGuestIdAndPdateWithCategoryVO>> findCategories(
            @RequestParam String guestId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date date
    ){
        /*------------ 1.查询数据 -------------*/
        /*--- 产品大类数据 ---*/
        List<Category> categories = categoryService.findAll();
        /*--- 报价数据 ---*/
        List<Pricelist> pricelists = pricelistService.findById_GuestIdAndId_pdate(guestId, date);
        /*--- 产品数据 ---*/
        Map<String, Product> productMap = productService.findMap();
        /*------------ 2.数据拼装 -------------*/
        List<FindByGuestIdAndPdateWithCategoryVO> findByGuestIdAndPdateWithCategoryVOS = new ArrayList<>();
        for (Category category : categories){
            FindByGuestIdAndPdateWithCategoryVO findByGuestIdAndPdateWithCategoryVO = new FindByGuestIdAndPdateWithCategoryVO();
            findByGuestIdAndPdateWithCategoryVO.setCode(category.getCode());
            findByGuestIdAndPdateWithCategoryVO.setName(category.getName());
            List<FindByGuestIdAndPdateVO> findByGuestIdAndPdateVOS = new ArrayList<>();
            for (Pricelist pricelist : pricelists){
                Product product = productMap.get(pricelist.getId().getProductId());
                if (product.getPCode().equals(category.getCode())){
                    FindByGuestIdAndPdateVO findByGuestIdAndPdateVO = new FindByGuestIdAndPdateVO();
                    findByGuestIdAndPdateVO.setProductId(pricelist.getId().getProductId());
                    findByGuestIdAndPdateVO.setProductName(product.getName());
                    findByGuestIdAndPdateVO.setPrice(pricelist.getPrice());
                    findByGuestIdAndPdateVO.setNote(pricelist.getNote());
                    findByGuestIdAndPdateVOS.add(findByGuestIdAndPdateVO);
                }
            }
            findByGuestIdAndPdateWithCategoryVO.setFindByGuestIdAndPdateVOS(findByGuestIdAndPdateVOS);
            findByGuestIdAndPdateWithCategoryVOS.add(findByGuestIdAndPdateWithCategoryVO);
        }
        /*------------ 3.返回 -------------*/
        return ResultVOUtils.success(findByGuestIdAndPdateWithCategoryVOS);
    }

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
        headers.setContentDispositionFormData("attachment",
                        "报价单 "+guestId+" "+new SimpleDateFormat("yyyy-MM-dd").format(date)+".xls");
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
        wb.write(outByteStream);
        return new ResponseEntity<byte[]>(outByteStream.toByteArray(), headers, HttpStatus.OK);
    }
}
