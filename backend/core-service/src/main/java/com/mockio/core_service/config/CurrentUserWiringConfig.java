package com.mockio.core_service.config;

import com.mockio.common_security.annotation.CurrentUserArgumentResolver;
import com.mockio.common_security.util.CurrentUserFacade;
import com.mockio.common_security.util.CurrentUserPort;
import com.mockio.core_service.user.domain.User;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class CurrentUserWiringConfig implements WebMvcConfigurer {

    private final CurrentUserPort<User> currentUserPort;

    public CurrentUserWiringConfig(CurrentUserPort<User> userPort) {
        this.currentUserPort = userPort;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        CurrentUserFacade<User> facade = new CurrentUserFacade<>(currentUserPort);
        resolvers.add(new CurrentUserArgumentResolver<>(facade, User.class));
    }

}