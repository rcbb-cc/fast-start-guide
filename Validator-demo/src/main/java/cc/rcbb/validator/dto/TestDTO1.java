package cc.rcbb.validator.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <p>TestDTO1</p>
 *  基础注解使用
 * @author rcbb.cc
 * @version 1.0.0
 * @date 2020/11/18
 */
@Data
public class TestDTO1 {

    @NotNull
    private Long id;

    @NotBlank
    private String name;

    @NotNull
    private Integer age;

    @NotNull
    private Integer status;

}
