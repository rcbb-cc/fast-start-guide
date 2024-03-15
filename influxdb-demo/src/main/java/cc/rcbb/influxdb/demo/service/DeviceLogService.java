package cc.rcbb.influxdb.demo.service;

import cc.rcbb.influxdb.demo.pojo.DeviceLog;
import com.baomidou.mybatisplus.plugins.Page;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * DeviceLogService
 * </p>
 *
 * @author rcbb.cc
 * @date 2024/3/15
 */
public interface DeviceLogService {

    void save(DeviceLog deviceLog);

    List<DeviceLog> list(Long productId, Long deviceId);

    Page<Map<String, Object>> page(Page<Map<String, Object>> page, Long productId, Long deviceId, LocalDateTime startTime, LocalDateTime endTime);

}
