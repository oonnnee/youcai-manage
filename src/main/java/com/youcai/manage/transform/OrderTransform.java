package com.youcai.manage.transform;

import com.youcai.manage.dto.order.AllDTO;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

public class OrderTransform {

    public static List<AllDTO> objectssToAllDTOS(List<Object[]> objectss){
        if (CollectionUtils.isEmpty(objectss)){
            return null;
        }

        return objectss.stream().map(
                e -> new AllDTO(e[0], e[1], e[2], e[3], e[4], e[5], e[6], e[7], e[8], e[9], e[10], e[11], e[12])
        ).collect(Collectors.toList());
    }
}
