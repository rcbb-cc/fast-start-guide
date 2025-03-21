package cc.rcbb.spring.kafka.demo;

import cc.rcbb.spring.kafka.demo.service.KafkaProducerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@SpringBootTest
class SpringKafkaDemoApplicationTests {

    @Autowired
    private KafkaProducerService kafkaProducerService;
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Test
    void sendMessageTest() {
        kafkaProducerService.sendMessage("demo-topic", "kvalue");
    }

    @Test
    void sendMessageToPartitionTest() {
        String message = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
        kafkaTemplate.send("demo-topic", 2, null, message);
    }

    @Test
    void sendMessageRandomToPartitionTest() throws InterruptedException {
        int count = 100;
        for (int i = 0; i < count; i++) {
            int partition = i % 2;
            String message = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
            kafkaProducerService.sendMessageToPartition("demo-topic", partition, message);
            Thread.sleep(1000);
        }
    }

}
