package cc.rcbb.spring.kafka.demo.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Component;

/**
 * <p>
 * KafkaConsumer
 * </p>
 *
 * @author rcbb.cc
 * @date 2024/11/16
 */

@Component
@Slf4j
public class KafkaConsumer {

    @KafkaListener(topicPartitions = {
            @TopicPartition(topic = "demo-topic", partitions = {"0"})
    })
    public void listen0(ConsumerRecord<?, ?> record) {
        log.info("[监听 topic<{}> partition<{}> offset<{}> value<{}>]", record.topic(), record.partition(), record.offset(), record.value());
    }

    @KafkaListener(topicPartitions = {
            @TopicPartition(topic = "demo-topic", partitions = {"1"})
    })
    public void listen1(ConsumerRecord<?, ?> record) {
        log.info("[监听 topic<{}> partition<{}> offset<{}> value<{}>]", record.topic(), record.partition(), record.offset(), record.value());
    }


    @KafkaListener(topicPartitions = {
		@TopicPartition(topic = "health-upload", partitions = {"0"})
	})
	public void deviceHealthUpload0(ConsumerRecord<?, ?> record) {
		        log.info("[监听 topic<{}> partition<{}> offset<{}> value<{}>]", record.topic(), record.partition(), record.offset(), record.value());

	}

	@KafkaListener(topicPartitions = {
		@TopicPartition(topic = "health-upload", partitions = {"1"})
	})
	public void deviceHealthUpload1(ConsumerRecord<?, ?> record) {
        log.info("[监听 topic<{}> partition<{}> offset<{}> value<{}>]", record.topic(), record.partition(), record.offset(), record.value());

	}

	@KafkaListener(topicPartitions = {
		@TopicPartition(topic = "health-upload", partitions = {"2"})
	})
	public void deviceHealthUpload2(ConsumerRecord<?, ?> record) {
        log.info("[监听 topic<{}> partition<{}> offset<{}> value<{}>]", record.topic(), record.partition(), record.offset(), record.value());

	}

	@KafkaListener(topicPartitions = {
		@TopicPartition(topic = "health-upload", partitions = {"3"})
	})
	public void deviceHealthUpload3(ConsumerRecord<?, ?> record) {
        log.info("[监听 topic<{}> partition<{}> offset<{}> value<{}>]", record.topic(), record.partition(), record.offset(), record.value());

	}
}
