package com.youcai.manage.form.guest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePwdForm {

    @NotEmpty(message = "此客户不存在")
    private String id;

    @NotEmpty(message = "新密码不能为空")
    @Length(min = 6, message = "新密码长度不能低于6位")
    private String pwd;

}
