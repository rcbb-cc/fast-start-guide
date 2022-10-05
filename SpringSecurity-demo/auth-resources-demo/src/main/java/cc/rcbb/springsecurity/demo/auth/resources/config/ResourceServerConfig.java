package cc.rcbb.springsecurity.demo.auth.resources.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;

/**
 * <p>
 * ResourceServerConfig
 * </p>
 *
 * @author rcbb.cc
 * @date 2022/10/3
 */
@Configuration
@EnableResourceServer
// 开启注解鉴权
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    //配置资源服安全规则
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                // 授权的请求
                .authorizeRequests()
                // 任何请求
                .anyRequest()
                // 需要身份认证
                .authenticated()
                .and().csrf().disable();
    }
}
