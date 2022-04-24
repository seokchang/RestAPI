package com.spring.restApi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;

@Configuration
/**
 * @EnableResourceServer
 * 발행된 토큰을 사용하여 API를 호출할 때, 권한을 검증하는 필터 역할
 */
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
    private static final String[] AUTH_LIST = {
            "/v2/api-docs",
            "/configuration/ui",
            "/swagger-resources/**",
            "/configuration/security",
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/webjars/**"
    };

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId("event");
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.anonymous()
                .and()
                .authorizeRequests()
                .mvcMatchers(HttpMethod.GET, "/api/**").permitAll()
                .antMatchers(AUTH_LIST).permitAll()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling()
                .accessDeniedHandler(new OAuth2AccessDeniedHandler());
    }
}
