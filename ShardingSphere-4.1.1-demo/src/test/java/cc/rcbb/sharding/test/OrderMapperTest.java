package cc.rcbb.sharding.test;

import cc.rcbb.sharding.test.entity.Order;
import cc.rcbb.sharding.test.mapper.OrderMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

/**
 * <p>
 * OrderMapperTest
 * </p>
 *
 * @author rcbb.cc
 * @date 2021/9/16
 */
@SpringBootTest
public class OrderMapperTest {

    @Autowired
    private OrderMapper orderMapper;

    @Test
    public void insert() {
        orderMapper.insert(new Order().setOrderId(1L).setUserId(1L).setOrderNo("1").setCreateTime(new Date()));
        orderMapper.insert(new Order().setOrderId(2L).setUserId(2L).setOrderNo("2").setCreateTime(new Date()));
        orderMapper.insert(new Order().setOrderId(3L).setUserId(3L).setOrderNo("3").setCreateTime(new Date()));
        orderMapper.insert(new Order().setOrderId(4L).setUserId(4L).setOrderNo("4").setCreateTime(new Date()));
    }

}
