package cc.rcbb.influxdb.demo.service;

import cc.rcbb.influxdb.demo.pojo.DeviceLog;
import cn.hutool.core.lang.Dict;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.plugins.Page;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * DeviceLogServiceTest
 * </p>
 *
 * @author rcbb.cc
 * @date 2024/3/15
 */
@Slf4j
@SpringBootTest
public class DeviceLogServiceTest {

    @Autowired
    private DeviceLogService deviceLogService;

    @Test
    public void save() {
        DeviceLog deviceLog = new DeviceLog()
                .setProductId(1L)
                .setDeviceId(1L)
                .setTopic("/sys/test/log")
                .setMessage(JSONUtil.toJsonStr(new Dict()
                        .set("request", new Dict().set("param1", "123")
                                .set("param2", new Dict().set("key1", "value1").set("key2", "value2")))
                        .set("param3", List.of("1", "2", "3"))
                ));
        deviceLogService.save(deviceLog);
        DeviceLog deviceLogReply = new DeviceLog()
                .setProductId(1L)
                .setDeviceId(1L)
                .setTopic("/sys/test/log_reply")
                .setMessage(JSONUtil.toJsonStr(new Dict()
                        .set("response", new Dict().set("code", "200")
                                .set("data", new Dict().set("key1", "value1").set("key2", "value2")))
                        .set("msg", "success")
                ));
        deviceLogService.save(deviceLogReply);
    }

    @Test
    public void list() {
        Long productId = 1L;
        Long deviceId = 1L;
        List<DeviceLog> list = deviceLogService.list(productId, deviceId);
        log.info("[list()测试成功 list<{}>]", list);
    }

    @Test
    public void page() {
        Page<Map<String, Object>> pageParam = new Page<>();
        Long productId = 1L;
        Long deviceId = 1L;
        LocalDateTime startTime = LocalDateTime.of(2024, 3, 15, 0, 0, 0);
        LocalDateTime endTime = LocalDateTime.of(2024, 3, 15, 16, 0, 0);
        Page<Map<String, Object>> page = deviceLogService.page(pageParam, productId, deviceId, startTime, endTime);
        log.info("[page()测试成功 page<{}> records<{}>]", page, page.getRecords());

    }

}
