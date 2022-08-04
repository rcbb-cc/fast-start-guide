package cc.rcbb.sharding.test.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * Position
 * </p>
 *
 * @author rcbb.cc
 * @date 2021/9/16
 */
@Accessors(chain = true)
@Data
@TableName("t_position")
public class Position {

    @TableId(type = IdType.INPUT)
    private Long positionId;

    private String address;

    private Date locDate;
}
