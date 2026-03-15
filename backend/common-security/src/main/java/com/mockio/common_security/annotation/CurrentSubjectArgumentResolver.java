package com.mockio.common_security.annotation;

import com.mockio.common_security.annotation.CurrentSubject;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@RequiredArgsConstructor
public class CurrentSubjectArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        if (!parameter.hasParameterAnnotation(CurrentSubject.class)) {
            return false;
        }

        Class<?> parameterType = parameter.getParameterType();
        return String.class.equals(parameterType)
                || Long.class.equals(parameterType)
                || long.class.equals(parameterType);
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) {
        CurrentSubject annotation = parameter.getParameterAnnotation(CurrentSubject.class);
        boolean required = annotation == null || annotation.required();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getPrincipal())) {
            if (required) throw new UnauthorizedException("UNAUTHORIZED");
            return null;
        }

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof Jwt jwt)) {
            if (required) throw new UnauthorizedException("UNAUTHORIZED_PRINCIPAL");
            return null;
        }

        Class<?> parameterType = parameter.getParameterType();

        if (String.class.equals(parameterType)) {
            String sub = jwt.getSubject();
            if (sub == null || sub.isBlank()) {
                if (required) throw new UnauthorizedException("MISSING_SUBJECT");
                return null;
            }
            return sub;
        }

        if (Long.class.equals(parameterType) || long.class.equals(parameterType)) {
            Object userIdClaim = jwt.getClaims().get("userId");

            if (userIdClaim instanceof Long l) {
                return l;
            }

            if (userIdClaim instanceof Integer i) {
                return i.longValue();
            }

            if (userIdClaim instanceof String s) {
                try {
                    return Long.valueOf(s);
                } catch (NumberFormatException e) {
                    throw new UnauthorizedException("INVALID_USER_ID_CLAIM");
                }
            }

            String sub = jwt.getSubject();
            if (sub == null || sub.isBlank()) {
                if (required) throw new UnauthorizedException("MISSING_SUBJECT");
                return null;
            }

            try {
                return Long.valueOf(sub);
            } catch (NumberFormatException e) {
                throw new UnauthorizedException("INVALID_SUBJECT_TYPE");
            }
        }

        throw new IllegalArgumentException("@CurrentSubject는 String 또는 Long 타입만 지원합니다.");
    }

    public static class UnauthorizedException extends RuntimeException {
        public UnauthorizedException(String message) {
            super(message);
        }
    }
}
