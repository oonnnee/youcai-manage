package com.youcai.manage.service.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.youcai.manage.dataobject.Guest;
import com.youcai.manage.dataobject.Order;
import com.youcai.manage.dataobject.OrderKey;
import com.youcai.manage.dataobject.Product;
import com.youcai.manage.dto.excel.order.Export;
import com.youcai.manage.dto.excel.order.ProductExport;
import com.youcai.manage.dto.order.AllDTO;
import com.youcai.manage.dto.order.NewDTO;
import com.youcai.manage.enums.OrderEnum;
import com.youcai.manage.exception.ManageException;
import com.youcai.manage.repository.OrderRepository;
import com.youcai.manage.service.GuestService;
import com.youcai.manage.service.OrderService;
import com.youcai.manage.service.ProductService;
import com.youcai.manage.transform.OrderTransform;
import com.youcai.manage.utils.ManageUtils;
import com.youcai.manage.utils.OrderUtils;
import com.youcai.manage.vo.order.PendingVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private GuestService guestService;
    @Autowired
    private ProductService productService;

    @Override
    public Page<Order> findAll(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }

    @Override
    public List<Order> findByIdGuestId(String guestId) {
        return orderRepository.findByIdGuestId(guestId);
    }

    @Override
    public List<Date> findDatesByGuestId(String guestId) {
        List<Date> dates = orderRepository.findDistinctIdOdateByIdGuestId(guestId);
        return dates;
    }

    @Override
    public List<String> findStatesByGuestIdAndDate(String guestId, Date date) {
        List<String> states = orderRepository.findDistinctIdStateByIdGuestIdAndIdOdate(guestId, date);
        return states;
    }

    @Override
    public List<Order> findByIdGuestIdAndIdDateAndIdState(String guestId, Date date, String state) {
        return orderRepository.findByIdGuestIdAndIdOdateAndIdState(guestId, date, state);
    }

    @Override
    public Page<Guest> findGuestPage(Pageable pageable) {
        List<String> guestIds = orderRepository.findDistinctIdGuestId();
        return guestService.findByIdIn(guestIds, pageable);
    }

    @Override
    public Page<Guest> findGuestPageByGuestIdLike(Pageable pageable, String guestId) {
        List<String> guestIds = orderRepository.findDistinctIdGuestId("%"+guestId+"%");
        return guestService.findByIdIn(guestIds, pageable);
    }

    @Override
    public Page<Guest> findGuestPageByGuestNameLike(Pageable pageable, String guestName) {
        List<String> guestIds = orderRepository.findDistinctIdGuestId();
        return guestService.findByIdInAndNameLike(guestIds, guestName, pageable);
    }

    @Override
    public Export getExcelExport(String guestId, Date date, String state) {
        Export export = new Export();
        /*------------ 客户名 -------------*/
        export.setGuestName(guestService.findOne(guestId).getName());
        /*------------ 日期 -------------*/
        export.setDate(date);
        /*------------ 产品&采购单金额 -------------*/
        List<ProductExport> productExports = new ArrayList<>();
        BigDecimal amount = BigDecimal.ZERO;
        List<Order> orders = this.findByIdGuestIdAndIdDateAndIdState(guestId, date, state);
        Map<String, Product> productMap = productService.findMap();
        int index = 1;
        for (Order order : orders){
            if (!ManageUtils.isZero(order.getNum())){
                /*--- 产品 ---*/
                Product product = productMap.get(order.getId().getProductId());
                ProductExport productExport = new ProductExport();
                productExport.setIndex(index++);
                productExport.setName(product.getName());
                productExport.setNum(order.getNum());
                productExport.setUnit(product.getUnit());
                productExport.setPrice(order.getPrice());
                productExport.setAmount(order.getAmount());
                productExport.setNote(order.getNote());
                productExports.add(productExport);
                /*--- 采购单金额 ---*/
                amount = amount.add(order.getAmount());
            }
        }
        /*--- 产品 ---*/
        export.setProductExports(productExports);
        /*--- 采购单金额 ---*/
        export.setAmount(amount);
        return export;
    }

    @Override
    public Long countByState(String state) {
        Long count = 0L;
        if (state.equals(OrderEnum.PENDING.getState())){
            String[] stateHeads = {OrderEnum.NEW.getState()+"%", OrderEnum.BACKING.getState()+"%"};
            for (int i=0; i<stateHeads.length; i++){
                count += orderRepository.countDistinctByIdStateLike(stateHeads[i]);
            }
        } else {
            String stateHead = state + "%";
            count += orderRepository.countDistinctByIdStateLike(stateHead);
        }
        return count;
    }

    @Override
    public List<PendingVO> findPendingList(String state) {
        List<PendingVO> pendingVOS = new ArrayList<>();
        List<Object[]> objects;
        Map<String, String> guestMap = guestService.findMap();
        if (state.equals(OrderEnum.PENDING.getState())){
            String[] stateHeads = {OrderEnum.NEW.getState()+"%", OrderEnum.BACKING.getState()+"%"};
            for (int i=0; i<stateHeads.length; i++){
                objects = orderRepository.findDistinctOdateAndGuestIdAndStateByStateLike(stateHeads[i]);
                for (Object[] objs : objects){
                    String guestId = (String) objs[0];
                    Date date = (Date) objs[1];
                    String state2 = (String) objs[2];
                    String guestName = guestMap.get(guestId);
                    PendingVO pendingVO = new PendingVO(guestId, date, state2, guestName);
                    pendingVOS.add(pendingVO);
                }
            }
        } else {
            String stateHead = state + "%";
            objects = orderRepository.findDistinctOdateAndGuestIdAndStateByStateLike(stateHead);
            for (Object[] objs : objects){
                String guestId = (String) objs[0];
                Date date = (Date) objs[1];
                String state2 = (String) objs[2];
                String guestName = guestMap.get(guestId);
                PendingVO pendingVO = new PendingVO(guestId, date, state2, guestName);
                pendingVOS.add(pendingVO);
            }
        }
        return pendingVOS;
    }

    @Override
    @Transactional
    public void updateState(String guestId, Date date, String oldState, String newState) {
        orderRepository.updateState(guestId, date, oldState, newState);
    }

    @Override
    public boolean isGuestExist(String guestId) {
        return orderRepository.find(guestId) != null;
    }
    @Override
    public boolean isProductExist(String productId) {
        return orderRepository.findWithProductId(productId) != null;
    }

    @Override
    public List<Object[]> sumByStateAndDate(String state, Date startDate, Date endDate) {
        ManageUtils.ManageException(endDate.getTime() < startDate.getTime(), "结束日期不能小于起始日期");
        ManageUtils.ManageException(startDate == null, "起始日期不能为空");
        ManageUtils.ManageException(endDate == null, "结束日期不能为空");

        List<Object[]> objects = orderRepository.sumByStateAndDate(state, startDate, endDate);

        return objects;
    }
    @Override
    public List<Object[]> sumByStateAndDateNoOrder(String state, Date startDate, Date endDate) {
        ManageUtils.ManageException(endDate.getTime() < startDate.getTime(), "结束日期不能小于起始日期");
        ManageUtils.ManageException(startDate == null, "起始日期不能为空");
        ManageUtils.ManageException(endDate == null, "结束日期不能为空");

        List<Object[]> objects = orderRepository.sumByStateAndDateNoOrder(state, startDate, endDate);

        return objects;
    }
    @Override
    public List<Object[]> sumByStateAndMonth(String state, Integer month) {
        String months = month < 10 ? "0"+month : ""+month;
        String date = LocalDate.now().getYear() + "-" +months+ "-%";

        List<Object[]> objects = orderRepository.sumByStateAndMonth(state, date);

        return objects;
    }
    @Override
    public List<Object[]> sumByStateAndDay(String state, int startDay, int endDay) {
        ManageUtils.ManageException(endDay < startDay, "结束日期不能小于起始日期");

        Date startDate = null;
        Date endDate = null;

        LocalDate now = LocalDate.now();
        int year = now.getYear();
        int month = now.getMonthValue();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            startDate = dateFormat.parse(year+"-"+month+"-"+startDay);
            endDate = dateFormat.parse(year+"-"+month+"-"+endDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        List<Object[]> objects = orderRepository.sumByStateAndDateNoOrder(state, startDate, endDate);

        return objects;
    }
    @Override
    public List<Object[]> sumByStateAndDateEqual(String state, Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date = dateFormat.parse(dateFormat.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        List<Object[]> objects = orderRepository.sumByStateAndDateEqual(state, date);

        return objects;
    }

    @Override
    public List<AllDTO> findAllWith(String guestId, Date date, String state) {
        List<Object[]> objectss = orderRepository.findAllWith(guestId, date, state);

        List<AllDTO> allDTOS = OrderTransform.objectssToAllDTOS(objectss);

        return allDTOS;
    }

    @Override
    public void update(String guestId, Date date, String products) {
        List<NewDTO> newDTOS;
        try {
            newDTOS = new Gson().fromJson(products,
                    new TypeToken<List<NewDTO>>() {
                    }.getType());
        } catch (Exception e) {
            throw new ManageException("更新采购单失败，产品参数错误");
        }

        // 校验数量是否为负
        for (NewDTO newDTO : newDTOS){
            ManageUtils.ManageException(ManageUtils.isNegative(newDTO.getNum()), "更新采购单失败, 产品数量不能小于0");
        }

        List<Order> orders = newDTOS.stream().map(e ->
                new Order(new OrderKey(date, guestId, e.getProductId(), OrderUtils.getStateNew()), e.getPrice(), e.getNum(),
                        e.getPrice().multiply(e.getNum()), e.getNote())
        ).collect(Collectors.toList());

        orderRepository.save(orders);
    }
}
