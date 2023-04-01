package cc.rcbb.validator.config;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * <p>ListInValues</p>
 * 自定义的校验注解
 * @author rcbb.cc
 * @version 1.0.0
 * @date 2020/11/19
 */
@Documented
// 可以指定多个不同的校验器
@Constraint(validatedBy = {ListInValuesValiadator.class})
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
public @interface ListInValues {
    // 默认提示值可自定义，词条文件：ValidationMessages.properties
    String message() default "{javax.validation.constraints.NotBlank.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    int[] values() default {};
}
