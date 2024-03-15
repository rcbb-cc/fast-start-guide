package cc.rcbb.influxdb.demo.config;

import lombok.Data;
import org.influxdb.InfluxDB;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.concurrent.TimeUnit;

/**
 * <p>
 * InfluxdbProperties
 * </p>
 *
 * @author rcbb.cc
 * @date 2024/3/15
 */
@Data
@ConfigurationProperties(prefix = "influxdb")
public class InfluxdbProperties {

    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 连接地址
     */
    private String url;
    /**
     * 数据库名
     */
    private String database;
    /**
     * 开启批量。默认：true
     */
    private boolean enableBatch = true;
    /**
     * 开启 gzip 压缩，默认：true
     */
    private boolean enableGzip = true;
    /**
     * 日志级别，默认为：NONE
     */
    private InfluxDB.LogLevel logLevel = InfluxDB.LogLevel.NONE;
    /**
     * 存储策略
     */
    private String retentionPolicy;
    /**
     * 写操作一致性级别，默认：one
     */
    private InfluxDB.ConsistencyLevel consistencyLevel = InfluxDB.ConsistencyLevel.ONE;
    /**
     * 精度，默认是：纳秒
     */
    private TimeUnit precision;

}

