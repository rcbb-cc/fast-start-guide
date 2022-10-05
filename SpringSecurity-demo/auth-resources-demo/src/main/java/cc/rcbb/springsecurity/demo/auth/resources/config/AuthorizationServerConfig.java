package cc.rcbb.springsecurity.demo.auth.resources.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;

/**
 * <p>
 * AuthorizationServerConfig
 * </p>
 *
 * @author rcbb.cc
 * @date 2022/10/4
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        // 基于内存便于测试，使用in-memory存储
        clients.inMemory()
                // client_id
                .withClient("rcbb")
                // 未加密
                //.secret("secret")
                // 加密
                .secret(passwordEncoder.encode("rcbb"))
                // 资源列表
                //.resourceIds("res1")
                // 该client允许的授权类型authorization_code,password,refresh_token,implicit,client_credentials
                .authorizedGrantTypes("authorization_code", "password", "client_credentials", "implicit", "refresh_token")
                // 允许的授权范围
                .scopes("all", "ROLE_ADMIN", "ROLE_USER")
                // false跳转到授权页面
                //.autoApprove(false)
                //加上验证回调地址
                .redirectUris("http://rcbb.cc");
    }

    /**
     * 配置令牌的访问端点和令牌服务
     *
     * @param endpoints the endpoints configurer
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        // 配置认证管理器
        endpoints.authenticationManager(authenticationManager);
    }

}
