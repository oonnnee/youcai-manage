package com.youcai.manage.dataobject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Date;

/**
 * 联合主键：报价日期、报价客户id、报价产品id
 */
@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PricelistKey implements Serializable {

    /*--- 报价日期 ---*/
    private Date pdate;

    /*--- 报价客户id ---*/
    private String guestId;

    /*--- 报价产品id ---*/
    private String productId;

}
