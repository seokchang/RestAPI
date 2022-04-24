package com.spring.restApi.accounts;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * [현재 사용자 조회]
 * User Class를 상속받고, AccountAdapter가 Account를 참조하도록 함(has -a)
 * Account Class의 email, password, roles 정보를 Spring Security User에 저장
 */
public class AccountAdapter extends User {
    private final Account account;

    public AccountAdapter(Account account) {
        super(account.getEmail(), account.getPassword(), authorities(account.getRoles()));
        this.account = account;
    }

    private static Collection<? extends GrantedAuthority> authorities(Set<AccountRole> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.name())).collect(Collectors.toSet());
    }

    public Account getAccount() {
        return account;
    }
}
