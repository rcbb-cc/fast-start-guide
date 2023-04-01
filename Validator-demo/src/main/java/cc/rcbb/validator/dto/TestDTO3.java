package cc.rcbb.validator.dto;

import cc.rcbb.validator.config.AddGroup;
import cc.rcbb.validator.config.UpdateGroup;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

/**
 * <p>TestDTO2</p>
 *  基础注解使用，自定义message信息，进行分组
 * @author rcbb.cc
 * @version 1.0.0
 * @date 2020/11/18
 */
@Data
public class TestDTO3 {

    @NotNull(message = "id不能为空", groups = {UpdateGroup.class})
    @Null(message = "id必须为空", groups = {AddGroup.class})
    private Long id;

    @NotBlank(message = "name不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private String name;

    @NotNull(message = "age不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private Integer age;

    @NotNull(message = "status不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private Integer status;

}
