package com.spring.restApi.config;

import com.spring.restApi.accounts.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

@Configuration
/**
 * @EnableWebSecurity
 * 웹 보안 활성화
 * WebSecurityConfigurerAdapter를 확장한 Bean으로 설정해야 함
 */
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private AccountService accountService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.anonymous() // 익명 사용자 활성화
                .and()
                .formLogin() // Form 인증 방식 활성화, Spring Security가 제공하는 기본 로그인 페이지
                .and()
                .authorizeRequests() // 요청에 인증 적용
                .mvcMatchers(HttpMethod.GET, "/api/**").permitAll() // '/api' 이하 모든 GET 요청은 인증 필요 없음
                .anyRequest().authenticated(); // 그 밖의 모든 요청(POST)은 인증 필요
    }

    /**
     * [Token 저장]
     * Map, Queue 구조의 메모리를 사용하는 저장소
     */
    @Bean
    public TokenStore tokenStore() {
        return new InMemoryTokenStore();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * 인증 객체 생성 제공
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(accountService).passwordEncoder(passwordEncoder);
    }
}
