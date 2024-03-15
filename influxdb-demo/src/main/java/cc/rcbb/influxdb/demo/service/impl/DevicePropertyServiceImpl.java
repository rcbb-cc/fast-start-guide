package cc.rcbb.influxdb.demo.service.impl;

import cc.rcbb.influxdb.demo.pojo.DeviceProperty;
import cc.rcbb.influxdb.demo.service.DevicePropertyService;
import cc.rcbb.influxdb.demo.service.IInfluxdbService;
import cn.hutool.json.JSONUtil;
import lombok.AllArgsConstructor;
import org.influxdb.dto.Point;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * DevicePropertyServiceImpl
 * </p>
 *
 * @author rcbb.cc
 * @date 2024/3/15
 */
@Service
@AllArgsConstructor
public class DevicePropertyServiceImpl implements DevicePropertyService {

    private final IInfluxdbService influxdbService;

    @Override
    public void save(DeviceProperty deviceProperty) {
        Map<String, String> tagMap = new HashMap<>();
        tagMap.put("productId", deviceProperty.getProductId().toString());
        tagMap.put("deviceId", deviceProperty.getProductId().toString());
        // 表名 device_property
        Point point = Point.measurement("device_property")
                .tag(tagMap)
                .addField("jsonValue", JSONUtil.toJsonStr(deviceProperty.getParamMap()))
                .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                .build();
        influxdbService.save(point);
    }

    @Override
    public DeviceProperty getLast(Long productId, Long deviceId) {
        String sql = "SELECT last(*) FROM \"device_property\" where productId='{productId}' and deviceId='{deviceId}'";
        Map<String, Object> paramMap = new HashMap<>(2);
        paramMap.put("productId", productId);
        paramMap.put("deviceId", deviceId);
        Map<String, Object> last = influxdbService.getLastBySql(sql, paramMap);
        return new DeviceProperty().setProductId(productId).setDeviceId(deviceId).setParamMap(last);
    }
}
