package cc.rcbb.spring.kafka.demo.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * <p>
 * DemoTopicConsumer
 * </p>
 *
 * @author rcbb.cc
 * @date 2025/3/18
 */
@Slf4j
@Component
public class DemoTopicConsumer {

    @KafkaListener(id = "demoTopicListen", groupId = "demoTopicListen", topics = "demo-topic", concurrency = "1")
    public void demoTopicListen(ConsumerRecord<?, ?> record) {
        log.info("[监听threadName<{}> topic<{}> partition<{}> offset<{}> value<{}>]", Thread.currentThread().getName(), record.topic(), record.partition(), record.offset(), record.value());
    }

}
