package com.youcai.manage.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum OrderEnum {

    UN_KNOW("-1", "未知状态"),
    ALL("0", "全部"), // NEW & BACKING & BACKED & DELIVERED
    NEW("1", "新采购单"),
    BACKING("2", "待退回"),
    BACKED("3", "已退回"),
    DELIVERED("4", "已发货"),
    PENDING("5", "待处理") // NEW & BACKING
    ;

    private String state;
    private String note;
}
