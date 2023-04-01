package cc.rcbb.validator.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <p>TestDTO2</p>
 *  基础注解使用，自定义message信息
 * @author rcbb.cc
 * @version 1.0.0
 * @date 2020/11/18
 */
@Data
public class TestDTO2 {

    @NotNull(message = "id不能为空")
    private Long id;

    @NotBlank(message = "name不能为空")
    private String name;

    @NotNull(message = "age不能为空")
    private Integer age;

    @NotNull(message = "status不能为空")
    private Integer status;

}
