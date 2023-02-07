package cc.rcbb.mail.demo.hutool;

import cn.hutool.core.util.RandomUtil;

import java.util.List;

/**
 * <p>
 * HutoolMailSenderTest
 * </p>
 *
 * @author rcbb.cc
 * @date 2023/2/7
 */
public class HutoolMailSenderTest {

    public static void main(String[] args) {
        String to = "javacourse001@sina.com";

        HutoolMailSendInfo dto = new HutoolMailSendInfo()
                .setEmail("jxsmtp101@outlook.com")
                .setUsername("jxsmtp101@outlook.com")
                .setPassword("java-12345678")
                .setPersonal("sysadmin")
                .setHost("smtp.office365.com")
                .setPort(465)
                .setAuth(Boolean.TRUE)
                .setStarttlsEnable(Boolean.FALSE)
                .setToUsers(List.of(to))
                .setLogoUrl("");

        String randomCode = RandomUtil.randomNumbers(6);
        dto.setTitle("title-" + randomCode)
                .setContent(randomCode);
        HutoolMailSender.send(dto);
    }

}
