package cc.rcbb.mapstruct.demo.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * User
 * </p>
 *
 * @author rcbb.cc
 * @date 2022/8/17
 */
@Accessors(chain = true)
@Data
public class User {

    private Long id;

    private String name;

    private LocalDate birthday;

    private LocalDateTime createTime;

    private Long updateTime;

}
