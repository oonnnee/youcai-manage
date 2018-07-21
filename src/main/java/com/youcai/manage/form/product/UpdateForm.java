package com.youcai.manage.form.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateForm {

    @NotEmpty(message = "产品不存在")
    private String id;

    @NotEmpty(message = "产品名称不能为空")
    private String name;

    @NotEmpty(message = "产品大类不能为空")
    private String pCode;

    @NotEmpty(message = "产品单位不能为空")
    private String unit;

    @NotNull(message = "产品价格不能为空")
    private BigDecimal price;

    @Length(max = 256, message = "最多上传6张图片哦")
    private String imgfile;

    private String note;
}
