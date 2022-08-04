package cc.rcbb.sharding.test.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Accessors(chain = true)
@Data
@TableName("t_order")
public class Order {

    /**
     * 订单流水ID
     */
    @TableId(type = IdType.INPUT)
    private Long orderId;

    /**
     * 用户流水ID
     */
    private Long userId;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 订单创建时间
     */
    private Date createTime;

}
