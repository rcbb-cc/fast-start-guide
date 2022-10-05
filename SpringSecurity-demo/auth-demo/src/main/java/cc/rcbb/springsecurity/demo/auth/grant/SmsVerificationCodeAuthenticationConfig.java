package cc.rcbb.springsecurity.demo.auth.grant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.stereotype.Component;

/**
 * <p>
 * SmsVerificationCodeAuthenticationConfig
 * </p>
 *
 * @author rcbb.cc
 * @date 2022/10/5
 */
@Component
public class SmsVerificationCodeAuthenticationConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    public void configure(HttpSecurity builder) throws Exception {
        SmsVerificationCodeAuthenticationProvider provider = new SmsVerificationCodeAuthenticationProvider(userDetailsService);
        builder.authenticationProvider(provider);
    }
}