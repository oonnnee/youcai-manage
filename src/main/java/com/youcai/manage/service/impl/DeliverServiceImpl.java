package com.youcai.manage.service.impl;

import com.youcai.manage.dataobject.Deliver;
import com.youcai.manage.dataobject.Guest;
import com.youcai.manage.dto.deliver.ListDTO;
import com.youcai.manage.dto.deliver.ListKey;
import com.youcai.manage.repository.DeliverRepository;
import com.youcai.manage.service.DeliverService;
import com.youcai.manage.service.DriverService;
import com.youcai.manage.service.GuestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class DeliverServiceImpl implements DeliverService {

    @Autowired
    private DeliverRepository deliverRepository;
    @Autowired
    private GuestService guestService;
    @Autowired
    private DriverService driverService;

    @Override
    public List<Deliver> findByIdGuestId(String guestId) {
        return deliverRepository.findByIdGuestId(guestId);
    }

    @Override
    public List<ListDTO> findListDTOS() {
        List<Deliver> delivers = deliverRepository.findAll();
        Map<Integer, String> driverMap = driverService.findMap();
        Map<String, String> guestMap = guestService.findMap();
        List<ListDTO> listDTOS = new ArrayList<>();
        for (Deliver deliver : delivers){
            Integer driverId = deliver.getId().getDriverId();
            String guestId = deliver.getId().getGuestId();
            ListDTO listDTO = new ListDTO();
            listDTO.setListKey(new ListKey(driverId, guestId, driverMap.get(driverId), guestMap.get(guestId)));
            listDTO.setDate(deliver.getId().getDdate());
            listDTOS.add(listDTO);
        }
        return listDTOS;
    }

    @Override
    public List<ListDTO> findListDTOSByDriverName(String driverName) {
        List<Deliver> delivers = deliverRepository.findAll();
        Map<Integer, String> driverMap = driverService.findMapByNameLike(driverName);
        Map<String, String> guestMap = guestService.findMap();
        List<ListDTO> listDTOS = new ArrayList<>();
        for (Deliver deliver : delivers){
            if (driverMap.get(deliver.getId().getDriverId()) == null){
                continue;
            }
            Integer driverId = deliver.getId().getDriverId();
            String guestId = deliver.getId().getGuestId();
            ListDTO listDTO = new ListDTO();
            listDTO.setListKey(new ListKey(driverId, guestId, driverMap.get(driverId), guestMap.get(guestId)));
            listDTO.setDate(deliver.getId().getDdate());
            listDTOS.add(listDTO);
        }
        return listDTOS;
    }

    @Override
    public List<ListDTO> findListDTOSByGuestName(String guestName) {
        List<Deliver> delivers = deliverRepository.findAll();
        Map<Integer, String> driverMap = driverService.findMap();
        Map<String, String> guestMap = guestService.findMapByNameLike(guestName);
        List<ListDTO> listDTOS = new ArrayList<>();
        for (Deliver deliver : delivers){
            if (guestMap.get(deliver.getId().getGuestId()) == null){
                continue;
            }
            Integer driverId = deliver.getId().getDriverId();
            String guestId = deliver.getId().getGuestId();
            ListDTO listDTO = new ListDTO();
            listDTO.setListKey(new ListKey(driverId, guestId, driverMap.get(driverId), guestMap.get(guestId)));
            listDTO.setDate(deliver.getId().getDdate());
            listDTOS.add(listDTO);
        }
        return listDTOS;
    }
}
