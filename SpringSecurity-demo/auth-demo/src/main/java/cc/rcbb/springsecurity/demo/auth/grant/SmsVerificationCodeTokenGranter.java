package cc.rcbb.springsecurity.demo.auth.grant;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AbstractTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <p>
 * SmsVerificationCodeTokenGranter
 * </p>
 *
 * @author rcbb.cc
 * @date 2022/10/5
 */
@Slf4j
public class SmsVerificationCodeTokenGranter extends AbstractTokenGranter {

    /**
     * 授权类型，和password是一样的作用
     */
    private static final String grantType = "sms_code";

    private final AuthenticationManager authenticationManager;

    public SmsVerificationCodeTokenGranter(AuthenticationManager authenticationManager, AuthorizationServerTokenServices tokenServices, ClientDetailsService clientDetailsService, OAuth2RequestFactory requestFactory) {
        this(authenticationManager, tokenServices, clientDetailsService, requestFactory, grantType);
    }

    protected SmsVerificationCodeTokenGranter(AuthenticationManager authenticationManager, AuthorizationServerTokenServices tokenServices, ClientDetailsService clientDetailsService, OAuth2RequestFactory requestFactory, String grantType) {
        super(tokenServices, clientDetailsService, requestFactory, grantType);
        this.authenticationManager = authenticationManager;
    }

    @Override
    protected OAuth2Authentication getOAuth2Authentication(ClientDetails client, TokenRequest tokenRequest) {
        // 得到请求参数
        Map<String, String> parameters = new LinkedHashMap<>(tokenRequest.getRequestParameters());
        // 手机号
        String phone = parameters.get("phone");
        // 验证码
        String verificationCode = parameters.get("verification_code");
        log.info("[登录请求 phone<{}> verificationCode<{}>]", phone, verificationCode);

        Authentication userAuth = new SmsVerificationCodeAuthenticationToken(parameters);
        ((AbstractAuthenticationToken) userAuth).setDetails(parameters);
        try {
            // 调用authenticationManager进行认证，会根据SmsVerificationCodeAuthenticationToken找到对应的Provider
            userAuth = authenticationManager.authenticate(userAuth);
        } catch (AccountStatusException ase) {
            //covers expired, locked, disabled cases (mentioned in section 5.2, draft 31)
            throw new InvalidGrantException(ase.getMessage());
        } catch (BadCredentialsException e) {
            // If the username/password are wrong the spec says we should send 400/invalid grant
            throw new InvalidGrantException(e.getMessage());
        }
        if (userAuth == null || !userAuth.isAuthenticated()) {
            throw new InvalidGrantException("Could not authenticate user,phone number is: " + phone);
        }
        OAuth2Request storedOAuth2Request = getRequestFactory().createOAuth2Request(client, tokenRequest);
        return new OAuth2Authentication(storedOAuth2Request, userAuth);
    }
}
