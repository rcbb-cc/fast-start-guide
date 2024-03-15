package cc.rcbb.influxdb.demo.pojo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * DeviceLog
 * </p>
 *
 * @author rcbb.cc
 * @date 2024/3/15
 */
@Accessors(chain = true)
@Data
public class DeviceLog {

    private Long productId;

    private Long deviceId;

    private String topic;

    private String message;

}
