package cc.rcbb.validator.dto;

import cc.rcbb.validator.config.sequence.ValidateSequence;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <p>TestDTO2</p>
 *  基础注解使用，自定义message信息，进行分组，顺序校验
 * @author rcbb.cc
 * @version 1.0.0
 * @date 2020/11/18
 */
@Data
public class TestDTO4 {
    @NotNull(message = "id不能为空", groups = {ValidateSequence.Validate1.class})
    private Long id;
    @NotBlank(message = "name不能为空", groups = {  ValidateSequence.Validate2.class})
    private String name;
    @NotNull(message = "age不能为空", groups = {  ValidateSequence.Validate2.class})
    private Integer age;
    @NotNull(message = "status不能为空")
    private Integer status;
}
