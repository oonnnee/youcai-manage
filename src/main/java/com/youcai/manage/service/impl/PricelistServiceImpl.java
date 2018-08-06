package com.youcai.manage.service.impl;

import com.youcai.manage.dataobject.Category;
import com.youcai.manage.dataobject.Pricelist;
import com.youcai.manage.dataobject.PricelistKey;
import com.youcai.manage.dataobject.Product;
import com.youcai.manage.dto.excel.pricelist.CategoryExport;
import com.youcai.manage.dto.excel.pricelist.Export;
import com.youcai.manage.dto.excel.pricelist.ProductExport;
import com.youcai.manage.dto.pricelist.AllDTO;
import com.youcai.manage.repository.PricelistRepository;
import com.youcai.manage.service.CategoryService;
import com.youcai.manage.service.PricelistService;
import com.youcai.manage.service.ProductService;
import com.youcai.manage.transform.PricelistTransform;
import com.youcai.manage.utils.ManageUtils;
import com.youcai.manage.vo.pricelist.PricelistsVO;
import com.youcai.manage.vo.pricelist.ProductVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PricelistServiceImpl implements PricelistService {

    private void checkRepeat(String guestId, Date date){
        ManageUtils.ManageException(
                this.isRepeat(guestId, date),
                ManageUtils.toErrorString("新增报价失败", "该客户当天已经存在报价了")
        );
    }


    @Autowired
    private PricelistRepository pricelistRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Override
    @Transactional
    public void save(List<Pricelist> pricelists) {
        ManageUtils.ManageException(
                CollectionUtils.isEmpty(pricelists),
                ManageUtils.toErrorString("新增报价失败", "报价产品为空")
        );

        PricelistKey pricelistKey = pricelists.get(0).getId();
        this.checkRepeat(pricelistKey.getGuestId(), pricelistKey.getPdate());

        List<Pricelist> saveResult = pricelistRepository.save(pricelists);
        ManageUtils.ManageException(saveResult, ManageUtils.toErrorString("新增报价失败", "服务器繁忙，请稍后再试"));
    }

    @Override
    @Transactional
    public void update(List<Pricelist> pricelists) {
        ManageUtils.ManageException(
                CollectionUtils.isEmpty(pricelists),
                ManageUtils.toErrorString("更新报价失败", "更新产品为空")
        );

        List<Pricelist> updateResult = pricelistRepository.save(pricelists);
        ManageUtils.ManageException(updateResult, ManageUtils.toErrorString("更新报价失败", "服务器繁忙，请稍后再试"));
    }

    @Override
    public List<Pricelist> findById_GuestId(String guestId) {
        List<Pricelist> pricelists = pricelistRepository.findById_GuestId(guestId);

        return pricelists;
    }

    @Override
    @Transactional
    public void delete(String guestId, Date pdate) {
        pricelistRepository.deleteById_GuestIdAndId_Pdate(guestId, pdate);
    }

    @Override
    public List<Pricelist> findById_GuestIdAndId_pdate(String guestId, Date pdate) {
        return pricelistRepository.findById_GuestIdAndId_Pdate(guestId, pdate);
    }

    @Override
    public List<Date> findPdatesByGuestId(String guestId) {
        List<Date> dates = pricelistRepository.findDistinctId_PdateById_GuestId(guestId);

        return dates;
    }

    @Override
    public Map<String, Pricelist> findProductIdMap(String guestId, Date pdate) {
        List<Pricelist> pricelists = this.findById_GuestIdAndId_pdate(guestId, pdate);
        Map<String, Pricelist> map = new HashMap<>();

        for (Pricelist pricelist : pricelists){
            map.put(pricelist.getId().getProductId(), pricelist);
        }

        return map;
    }

    @Override
    public Export getExcelExport(String guestId, Date pdate) {
        List<Category> categories = categoryService.findAll();
        List<Pricelist> pricelists = pricelistRepository.findById_GuestIdAndId_Pdate(guestId, pdate);
        Map<String, Product> productMap = productService.findMap();

        Export export = new Export();
        export.setExpire(10);
        export.setPdate(pdate);

        List<CategoryExport> categoryExports = new ArrayList<>();
        for (Category category : categories){
            CategoryExport categoryExport = new CategoryExport();
            categoryExport.setCategoryName(category.getName());
            List<ProductExport> productExports = new ArrayList<>();
            for (Pricelist pricelist : pricelists){
                Product product = productMap.get(pricelist.getId().getProductId());
                if (product.getPCode().equals(category.getCode())){
                    ProductExport productExport = new ProductExport();
                    productExport.setName(product.getName());
                    productExport.setPrice(pricelist.getPrice());
                    productExports.add(productExport);
                }
            }
            categoryExport.setProductExports(productExports);
            categoryExports.add(categoryExport);
        }

        export.setCategoryExports(categoryExports);

        return export;
    }

    @Override
    public boolean isGuestExist(String guestId) {
        return pricelistRepository.find(guestId) != null;
    }
    @Override
    public boolean isProductExist(String productId) {
        return pricelistRepository.findWithProductId(productId) != null;
    }

    @Override
    public boolean isRepeat(String guestId, Date date) {
        return pricelistRepository.findWithGuestIdAndDate(guestId, date) != null;
    }

    @Override
    public List<AllDTO> findAllWith(String guestId, Date date) {
        List<Object[]> objectss = pricelistRepository.findAllWith(guestId, date);

        List<AllDTO> allDTOS = PricelistTransform.objectssToAllDTOS(objectss);

        return allDTOS;
    }

    @Override
    public PricelistsVO findLatest(String guestId) {
        List<Date> dates = this.findDates(guestId);
        if (CollectionUtils.isEmpty(dates) == false){
            List<Object[]> objectss = pricelistRepository.findAllWith(guestId, dates.get(0));
            List<AllDTO> allDTOS = PricelistTransform.objectssToAllDTOS(objectss);

            PricelistsVO pricelistsVO = new PricelistsVO();

            List<ProductVO> products = allDTOS.stream().map(e ->
                    new ProductVO(
                            e.getProductId(), e.getProductName(), e.getProductCategory(), e.getProductUnit(),
                            e.getProductMarketPrice(), e.getProductGuestPrice(), e.getProductImgfile(), e.getNote(),
                            null, null
                    )
            ).collect(Collectors.toList());


            pricelistsVO.setGuestId(guestId);
            pricelistsVO.setDate(dates.get(0));
            pricelistsVO.setProducts(products);

            return pricelistsVO;
        }else{
            List<com.youcai.manage.dto.product.AllDTO> productAllDTOS = productService.findAllWith();
            PricelistsVO pricelistsVO = new PricelistsVO();

            List<ProductVO> products = productAllDTOS.stream().map(e ->
                    new ProductVO(
                            e.getId(), e.getName(), e.getCategory(), e.getUnit(),
                            e.getPrice(), e.getPrice(), e.getImgfile(), "",
                            null, null
                    )
            ).collect(Collectors.toList());

            pricelistsVO.setGuestId(guestId);
            pricelistsVO.setDate(new Date());
            pricelistsVO.setProducts(products);

            return pricelistsVO;
        }
    }

    @Override
    public List<Date> findDates(String guestId) {
        List<Date> dates = pricelistRepository.findDistinctId_PdateById_GuestId(guestId);
        return dates;
    }
}
