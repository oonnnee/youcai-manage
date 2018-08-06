package com.youcai.manage.transform;

import com.youcai.manage.dto.product.AllDTO;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

public class ProductTransform {

    public static List<AllDTO> objectssToAllDTOS(List<Object[]> objectss){
        if (CollectionUtils.isEmpty(objectss)){
            return null;
        }

        return objectss.stream().map(
                e -> new AllDTO(e[0], e[1], e[2], e[3], e[4], e[5])
        ).collect(Collectors.toList());
    }
}
