package cc.rcbb.validator.dto;

import cc.rcbb.validator.config.ListInValues;
import cc.rcbb.validator.config.sequence.ValidateSequence;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * <p>TestDTO2</p>
 *  基础注解使用，自定义message信息，自定义规则
 * @author rcbb.cc
 * @version 1.0.0
 * @date 2020/11/18
 */
@Data
public class TestDTO5 {
    @NotNull(message = "id不能为空")
    private Long id;
    @NotBlank(message = "name不能为空")
    @Pattern(regexp = "^[A-Za-z][A-Za-z0-9]{5,17}$", message = "用户名由字母开头的6-18位数字和字母组成")
    private String name;
    @NotBlank(message = "phone不能为空")
    @Pattern(regexp = "^1[3|4|5|6|8|7|9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    @NotNull
    @ListInValues(values = {0,1}, message = "错误值")
    private Integer status;
}
