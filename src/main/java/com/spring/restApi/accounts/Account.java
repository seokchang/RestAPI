package com.spring.restApi.accounts;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {
    @Id
    @GeneratedValue
    private Integer id;
    @Column(unique = true)
    private String email;
    private String password;
    /**
     * @ElementCollection : 별도의 Entity를 만들지 않고 Collection 테이블로 사용
     * EAGER Loading(즉시 로딩) : 연관관계에 매핑되어 있는 Entity를 항상 함께 조회
     * Lazy Loading(지연 로딩) : 연관관계에 매핑되어 있는 Entity를 실제 사용할 때 조회
     * @Enumerated : Enum 사용
     */
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(value = EnumType.STRING)
    private Set<AccountRole> roles;
}
