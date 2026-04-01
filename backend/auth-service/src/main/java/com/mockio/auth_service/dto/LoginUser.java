package com.mockio.auth_service.dto;

/**
 * 인증된 사용자 정보를 표현하는 Security Principal 객체
 *
 * Spring Security의 UserDetails와 OAuth2User를 동시에 구현하여
 * 일반 로그인과 OAuth2 로그인을 하나의 통합 모델로 처리한다.
 *
 * 사용자 식별 정보(userId, email), 인증 정보(password),
 * 권한(role), 계정 상태(status), 로그인 실패 횟수 등을 포함하며,
 * 인증 및 인가 과정에서 SecurityContext에 저장되어 사용된다.
 *
 * OAuth2 로그인 시에는 외부 제공자의 사용자 정보(attributes)를 함께 보관한다.
 */

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Getter
public class LoginUser implements UserDetails , OAuth2User {

    private final Long userId;
    private final String email;
    private final String name;
    private final String password;
    private final String role;
    private Map<String , Object> attributes;
    private final int failLoginCount;
    private final String status;

    /**
     * 일반 로그인
     */
    public LoginUser(Long userId,
                     String email,
                     String name,
                     String password,
                     int failLoginCount,
                     String status,
                     String role) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.status = status;
        this.failLoginCount = failLoginCount;
    }

    /**
     * OAuth 로그인
     * @param attributes
     */
    public LoginUser(Long userId,
                     String name,
                     String email,
                     String password,
                     int failLoginCount,
                     String status,
                     String role,
                     Map<String, Object> attributes) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.failLoginCount = failLoginCount;
        this.status = status;
        this.role = role;
        this.attributes = attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(() -> "ROLE_" + role);
        return authorities;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return name;
    }

    /**
     * 휴면 계정 or 컬럼 생성(true , false) 해도 됨
     * 현재 일자 기준
     * @return
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 비밀번호 오류 5회 이상
     * @return
     */
    @Override
    public boolean isAccountNonLocked() {
        return this.failLoginCount <=4;
    }

    /**
     * 비밀번호 만료
     * @return
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 탈퇴여부
     * @return
     */
    @Override
    public boolean isEnabled() {
        return this.status != "ACTIVE";
    }

}
