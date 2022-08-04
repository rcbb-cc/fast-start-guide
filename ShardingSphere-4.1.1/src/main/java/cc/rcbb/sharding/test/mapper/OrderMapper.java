package cc.rcbb.sharding.test.mapper;

import cc.rcbb.sharding.test.entity.Order;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * OrderMapper
 * </p>
 *
 * @author rcbb.cc
 * @date 2021/9/16
 */
@Mapper
public interface OrderMapper extends BaseMapper<Order> {
}
