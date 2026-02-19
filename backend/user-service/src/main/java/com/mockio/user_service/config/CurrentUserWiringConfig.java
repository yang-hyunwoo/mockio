package com.mockio.user_service.config;

import com.mockio.common_security.annotation.CurrentUserArgumentResolver;
import com.mockio.common_security.util.CurrentUserFacade;
import com.mockio.common_security.util.CurrentUserPort;
import com.mockio.user_service.domain.UserProfile;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class CurrentUserWiringConfig implements WebMvcConfigurer {

    private final CurrentUserPort<UserProfile> currentUserPort;

    public CurrentUserWiringConfig(CurrentUserPort<UserProfile> currentUserPort) {
        this.currentUserPort = currentUserPort;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        CurrentUserFacade<UserProfile> facade = new CurrentUserFacade<>(currentUserPort);
        resolvers.add(new CurrentUserArgumentResolver<>(facade, UserProfile.class));
    }

}