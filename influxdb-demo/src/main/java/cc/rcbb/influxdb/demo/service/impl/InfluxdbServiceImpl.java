package cc.rcbb.influxdb.demo.service.impl;

import cc.rcbb.influxdb.demo.config.InfluxdbProperties;
import cc.rcbb.influxdb.demo.service.IInfluxdbService;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.influxdb.InfluxDB;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * InfluxdbServiceImpl
 * </p>
 *
 * @author rcbb.cc
 * @date 2024/3/15
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class InfluxdbServiceImpl implements IInfluxdbService {

    private final InfluxDB influxdb;
    private final InfluxdbProperties influxdbProperties;

    @Override
    public void save(Point point) {
        influxdb.write(point);
    }

    @Override
    public BatchPoints.Builder getBatchPointsBuilder() {
        BatchPoints.Builder builder = BatchPoints.database(influxdbProperties.getDatabase());
        builder.retentionPolicy(influxdbProperties.getRetentionPolicy());
        builder.consistency(influxdbProperties.getConsistencyLevel());
        builder.precision(influxdbProperties.getPrecision());
        return builder;
    }

    @Override
    public void save(BatchPoints batchPoints) {
        influxdb.write(batchPoints);
    }

    @Override
    public void save(List<Point> pointList) {
        BatchPoints.Builder pointsBuilder = this.getBatchPointsBuilder();
        pointList.forEach(pointsBuilder::points);
        influxdb.write(pointsBuilder.build());
    }

    @Override
    public List<Map<String, Object>> getBySql(String sql, Map<String, Object> params) {
        // 按占位符替换参数
        String formattedSql = StrUtil.format(sql, params);
        log.info("[Influxdb Query SQL:\n{}\n]", formattedSql);
        Query query = new Query(formattedSql);
        QueryResult queryResult = influxdb.query(query, TimeUnit.MILLISECONDS);
        List<QueryResult.Result> queryResultResults = queryResult.getResults();
        List<Map<String, Object>> dataList = new ArrayList<>();
        for (QueryResult.Result result : queryResultResults) {
            List<QueryResult.Series> seriesList = result.getSeries();
            // seriesList 可能为 null
            if (seriesList == null) {
                continue;
            }
            for (QueryResult.Series series : seriesList) {
                // 列名称
                List<String> columns = series.getColumns();
                // 数据值
                List<List<Object>> valuesList = series.getValues();
                for (List<Object> values : valuesList) {
                    Map<String, Object> data = new HashMap<>();
                    for (int i = 0; i < values.size(); i++) {
                        String column = columns.get(i);
                        Object value = values.get(i);
                        data.put(column, value);
                    }
                    dataList.add(data);
                }
            }
        }
        return dataList;
    }
}
