package com.youcai.manage.vo.deliver;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListVO {

    private String guestId;
    private String guestName;
    private Date deliverDate;
    private Date orderDate;

}
