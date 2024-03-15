package cc.rcbb.influxdb.demo.pojo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;

/**
 * <p>
 * DeviceProperty
 * </p>
 *
 * @author rcbb.cc
 * @date 2024/3/15
 */
@Accessors(chain = true)
@Data
public class DeviceProperty {

    private Long productId;

    private Long deviceId;

    private Map<String, Object> paramMap;

}
