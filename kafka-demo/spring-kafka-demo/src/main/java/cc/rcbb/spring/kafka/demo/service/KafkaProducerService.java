package cc.rcbb.spring.kafka.demo.service;

import lombok.AllArgsConstructor;
import org.apache.kafka.common.protocol.types.Field;
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

}
