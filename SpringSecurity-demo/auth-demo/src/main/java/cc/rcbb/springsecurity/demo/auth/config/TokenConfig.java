package cc.rcbb.springsecurity.demo.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

/**
 * <p>
 * TokenConfig
 * </p>
 *
 * @author rcbb.cc
 * @date 2022/10/3
 */
@Configuration
public class TokenConfig {

    @Bean
    public TokenStore tokenStore() {
        // 使用内存存储令牌（普通令牌）
        return new InMemoryTokenStore();
    }

}
