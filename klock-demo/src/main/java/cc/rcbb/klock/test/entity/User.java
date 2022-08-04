package cc.rcbb.klock.test.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * User
 * </p>
 *
 * @author rcbb.cc
 * @date 2021/6/10
 */
@Accessors(chain = true)
@Data
public class User {

    private Integer id;

    private String name;

    private Integer age;

}
