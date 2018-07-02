package com.youcai.manage.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum OrderEnum {

    OK("0", "正常");

    private String state;
    private String note;
}
