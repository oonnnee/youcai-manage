package com.youcai.manage.service;

import com.youcai.manage.dataobject.Pricelist;
import com.youcai.manage.dto.excel.pricelist.Export;
import com.youcai.manage.dto.pricelist.AllDTO;
import com.youcai.manage.vo.pricelist.PricelistsVO;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface PricelistService {

    List<Pricelist> findById_GuestId(String guestId);

    List<Pricelist> findById_GuestIdAndId_pdate(String guestId, Date pdate);

    List<AllDTO> findAllWith(String guestId, Date date);

    // productId: pricelist
    Map<String, Pricelist> findProductIdMap(String guestId, Date pdate);

    void save(List<Pricelist> pricelists);
    void update(List<Pricelist> pricelists);

    void delete(String guestId, Date pdate);

    List<Date> findPdatesByGuestId(String guestId);

    Export getExcelExport(String guestId, Date pdate);

    boolean isGuestExist(String guestId);
    boolean isProductExist(String productId);

    boolean isRepeat(String guestId, Date date);

    PricelistsVO findLatest(String guestId);

    List<Date> findDates(String guestId);
}
