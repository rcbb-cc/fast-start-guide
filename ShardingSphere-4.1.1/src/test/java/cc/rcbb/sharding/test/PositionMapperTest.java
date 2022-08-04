package cc.rcbb.sharding.test;

import cc.rcbb.sharding.test.entity.Position;
import cc.rcbb.sharding.test.mapper.PositionMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Date;

/**
 * <p>
 * PositionMapperTest
 * </p>
 *
 * @author rcbb.cc
 * @date 2021/9/16
 */
@Slf4j
@SpringBootTest
public class PositionMapperTest {

    @Autowired
    private PositionMapper positionMapper;

    @Test
    public void selectById() {
        Position position = positionMapper.selectOne(Wrappers.lambdaQuery(Position.class).eq(Position::getPositionId, 1L).eq(Position::getLocDate, new Date()));
        log.info("[测试结果<{}>]", position);
    }

    @Test
    public void insert() {
        positionMapper.insert(new Position().setPositionId(1L).setAddress("1").setLocDate(new Date()));
        positionMapper.insert(new Position().setPositionId(2L).setAddress("2").setLocDate(new Date()));
        positionMapper.insert(new Position().setPositionId(3L).setAddress("3").setLocDate(new Date()));
    }

}
