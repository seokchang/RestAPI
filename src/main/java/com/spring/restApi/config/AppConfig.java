package com.spring.restApi.config;

import com.spring.restApi.accounts.Account;
import com.spring.restApi.accounts.AccountRole;
import com.spring.restApi.accounts.AccountService;
import com.spring.restApi.common.AppProperties;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
public class AppConfig {
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    /**
     * PasswordEncoder : 사용자가 등록한 비밀번호를 단방향으로 변환하여 저장하는 용도로 사용
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public ApplicationRunner applicationRunner() {
        return new ApplicationRunner() {
            @Autowired
            AccountService accountService;
            @Autowired
            AppProperties appProperties;

            @Override
            public void run(ApplicationArguments args) throws Exception {
                Account account = Account.builder()
                        .email(appProperties.getUserUsername())
                        .password(appProperties.getUserPassword())
                        .roles(Set.of(AccountRole.ADMIN, AccountRole.USER))
                        .build();
                accountService.saveAccount(account);
            }
        };
    }
}
