package cc.rcbb.influxdb.demo.service;

import cc.rcbb.influxdb.demo.pojo.DeviceProperty;

/**
 * <p>
 * DevicePropertyService
 * </p>
 *
 * @author rcbb.cc
 * @date 2024/3/15
 */
public interface DevicePropertyService {

    void save(DeviceProperty deviceProperty);

    DeviceProperty getLast(Long productId, Long deviceId);

}
