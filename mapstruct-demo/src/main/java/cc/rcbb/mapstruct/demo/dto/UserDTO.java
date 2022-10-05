package cc.rcbb.mapstruct.demo.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * UserDTO
 * </p>
 *
 * @author rcbb.cc
 * @date 2022/8/17
 */
@Accessors(chain = true)
@Data
public class UserDTO {
    /**
     * 由 long 转为 String
     * 加密过的 id
     */
    private String id;

    private String name;
    /**
     * 格式：yyyy-MM-dd
     */
    private String birthday;

    private String createTime;

    private Long updateTime;

}
