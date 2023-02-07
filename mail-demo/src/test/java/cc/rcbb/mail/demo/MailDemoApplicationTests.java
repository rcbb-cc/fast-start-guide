package cc.rcbb.mail.demo;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

@Slf4j
@SpringBootTest
class MailDemoApplicationTests {

    @Autowired
    private MailSender mailSender;

    @Test
    void contextLoads() {

        String from = "jxsmtp101@outlook.com";
        String to = "javacourse001@sina.com";
        String subject = "123";
        String content = "123";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);

        try {
            mailSender.send(message);
            log.info("简单邮件已经发送");
        } catch (Exception e) {
            log.error("发送简单邮件时发生异常", e);
        }
    }

}
