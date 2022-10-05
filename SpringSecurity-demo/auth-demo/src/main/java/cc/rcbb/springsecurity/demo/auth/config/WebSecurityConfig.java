package cc.rcbb.springsecurity.demo.auth.config;

import cc.rcbb.springsecurity.demo.auth.grant.SmsVerificationCodeAuthenticationConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * <p>
 * WebSecurityConfig
 * </p>
 *
 * @author rcbb.cc
 * @date 2022/10/1
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private SmsVerificationCodeAuthenticationConfig smsVerificationCodeAuthenticationConfig;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Autowired
    public PasswordEncoder passwordEncoders() {
        return new BCryptPasswordEncoder();
    }


    @Autowired
    public void globalUserDetails(AuthenticationManagerBuilder auth) throws Exception {
        // 配置全局用户信息
        auth.inMemoryAuthentication().withUser("admin")
                .password(passwordEncoders().encode("123456"))
                .authorities(AuthorityUtils.commaSeparatedStringToAuthorityList("user_add,user_update,ROLE_ADMIN"));
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.apply(smsVerificationCodeAuthenticationConfig);
    }
}
