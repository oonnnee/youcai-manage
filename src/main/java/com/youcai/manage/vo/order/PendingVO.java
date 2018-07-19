package com.youcai.manage.vo.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PendingVO {

    private String guestId;
    private Date date;
    private String state;

    private String guestName;
}
