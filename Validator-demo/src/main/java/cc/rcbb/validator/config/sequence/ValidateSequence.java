package cc.rcbb.validator.config.sequence;

import javax.validation.GroupSequence;

/**
 * <p>ValidateSequence</p>
 *  校验顺序，1 -> 2 ->3 ...
 * @author rcbb.cc
 * @version 1.0.0
 * @date 2020/11/19
 */
@GroupSequence(value = {ValidateSequence.Validate1.class,
        ValidateSequence.Validate2.class,
        ValidateSequence.Validate3.class,
        ValidateSequence.Validate4.class,
        ValidateSequence.Default.class,})
public interface ValidateSequence {
    interface Validate1{}
    interface Validate2{}
    interface Validate3{}
    interface Validate4{}
    interface Default{}
}
