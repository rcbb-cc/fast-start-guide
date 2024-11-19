package cc.rcbb.spring.kafka.demo;

import cc.rcbb.spring.kafka.demo.service.KafkaProducerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringKafkaDemoApplicationTests {

    @Autowired
    private KafkaProducerService kafkaProducerService;

    @Test
    void sendTest() {
        kafkaProducerService.sendMessage("demo-topic", "kvalue");
    }

     @Test
    void sendTest2() {
        kafkaProducerService.sendMessage("health-upload", "1");
    }


}
