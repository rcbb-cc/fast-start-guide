package cc.rcbb.sharding.test.config;

import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;

import java.util.Collection;
import java.util.Date;

@Slf4j
public class TableShardingAlgorithm implements PreciseShardingAlgorithm<Date> {
    @Override
    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<Date> shardingValue) {
        log.info("[分表算法]");

        // 真实节点
        availableTargetNames.stream().forEach((item) -> {
            log.info("[存在的表<{}>]", item);
        });
        log.info("[表名<{}> 列名<{}>]", shardingValue.getLogicTableName(), shardingValue.getColumnName());

        //精确分片
        log.info("[分表列的值<{}>]", shardingValue.getValue());
        // 根据当前日期来分库分表
        Date date = shardingValue.getValue();
        String tbName = shardingValue.getLogicTableName() + "_";
        int year = DateUtil.year(date);
        int month = DateUtil.month(date) + 1;
        int day = DateUtil.dayOfMonth(date);
        log.info("[year<{}> month<{}> day<{}>]", year, month, day);
        tbName = tbName + year + "_" + month + "_" + day;
        log.info("[分片确定的表名<{}>]", tbName);

        for (String each : availableTargetNames) {
            if (each.equals(tbName)) {
                return each;
            }
        }
        throw new IllegalArgumentException();
    }
}