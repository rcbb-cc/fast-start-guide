package cc.rcbb.influxdb.demo.service;

import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.PagerUtils;
import com.baomidou.mybatisplus.plugins.Page;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;

import java.util.*;

/**
 * <p>
 * IInfluxdbService
 * </p>
 *
 * @author rcbb.cc
 * @date 2024/3/15
 */
public interface IInfluxdbService {


    /**
     * 保存点位
     *
     * @param point Point
     */
    void save(Point point);

    /**
     * 获取批量 点位构建器
     *
     * @return BatchPoints Builder
     */
    BatchPoints.Builder getBatchPointsBuilder();

    /**
     * 批量保存数据
     *
     * @param batchPoints BatchPoints
     */
    void save(BatchPoints batchPoints);

    /**
     * 批量保存数据
     *
     * @param pointList point List
     */
    void save(List<Point> pointList);

    /**
     * 根据 sql 查询数据
     *
     * @param sql sql
     * @return 数据集合
     */
    default List<Map<String, Object>> getBySql(String sql) {
        return getBySql(sql, null);
    }

    /**
     * 根据 sql 查询数据
     *
     * @param sql    sql
     * @param params params
     * @return 数据集合
     */
    List<Map<String, Object>> getBySql(String sql, Map<String, Object> params);

    /**
     * 根据 sql 查询数量
     *
     * @param sql sql
     * @return 数据集合
     */
    default long getCountBySql(String sql) {
        return getCountBySql(sql, null);
    }

    /**
     * 根据 sql 查询数量
     *
     * @param sql    sql
     * @param params 参数
     * @return 数据集合
     */
    default long getCountBySql(String sql, Map<String, Object> params) {
        List<Map<String, Object>> mapList = this.getBySql(sql, params);
        if (mapList == null || mapList.isEmpty()) {
            return 0L;
        }
        long total = 0;
        for (Map<String, Object> objectMap : mapList) {
            Set<Map.Entry<String, Object>> entrySet = objectMap.entrySet();
            for (Map.Entry<String, Object> objectEntry : entrySet) {
                String entryKey = objectEntry.getKey();
                // count(*) 结果，以 count_ 开头
                if (entryKey.startsWith("count_")) {
                    long value = ((Number) objectEntry.getValue()).longValue();
                    if (value > total) {
                        total = value;
                    }
                }
            }
        }
        return total;
    }


    /**
     * 获取设备最新的属性
     *
     * @param sql sql
     * @return 设备属性
     */
    default Map<String, Object> getLastBySql(String sql) {
        return getLastBySql(sql, null);
    }

    /**
     * 获取设备最新的属性
     *
     * @param sql    sql
     * @param params 参数
     * @return 设备属性
     */
    default Map<String, Object> getLastBySql(String sql, Map<String, Object> params) {
        List<Map<String, Object>> mapList = this.getBySql(sql, params);
        // 数据为空
        if (mapList.isEmpty()) {
            return Collections.emptyMap();
        }
        Map<String, Object> result = new HashMap<>();
        // 去除 key last_ 前缀
        mapList.get(0).forEach((key, value) ->
                result.put(key.replace("last_", ""), value)
        );
        return result;
    }

    /**
     * 根据 sql 查询数据
     *
     * @param page page
     * @param sql  sql
     * @return 数据集合
     */
    default Page<Map<String, Object>> getPageBySql(Page<Map<String, Object>> page, String sql) {
        return getPageBySql(page, sql, null);
    }

    /**
     * 根据 sql 查询数据
     *
     * @param page   page
     * @param sql    sql
     * @param params params
     * @return 数据集合
     */
    default Page<Map<String, Object>> getPageBySql(Page<Map<String, Object>> page, String sql, Map<String, Object> params) {
        // 查询数量
        String countSql = PagerUtils.count(sql, DbType.postgresql);
        long count = getCountBySql(countSql, params);
        if (count < 1) {
            return page;
        }
        int pageSize = (int) page.getSize();
        int offset = (int) (pageSize * (page.getCurrent() - 1));
        String pageSql = PagerUtils.limit(sql, DbType.postgresql, offset, pageSize);
        List<Map<String, Object>> mapList = getBySql(pageSql, params);
        page.setRecords(mapList);
        page.setTotal(count);
        return page;
    }


}
