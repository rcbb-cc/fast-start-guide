package cc.rcbb.validator.config;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashSet;
import java.util.Set;

/**
 * <p>ListInValuesValiadator</p>
 * 自定义的校验器
 * @author rcbb.cc
 * @version 1.0.0
 * @date 2020/11/19
 */
public class ListInValuesValiadator implements ConstraintValidator<ListInValues, Integer> {
    private Set<Integer> values = new HashSet<>();
    @Override
    public void initialize(ListInValues constraintAnnotation) {
        // 初始化
        int[] vals = constraintAnnotation.values();
        for (int val : vals) {
            values.add(val);
        }
    }
    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        // 校验
        return values.contains(value);
    }
}
