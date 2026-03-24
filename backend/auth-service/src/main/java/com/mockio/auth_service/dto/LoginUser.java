package com.mockio.auth_service.dto;

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
                     String password,
                     int failLoginCount,
                     String status,
                     String role) {
        this.userId = userId;
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
                     String email,
                     String password,
                     String role,
                     int failLoginCount,
                     String status,
                     Map<String, Object> attributes) {
        this.userId = userId;
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
        return null;
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

    @Override
    public String getName() {
        return null;
    }
}
