# ShardingSphere4

使用 ShardingSphere4.1.1 进行数据分片。

自定义分库、分表算法，实现按日期进行分表。

完整代码Github地址：
> https://github.com/rcbb-cc/ShardingSphere-4.1.1-test

项目中使用的是 PostgreSQL，如需要切换为 MySQL，仅需切换配置文件中 `driver-class-name` 和 `url` 等配置即可。

# ShardingSphere

> Sharding-Sphere是一套开源的分布式数据库中间件解决方案组成的生态圈，它由Sharding-JDBC、Sharding-Proxy和Sharding-Sidecar这3款相互独立的产品组成。他们均提供标准化的数据分片、读写分离、柔性事务和数据治理功能，可适用于如Java同构、异构语言、容器、云原生等各种多样化的应用场景。

ShardingSphere官网
> https://shardingsphere.apache.org/

ShardingSphere的当前版本

![ShardingSphere版本](https://rcbb-blog.oss-cn-guangzhou.aliyuncs.com/2021/09/20210917110446-a3c746.png?x-oss-process=style/yuantu_shuiyin)

由于5.x 版本目前还在 bate 版，线上使用的话，推荐使用稳定的 4.x 版本。


# 快速上手


## 依赖

ShardingSphere4.1.1 是需要引入两个包的。

~~~
<properties>
    <sharding-sphere.version>4.1.1</sharding-sphere.version>
</properties>
 <!-- for spring boot -->
<dependency>
    <groupId>org.apache.shardingsphere</groupId>
    <artifactId>sharding-jdbc-spring-boot-starter</artifactId>
    <version>${sharding-sphere.version}</version>
</dependency>

<!-- for spring namespace -->
<dependency>
    <groupId>org.apache.shardingsphere</groupId>
    <artifactId>sharding-jdbc-spring-namespace</artifactId>
    <version>${sharding-sphere.version}</version>
</dependency>
~~~

## 规则配置

官方有详细案例：
> https://shardingsphere.apache.org/document/4.1.1/cn/manual/sharding-jdbc/usage/sharding/

yml 配置如下：

我使用的是 PostgreSQL，若切换为 MySQL，仅需修改 `driver-class-name` 和 `url` 即可。

使用了多数据源，`t_order` 表根据 `order_id` 进行分片。

~~~
spring:
  shardingsphere:
    datasource:
      names: ds0,ds1
      ds0:
        driver-class-name: org.postgresql.Driver
        url: jdbc:postgresql://192.168.1.56:5433/sharding_test?&ssl=false&stringtype=unspecified
        type: com.alibaba.druid.pool.DruidDataSource
        username: postgres
        password: jetlinks
      ds1:
        driver-class-name: org.postgresql.Driver
        url: jdbc:postgresql://192.168.1.56:5433/sharding_test?&ssl=false&stringtype=unspecified
        type: com.alibaba.druid.pool.DruidDataSource
        username: postgres
        password: jetlinks
    props:
      sql:
        show: true
    sharding:
      default-data-source-name: ds0
      tables:
        t_order:
          actual-data-nodes: ds0.t_order$->{0..3}
          table-strategy:
            inline:
              algorithm-expression: t_order$->{order_id % 4}
              sharding-column: order_id
~~~

## 测试

1. 首先按照配置文件中的配置规则，创建 `t_order` 表，需要创建 `t_order0` `t_order1` `t_order2` `t_order3`  是个表。
2. 创建对应的实体、Mapper 来进行测试。

![测试Test](https://rcbb-blog.oss-cn-guangzhou.aliyuncs.com/2021/09/20210918143648-7ee0db.png?x-oss-process=style/yuantu_shuiyin)

最后的效果，即四个表中，一个表中一条记录。

![四个表，四条记录](https://rcbb-blog.oss-cn-guangzhou.aliyuncs.com/2021/09/20210918144011-eaf1d7.png?x-oss-process=style/yuantu_shuiyin)

分表成功。

# 自定义分库/分表算法

有时候根据我们的业务场景，需要根据自己的一些逻辑来进行分库分表。

例如：根据日期进行分表，一天一张表。

这个时候就需要进行自定义分库、分表算法了。


## 自定义分库算法

自定义分库算法：根据某个全局唯一的值。


```
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
```


## 自定义分表算法

自定义分表算法：根据日期来进行分表。

```
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
```

## 完整配置


```
spring:
  shardingsphere:
    datasource:
      names: ds0,ds1
      ds0:
        driver-class-name: org.postgresql.Driver
        url: jdbc:postgresql://192.168.1.56:5433/sharding_test?&ssl=false&stringtype=unspecified
        type: com.alibaba.druid.pool.DruidDataSource
        username: postgres
        password: jetlinks
      ds1:
        driver-class-name: org.postgresql.Driver
        url: jdbc:postgresql://192.168.1.56:5433/sharding_test?&ssl=false&stringtype=unspecified
        type: com.alibaba.druid.pool.DruidDataSource
        username: postgres
        password: jetlinks
    props:
      sql:
        show: true
    sharding:
      default-data-source-name: ds0
      tables:
        t_order:
          actual-data-nodes: ds0.t_order$->{0..3}
          table-strategy:
            inline:
              algorithm-expression: t_order$->{order_id % 4}
              sharding-column: order_id
        t_position:
          actual-data-nodes: ds$->{0..1}.t_position_$->{2020..2021}_$->{1..12}_$->{1..31}
          database-strategy:
            standard:
              precise-algorithm-class-name: cc.rcbb.sharding.test.config.DBShardingAlgorithm
              sharding-column: position_id
          table-strategy:
            standard:
              precise-algorithm-class-name: cc.rcbb.sharding.test.config.TableShardingAlgorithm
              sharding-column: loc_date
```

