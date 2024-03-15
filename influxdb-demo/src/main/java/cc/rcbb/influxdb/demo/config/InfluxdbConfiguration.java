package cc.rcbb.influxdb.demo.config;

import okhttp3.OkHttpClient;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * InfluxdbConfiguration
 * </p>
 *
 * @author rcbb.cc
 * @date 2024/3/15
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(InfluxdbProperties.class)
public class InfluxdbConfiguration {

	@Bean
	public InfluxDB influxdb(InfluxdbProperties influxdbProperties) {
		String url = influxdbProperties.getUrl();
		String username = influxdbProperties.getUsername();
		String password = influxdbProperties.getPassword();
		// 使用 MSGPACK 序列化，数据更小，类型不丢失
		InfluxDB influxDB = InfluxDBFactory.connect(
				url, username, password, new OkHttpClient.Builder(), InfluxDB.ResponseFormat.MSGPACK
		);
		// 开启批量
		if (influxdbProperties.isEnableBatch()) {
			influxDB.enableBatch();
		}
		// 开启 gzip
		if (influxdbProperties.isEnableGzip()) {
			influxDB.enableGzip();
		}
		// 数据库，自动创建库
		String database = influxdbProperties.getDatabase();
		influxDB.createDatabase(database);
		influxDB.setDatabase(database);
		// 日志等级
		influxDB.setLogLevel(influxdbProperties.getLogLevel());
		influxDB.setRetentionPolicy(influxdbProperties.getRetentionPolicy());
		influxDB.setConsistency(influxdbProperties.getConsistencyLevel());
		return influxDB;
	}

}
