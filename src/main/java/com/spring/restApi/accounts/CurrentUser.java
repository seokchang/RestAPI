package com.spring.restApi.accounts;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER) // Annotation 적용 위치 결정
@Retention(RetentionPolicy.RUNTIME) // 어떤 시점까지 Annotation이 영향을 미치는지 결정
@AuthenticationPrincipal(expression = "#this == 'anonymousUser' ? null : account")
public @interface CurrentUser {
}
