package com.mockio.user_service.config;

import com.mockio.common_security.annotation.CurrentUserArgumentResolver;
import com.mockio.common_security.util.CurrentUserFacade;
import com.mockio.common_security.util.CurrentUserPort;
import com.mockio.user_service.domain.UserProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class CurrentUserWiringConfig implements WebMvcConfigurer {

    private final CurrentUserFacade<UserProfile> currentUserFacade;

    @Bean
    public CurrentUserFacade<UserProfile> currentUserFacade(CurrentUserPort<UserProfile> currentUserPort) {
        return new CurrentUserFacade<>(currentUserPort);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new CurrentUserArgumentResolver<>(currentUserFacade, UserProfile.class));
    }
}