package com.youcai.manage.form.driver;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaveForm {

    @NotEmpty(message = "姓名不能为空")
    private String name;

    @NotEmpty(message = "身份证不能为空")
    private String cardid;

    @NotEmpty(message = "手机号不能为空")
    @Pattern(regexp = "^(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\\d{8}$", message = "手机号不存在")
    private String mobile;

    private String note;
}
