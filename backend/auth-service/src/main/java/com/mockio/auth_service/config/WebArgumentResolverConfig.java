package com.mockio.auth_service.config;

/**
 * Spring MVC 커스텀 ArgumentResolver 등록 설정 클래스
 *
 * 컨트롤러 메서드 파라미터에 특정 객체를 자동으로 주입하기 위해
 * HandlerMethodArgumentResolver를 등록한다.
 *
 * CurrentSubjectArgumentResolver를 통해
 * 인증된 사용자 정보 또는 공통 요청 데이터를 컨트롤러에 직접 전달할 수 있다.
 */

import com.mockio.common_security.annotation.CurrentSubjectArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebArgumentResolverConfig implements WebMvcConfigurer {
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new CurrentSubjectArgumentResolver());
    }

}
