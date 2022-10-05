package cc.rcbb.springsecurity.demo.auth.grant;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Map;

/**
 * <p>
 * SmsVerificationCodeAuthenticationProvider
 * </p>
 *
 * @author rcbb.cc
 * @date 2022/10/5
 */
@Slf4j
public class SmsVerificationCodeAuthenticationProvider implements AuthenticationProvider {

    /**
     * 得到UserDetailsService对象用来获取用户信息
     */
    private UserDetailsService userDetailsService;

    public SmsVerificationCodeAuthenticationProvider(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    /**
     * 过请求参数查询数据库用户信息，进行匹配
     *
     * @param authentication the authentication request object.
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        SmsVerificationCodeAuthenticationToken authenticationToken = (SmsVerificationCodeAuthenticationToken) authentication;
        Map<String, String> parameters = (Map<String, String>) authenticationToken.getPrincipal();
        log.info("[parameters <{}>]", parameters);
        // 手机号
        String phone = parameters.get("phone");
        // 验证码
        String verificationCode = parameters.get("verification_code");
        log.info("[登录请求 phone<{}> verificationCode<{}>]", phone, verificationCode);
        // 1.根据phone查询用户信息是否存在
        // 2.判断验证码 是否有效

        UserDetails user = new User("rcbb", "rcbb", true, true, true, true,
                AuthorityUtils.commaSeparatedStringToAuthorityList("user_add,user_update,ROLE_ADMIN"));

        SmsVerificationCodeAuthenticationToken smsVerificationCodeAuthenticationToken = new SmsVerificationCodeAuthenticationToken(user, user.getAuthorities());
        smsVerificationCodeAuthenticationToken.setDetails(authenticationToken.getDetails());
        return smsVerificationCodeAuthenticationToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        // 判断authentication是否是SmsCodeAuthenticationToken类型
        return SmsVerificationCodeAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
