package com.youcai.manage.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.youcai.manage.dataobject.*;
import com.youcai.manage.dto.deliver.ProductDTO;
import com.youcai.manage.dto.excel.deliver.Export;
import com.youcai.manage.dto.excel.deliver.ProductExport;
import com.youcai.manage.enums.ResultEnum;
import com.youcai.manage.exception.ManageException;
import com.youcai.manage.service.CategoryService;
import com.youcai.manage.service.DeliverService;
import com.youcai.manage.service.DriverService;
import com.youcai.manage.service.ProductService;
import com.youcai.manage.utils.ResultVOUtils;
import com.youcai.manage.utils.comparator.DateComparator;
import com.youcai.manage.utils.excel.deliver.ExportUtil;
import com.youcai.manage.vo.ResultVO;
import com.youcai.manage.vo.deliver.CategoryVO;
import com.youcai.manage.vo.deliver.ListVO;
import com.youcai.manage.vo.deliver.ProductVO;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/deliver")
public class DeliverController {
    @Autowired
    private DeliverService deliverService;
    @Autowired
    private DriverService driverService;
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
        Set<ListVO> listVOSet = deliverService.findListVOSet();
        return ResultVOUtils.success(getListVOPage(listVOSet, pageable));
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
        Set<ListVO> listVOSet = deliverService.findListVOSetByGuestName(guestName);
        return ResultVOUtils.success(getListVOPage(listVOSet, pageable));
    }

    @GetMapping("/listByDriverNameLike")
    public ResultVO<Page<ListVO>> listByDriverNameLike(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String driverName
    ){
        /*------------ 1.准备 -------------*/
        // 分页
        page = page<0 ? 0:page;
        size = size<=0 ? 10:size;
        Pageable pageable = new PageRequest(page, size);
        /*------------ 2.查询 -------------*/
        Set<ListVO> listVOSet = deliverService.findListVOSetByDriverName(driverName);
        return ResultVOUtils.success(getListVOPage(listVOSet, pageable));
    }

    private Page<ListVO> getListVOPage(Set<ListVO> listVOSet, Pageable pageable){
        List<ListVO> listVOS = new ArrayList<>();
        for (ListVO l : listVOSet){
            ListVO listVO = new ListVO(l);
            listVOS.add(listVO);
        }
        return new PageImpl<>(listVOS, pageable, listVOS.size());
    }

    @GetMapping("/findCategories")
    public ResultVO<List<CategoryVO>> findCategories(
            @RequestParam String guestId,
            @RequestParam Integer driverId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date date
    ){
        /*------------ 1.查询数据 -------------*/
        /*--- 产品大类数据 ---*/
        List<Category> categories = categoryService.findAll();
        /*--- 送货数据 ---*/
        List<Deliver> delivers = deliverService.findByGuestIdAndDriverIdAndDate(guestId, driverId, date);
        /*--- 产品数据 ---*/
        Map<String, Product> productMap = productService.findMap();
        /*------------ 2.数据拼装 -------------*/
        List<CategoryVO> categoryVOS = new ArrayList<>();
        for (Category category : categories){
            CategoryVO categoryVO = new CategoryVO();
            categoryVO.setCode(category.getCode());
            categoryVO.setName(category.getName());
            List<ProductVO> productVOS = new ArrayList<>();
            for (Deliver deliver : delivers){
                Product product = productMap.get(deliver.getId().getProductId());
                if (product.getPCode().equals(category.getCode())){
                    ProductVO productVO = new ProductVO();
                    productVO.setId(deliver.getId().getProductId());
                    productVO.setName(product.getName());
                    productVO.setUnit(product.getUnit());
                    productVO.setPrice(deliver.getPrice());
                    productVO.setNum(deliver.getNum());
                    productVO.setAmount(deliver.getAmount());
                    productVO.setNote(deliver.getNote());
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
            @RequestParam Integer driverId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date date
    ){
        deliverService.delete(guestId, driverId, date);
        return ResultVOUtils.success();
    }

    @PostMapping("/save")
    public ResultVO save(
            @RequestParam String guestId,
            @RequestParam Integer driverId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date date,
            @RequestParam String products
    ){
        List<ProductDTO> productDTOS = new ArrayList<>();
        try {
            productDTOS = new Gson().fromJson(products,
                    new TypeToken<List<ProductDTO>>() {
                    }.getType());
        } catch (Exception e) {
            throw new ManageException(ResultEnum.MANAGE_DELIVER_SAVE_JSON_PARSE_ERROR);
        }
        List<Deliver> delivers = productDTOS.stream().map(e ->
                new Deliver(new DeliverKey(driverId, guestId, date, e.getId()),
                        e.getPrice(), e.getNum(), e.getPrice().multiply(e.getNum()), e.getNote())
        ).collect(Collectors.toList());
        deliverService.save(delivers);
        return ResultVOUtils.success();
    }

    @GetMapping("/export")
    public ResponseEntity<byte[]> orderExport(
            @RequestParam String guestId,
            @RequestParam Integer driverId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date date
    ) throws IOException {
        Export export = deliverService.getExcelExport(guestId, driverId, date);
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
        /*--- 送货单 ---*/
        sheet.addMergedRegion(new CellRangeAddress(0,0,0,6));
        row = ExportUtil.createRow(rowNumber++, sheet);
        row.getCell(0).setCellValue("送货单");
        /*--- 送货单客户名&日期 ---*/
        sheet.addMergedRegion(new CellRangeAddress(1,1,0,6));
        row = ExportUtil.createRow(rowNumber++, sheet);
        row.getCell(0).setCellValue("客户："+export.getGuestName());
        row.getCell(4).setCellValue("日期："+new SimpleDateFormat("yyyy年 MM月 dd日").format(export.getDate()));
        /*------------ 内容区 -------------*/
        /*--- 表格头 ---*/
        row = ExportUtil.createRow(rowNumber++, sheet);
        String[] tableHeads = {"编号", "品名", "数量", "单位", "单价", "金额", "备注"};
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
        /*------------ 尾部区 -------------*/
        row = ExportUtil.createRowNoBorder(rowNumber++, sheet);
        row.getCell(0).setCellValue("送货人："+export.getDriverName());
        row.getCell(2).setCellValue("收货人：");
        row.getCell(6).setCellValue("出纳：");
        /*------------ 写入，返回 -------------*/
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDispositionFormData("attachment",
                "送货单 "+guestId+" "+new SimpleDateFormat("yyyy-MM-dd").format(export.getDate())+".xls");
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
        wb.write(outByteStream);
        return new ResponseEntity<byte[]>(outByteStream.toByteArray(), headers, HttpStatus.OK);
    }
}
