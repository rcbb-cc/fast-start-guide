package cc.rcbb.mail.demo.hutool;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * HutoolMailSender
 * </p>
 *
 * @author rcbb.cc
 * @date 2023/2/7
 */
@Slf4j
@UtilityClass
public class HutoolMailSender {

    public void send(HutoolMailSendInfo dto) {
        log.error("[邮件发送 dto<{}>]", dto);
        try {
            MailUtil.send(getMailAccount(dto), dto.getToUsers(), dto.getTitle(), dto.getContent(), Boolean.TRUE);
            log.error("[邮件发送成功 dto<{}>]", dto);
        } catch (Exception e) {
            log.error("[邮件发送出现异常 <{}>]", e.getMessage(), e);
        }
    }

    private MailAccount getMailAccount(HutoolMailSendInfo info) {
        MailAccount account = new MailAccount();
        account.setHost(info.getHost());
        account.setPort(info.getPort());
        account.setAuth(info.getAuth());
        account.setFrom(info.getEmail());
        if (StrUtil.isNotBlank(info.getPersonal())) {
            // Test<xxxx@xxx.com>
            account.setFrom(info.getPersonal() + "<" + info.getEmail() + ">");
        }
        if (info.getStarttlsEnable()) {
            account.setStarttlsEnable(Boolean.TRUE);
            account.setSslEnable(Boolean.FALSE);
        } else {
            account.setSslEnable(Boolean.TRUE);
        }
        account.setUser(info.getUsername());
        account.setPass(info.getPassword());
        return account;
    }

}
