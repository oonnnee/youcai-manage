package com.youcai.manage.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum DeliverEnum {

    UN_KNOW("-1", "未知状态"),
    ALL("0", "全部"), // DELIVERING & FINISH
    DELIVERING("1", "送货中"),
    RECEIVE("2", "已收货"),
    ;

    private String state;
    private String note;
}
