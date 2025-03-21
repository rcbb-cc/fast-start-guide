package cc.rcbb.spring.kafka.demo.service;

import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * <p>
 * KafkaProducerService
 * </p>
 *
 * @author rcbb.cc
 * @date 2024/11/16
 */
@Service
@AllArgsConstructor
public class KafkaProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(String topic, String message) {
        kafkaTemplate.send(topic, message);
    }

    public void sendMessageToPartition(String topic, int partition, String message) {
        kafkaTemplate.send(topic, partition, null, message);
    }

}
