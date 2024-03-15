package cc.rcbb.influxdb.demo.service;

import cc.rcbb.influxdb.demo.pojo.DeviceProperty;
import cn.hutool.core.lang.Dict;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * <p>
 * DevicePropertyServiceTest
 * </p>
 *
 * @author rcbb.cc
 * @date 2024/3/15
 */
@Slf4j
@SpringBootTest
public class DevicePropertyServiceTest {

    @Autowired
    private DevicePropertyService devicePropertyService;

    @Test
    public void save() {
        DeviceProperty deviceProperty = new DeviceProperty()
                .setProductId(1L)
                .setDeviceId(1L)
                .setParamMap(new Dict()
                        .set("param1", 1)
                        .set("param2", 2L)
                        .set("param3", 3.0D)
                        .set("param4", "4")
                        .set("param5", new Dict().set("version1", "123").set("version2", "234"))
                        .set("param6", List.of("1", "2", "3")));

        devicePropertyService.save(deviceProperty);
    }

    @Test
    public void getLast() {
        Long productId = 1L;
        Long deviceId = 1L;
        DeviceProperty last = devicePropertyService.getLast(productId, deviceId);
        log.info("[getLast()测试成功 last<{}>]", last);
    }

}
