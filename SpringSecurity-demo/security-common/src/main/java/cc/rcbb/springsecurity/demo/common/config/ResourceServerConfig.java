package cc.rcbb.springsecurity.demo.common.config;

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
    @Bean
    @Primary
    public ResourceServerTokenServices tokenServices() {
        // 如果资源服务和认证服务在一起可以使用 DefaultTokenServices 进行本地校验
        // 使用远程服务请求授权服务器校验token，必须指定url，client_id，client_secret
        RemoteTokenServices services = new RemoteTokenServices();
        services.setCheckTokenEndpointUrl("http://127.0.0.1:3000/oauth/check_token");
        services.setClientId("rcbb");
        services.setClientSecret("rcbb");
        return services;
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources
                // 验证令牌的服务
                .tokenServices(tokenServices())
                .stateless(true);
    }

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
