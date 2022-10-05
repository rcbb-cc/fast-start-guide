package cc.rcbb.springsecurity.demo.auth.config;

import cc.rcbb.springsecurity.demo.auth.grant.SmsVerificationCodeTokenGranter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.CompositeTokenGranter;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * AuthorizationServerConfig
 * </p>
 *
 * @author rcbb.cc
 * @date 2022/10/1
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private TokenStore tokenStore;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ClientDetailsService clientDetailsService;
    @Autowired
    private AuthenticationManager authenticationManager;


    //令牌管理服务
    @Bean
    public AuthorizationServerTokenServices tokenService() {
        DefaultTokenServices service = new DefaultTokenServices();
        // 客户端详情服务
        service.setClientDetailsService(clientDetailsService);
        // 支持刷新令牌
        service.setSupportRefreshToken(true);
        // 令牌存储策略
        service.setTokenStore(tokenStore);
        // 令牌默认有效期2小时
        service.setAccessTokenValiditySeconds(7200);
        // 刷新令牌默认有效期3天
        service.setRefreshTokenValiditySeconds(259200);
        return service;
    }

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
                .authorizedGrantTypes("authorization_code", "password", "client_credentials", "implicit", "refresh_token","sms_code")
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
        List<TokenGranter> tokenGranters = new ArrayList<>(Collections.singleton(endpoints.getTokenGranter()));
        tokenGranters.add(new SmsVerificationCodeTokenGranter(authenticationManager, tokenService(), clientDetailsService, new DefaultOAuth2RequestFactory(clientDetailsService)));

        // 配置认证管理器
        endpoints.authenticationManager(authenticationManager)
                .tokenServices(tokenService())
                .tokenGranter(new CompositeTokenGranter(tokenGranters));
    }

    /**
     * @Description: 用来配置令牌端点(Token Endpoint)的安全约束
     * Token令牌默认是有签名的，并且资源服务需要验证这个签名，
     * 因此呢，你需要使用一个对称的Key值，用来参与签名计算，
     * 这个Key值存在于授权服务以及资源服务之中。
     * 或者你可以使用非对称加密算法来对Token进行签名，
     * Public Key公布在/oauth/token_key这个URL连接中，
     * 默认的访问安全规则是"denyAll()"，
     * 即在默认的情况下它是关闭的，
     * 你可以注入一个标准的 SpEL 表达式到 AuthorizationServerSecurityConfigurer 这个配置中来将它开启
     * （例如使用"permitAll()"来开启可能比较合适，因为它是一个公共密钥）。
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security
                // 开放获取token key(这个是做JWT的时候开放的！也就是资源服可以通过这个请求得到JWT加密的key，目前这里没什么用，可以不用配置)
                .tokenKeyAccess("permitAll()")
                // 开放远程token检查
                .checkTokenAccess("permitAll()")
                // 允许client使用form的方式进行authentication的授权
                .allowFormAuthenticationForClients();
    }
}
