package cc.rcbb.sharding.test.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;

import java.util.Collection;
/**
 * <p>
 * DBShardingAlgorithm
 * </p>
 *
 * @author rcbb.cc
 * @date 2021/9/16
 */
@Slf4j
public class DBShardingAlgorithm implements PreciseShardingAlgorithm<Long> {
    @Override
    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<Long> shardingValue) {

        log.info("[分库算法开始]");
        // 真实节点
        availableTargetNames.stream().forEach((item) -> {
            log.info("[存在的库名<{}>]", item);
        });

        log.info("[表名<{}> 列名<{}>]", shardingValue.getLogicTableName(), shardingValue.getColumnName());

        //精确分片
        log.info("[分库列的值<{}>]", shardingValue.getValue());

        long orderId = shardingValue.getValue();

        // 效果等同于 orderId % 2
        long db_index = orderId & 1;

        for (String each : availableTargetNames) {
            if (each.equals("ds" + db_index)) {
                return each;
            }
        }

        throw new IllegalArgumentException();
    }
}