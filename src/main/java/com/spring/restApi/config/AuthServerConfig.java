package com.spring.restApi.config;

import com.spring.restApi.accounts.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

@Configuration
@EnableAuthorizationServer // 토큰 발행, 발행된 토큰 검증
public class AuthServerConfig extends AuthorizationServerConfigurerAdapter {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private AccountService accountService;
    @Autowired
    private TokenStore tokenStore;

    /**
     * 인증 서버 EndPoint 설정
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager)
                .userDetailsService(accountService)
                .tokenStore(tokenStore);
    }

    /**
     * OAuth2 인증 서버 보안(Password) 정보 설정
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.passwordEncoder(passwordEncoder);
    }

    /**
     * API 요청 클라이언트 정보
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        // inMemory() : 클라이언트 정보를 메모리에 저장
        // withClient("myApp") : client_id 값 설정
        clients.inMemory().withClient("myApp")
                .authorizedGrantTypes("password", "refresh_token")
                .scopes("read", "write")
                .secret(this.passwordEncoder.encode("pass"))
                .accessTokenValiditySeconds(10 * 60)
                .refreshTokenValiditySeconds(6 * 10 * 60);
    }
}
