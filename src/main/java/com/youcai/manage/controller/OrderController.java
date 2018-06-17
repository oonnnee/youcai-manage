package com.youcai.manage.controller;

import com.google.gson.Gson;
import com.youcai.manage.dataobject.Category;
import com.youcai.manage.dataobject.Guest;
import com.youcai.manage.dataobject.Product;
import com.youcai.manage.dto.excel.order.Export;
import com.youcai.manage.dto.excel.order.ProductExport;
import com.youcai.manage.service.CategoryService;
import com.youcai.manage.service.GuestService;
import com.youcai.manage.service.OrderService;
import com.youcai.manage.service.ProductService;
import com.youcai.manage.utils.ResultVOUtils;
import com.youcai.manage.utils.comparator.DateComparator;
import com.youcai.manage.utils.excel.order.ExportUtil;
import com.youcai.manage.vo.ResultVO;
import com.youcai.manage.vo.order.CategoryVO;
import com.youcai.manage.vo.order.ListVO;
import com.youcai.manage.vo.order.ProductVO;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
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
import com.youcai.manage.dataobject.Order;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private GuestService guestService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductService productService;
    
    @GetMapping("/list")
    public ResultVO<Page<ListVO>> list(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ){
        /*------------ 1.准备 -------------*/
        // 分页
        page = page<0 ? 0:page;
        size = size<=0 ? 10:size;
        Pageable pageable = new PageRequest(page, size);
        /*------------ 2.查询 -------------*/
        Page<Guest> guestPage = orderService.findGuestPage(pageable);
        return ResultVOUtils.success(this.getListVOPage(guestPage, pageable));
    }

    @GetMapping("/listByGuestIdLike")
    public ResultVO<Page<ListVO>> listByGuestIdLike(
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
        Page<Guest> guestIdPage = orderService.findGuestPageByGuestIdLike(pageable, guestId);
        return ResultVOUtils.success(this.getListVOPage(guestIdPage, pageable));
    }

    @GetMapping("/listByGuestNameLike")
    public ResultVO<Page<ListVO>> listByGuestNameLike(
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
        Page<Guest> guestIdPage = orderService.findGuestPageByGuestNameLike(pageable, guestName);
        return ResultVOUtils.success(this.getListVOPage(guestIdPage, pageable));
    }

    private Page<ListVO> getListVOPage(Page<Guest> guestPage, Pageable pageable){
        List<ListVO> listVOS = new ArrayList<>();
        for (Guest guest : guestPage.getContent()){
            List<Order> orders = orderService.findByIdGuestId(guest.getId());
            Set<Date> dates = new TreeSet<>(new DateComparator());
            for (Order order : orders){
                dates.add(order.getId().getOdate());
            }
            ListVO listVO = new ListVO(guest.getId(), guest.getName(), dates);
            listVOS.add(listVO);
        }
        Page<ListVO> listVOPage = new PageImpl<ListVO>(listVOS, pageable, guestPage.getTotalElements());
        return listVOPage;
    }

    @GetMapping("/findDatesByGuestId")
    public ResultVO<List<Date>> findDatesByGuestId(
            @RequestParam String guestId
    ){
        List<Date> dates = orderService.findDatesByGuestId(guestId);
        return ResultVOUtils.success(dates);
    }

    @GetMapping("/findCategories")
    public ResultVO<List<CategoryVO>> findCategories(
            @RequestParam String guestId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date date
    ){
        /*------------ 1.查询数据 -------------*/
        /*--- 产品大类数据 ---*/
        List<Category> categories = categoryService.findAll();
        /*--- 采购数据 ---*/
        List<Order> orders = orderService.findByIdGuestIdAndIdDate(guestId, date);
        /*--- 产品数据 ---*/
        Map<String, Product> productMap = productService.findMap();
        /*------------ 2.数据拼装 -------------*/
        List<CategoryVO> categoryVOS = new ArrayList<>();
        for (Category category : categories){
            CategoryVO categoryVO = new CategoryVO();
            categoryVO.setCode(category.getCode());
            categoryVO.setName(category.getName());
            List<ProductVO> productVOS = new ArrayList<>();
            for (Order order : orders){
                Product product = productMap.get(order.getId().getProductId());
                if (product.getPCode().equals(category.getCode())){
                    ProductVO productVO = new ProductVO();
                    productVO.setId(order.getId().getProductId());
                    productVO.setName(product.getName());
                    productVO.setUnit(product.getUnit());
                    productVO.setPrice(order.getPrice());
                    productVO.setNum(order.getNum());
                    productVO.setAmount(order.getAmount());
                    productVO.setNote(order.getNote());
                    productVOS.add(productVO);
                }
            }
            categoryVO.setProducts(productVOS);
            categoryVOS.add(categoryVO);
        }
        /*------------ 3.返回 -------------*/
        return ResultVOUtils.success(categoryVOS);
    }

    @PostMapping("/delete")
    public ResultVO delete(
            @RequestParam String guestId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date date
    ){
        orderService.delete(guestId, date);
        return ResultVOUtils.success();
    }

    @GetMapping("/export")
    public ResponseEntity<byte[]> orderExport(
            @RequestParam String guestId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date date
    ) throws IOException {
        Export export = orderService.getExcelExport(guestId, date);
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
        /*------------ 写入，返回 -------------*/
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDispositionFormData("attachment",
                "采购单 "+guestId+" "+new SimpleDateFormat("yyyy-MM-dd").format(export.getDate())+".xls");
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
        wb.write(outByteStream);
        return new ResponseEntity<byte[]>(outByteStream.toByteArray(), headers, HttpStatus.OK);
    }
}
