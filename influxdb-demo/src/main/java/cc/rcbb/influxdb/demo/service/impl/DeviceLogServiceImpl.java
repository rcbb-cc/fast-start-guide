package cc.rcbb.influxdb.demo.service.impl;

import cc.rcbb.influxdb.demo.pojo.DeviceLog;
import cc.rcbb.influxdb.demo.service.DeviceLogService;
import cc.rcbb.influxdb.demo.service.IInfluxdbService;
import com.baomidou.mybatisplus.plugins.Page;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.influxdb.dto.Point;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * DeviceLogServiceImpl
 * </p>
 *
 * @author rcbb.cc
 * @date 2024/3/15
 */
@Slf4j
@Service
@AllArgsConstructor
public class DeviceLogServiceImpl implements DeviceLogService {

    private final IInfluxdbService influxdbService;

    @Override
    public void save(DeviceLog deviceLog) {
        Map<String, String> tagMap = new HashMap<>();
        tagMap.put("productId", deviceLog.getProductId().toString());
        tagMap.put("deviceId", deviceLog.getProductId().toString());
        // 表名 device_log
        Point point = Point.measurement("device_log")
                .tag(tagMap)
                .addField("topic", deviceLog.getTopic())
                .addField("message", deviceLog.getMessage())
                .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                .build();
        influxdbService.save(point);
    }

    @Override
    public List<DeviceLog> list(Long productId, Long deviceId) {
        String sql = "SELECT * FROM \"device_log\" WHERE productId='{productId}' and deviceId='{deviceId}'";
        Map<String, Object> paramMap = new HashMap<>(2);
        paramMap.put("productId", productId);
        paramMap.put("deviceId", deviceId);
        List<Map<String, Object>> result = influxdbService.getBySql(sql, paramMap);
        List<DeviceLog> list = new ArrayList<>();
        for (Map<String, Object> map : result) {
            list.add(new DeviceLog()
                    .setProductId(productId)
                    .setDeviceId(deviceId)
                    .setTopic(map.getOrDefault("topic", "").toString())
                    .setMessage(map.getOrDefault("message", "").toString()));
        }
        return list;
    }

    @Override
    public Page<Map<String, Object>> page(Page<Map<String, Object>> page, Long productId, Long deviceId, LocalDateTime startTime, LocalDateTime endTime) {
        String sql = "SELECT * FROM \"device_log\" WHERE productId = '{productId}' and deviceId = '{deviceId}'";
        // sql 参数
        Map<String, Object> paramMap = new HashMap<>(2);
        paramMap.put("productId", productId);
        paramMap.put("deviceId", deviceId);
        // 时间筛选
        if (startTime != null && endTime != null) {
            sql += " and time >= '{time1}' and time <= '{time2}'";
            paramMap.put("time1", startTime.toInstant(ZoneOffset.UTC).toString());
            paramMap.put("time2", endTime.toInstant(ZoneOffset.UTC).toString());
        }
        // 排序
        sql += " order by time desc";
        return influxdbService.getPageBySql(page, sql, paramMap);
    }
}
