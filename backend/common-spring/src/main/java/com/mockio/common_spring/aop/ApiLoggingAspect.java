package com.mockio.common_spring.aop;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.MDC;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;

@Slf4j
@Component
@Aspect
@RequiredArgsConstructor
@ConditionalOnProperty(name = "aop.logging.enabled", havingValue = "true", matchIfMissing = true)
public class ApiLoggingAspect {

    private static final String TRACE_ID_KEY = "traceId";

    // Controller 기반으로 안정적으로 매칭
    @Pointcut("@within(org.springframework.web.bind.annotation.RestController) || @within(org.springframework.stereotype.Controller)")
    private void controllerLayer() {}

    @Around("controllerLayer()")
    public Object logApi(ProceedingJoinPoint pjp) throws Throwable {
        long startMs = System.currentTimeMillis();

        String traceId = Optional.ofNullable(MDC.get(TRACE_ID_KEY)).orElse("N/A");

        LogContext ctx = extractContextSafely(pjp);

        log.info("API_START traceId=[{}] http=[{}] uri=[{}] handler=[{}] args=[{}]",
                traceId, ctx.httpMethod, ctx.uri, ctx.classMethod, summarizeArgs(pjp.getArgs()));

        try {
            Object result = pjp.proceed();

            log.info("API_OK traceId=[{}] http=[{}] uri=[{}] handler=[{}] elapsedMs=[{}] result=[{}]",
                    traceId, ctx.httpMethod, ctx.uri, ctx.classMethod,
                    System.currentTimeMillis() - startMs,
                    summarizeResult(result));

            return result;
        } catch (Exception e) {
            log.warn("API_FAIL traceId=[{}] http=[{}] uri=[{}] handler=[{}] elapsedMs=[{}] ex=[{}:{}]",
                    traceId, ctx.httpMethod, ctx.uri, ctx.classMethod,
                    System.currentTimeMillis() - startMs,
                    e.getClass().getSimpleName(), safeMessage(e));
            throw e;
        }

    }

    private LogContext extractContextSafely(ProceedingJoinPoint pjp) {
        MethodSignature sig = (MethodSignature) pjp.getSignature();
        String classMethod = sig.getDeclaringType().getSimpleName() + "." + sig.getMethod().getName();

        HttpServletRequest req = currentRequest().orElse(null);
        if (req == null) {
            return new LogContext("N/A", "N/A", classMethod);
        }

        return new LogContext(req.getMethod(), req.getRequestURI(), classMethod);
    }

    private Optional<HttpServletRequest> currentRequest() {
        try {
            ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            return Optional.ofNullable(attrs).map(ServletRequestAttributes::getRequest);
        } catch (Exception ignored) {
            return Optional.empty();
        }
    }

    /**
     * args 요약: 민감/대용량 타입 제외
     */
    private String summarizeArgs(Object[] args) {
        if (args == null || args.length == 0) return "[]";

        StringBuilder sb = new StringBuilder("[");
        int count = 0;

        for (Object arg : args) {
            if (arg == null) continue;
            if (isIgnoredArg(arg)) continue;

            if (count > 0) sb.append(", ");
            sb.append(arg.getClass().getSimpleName()).append("=").append(safeToString(arg));

            count++;
            if (count >= 3) { // 너무 길어지지 않게 제한
                sb.append(", ...");
                break;
            }
        }

        sb.append("]");
        return sb.toString();
    }

    private boolean isIgnoredArg(Object arg) {
        String name = arg.getClass().getName();
        // request/response, 파일 등 대용량/민감 가능성이 큰 것 제외
        return name.startsWith("jakarta.servlet.")
                || name.startsWith("org.springframework.web.multipart.")
                || name.startsWith("org.springframework.validation.");
    }

    private String summarizeResult(Object result) {
        if (result == null) return "null";
        // 응답 전체를 찍으면 대용량/민감정보 위험 → 타입/간단 요약만
        return result.getClass().getSimpleName();
    }

    private String safeToString(Object obj) {
        try {
            String s = String.valueOf(obj);
            // 길이 제한
            return (s.length() > 300) ? s.substring(0, 300) + "...(truncated)" : s;
        } catch (Exception e) {
            return obj.getClass().getSimpleName();
        }
    }

    private String safeMessage(Exception e) {
        String msg = e.getMessage();
        if (msg == null) return "";
        return (msg.length() > 300) ? msg.substring(0, 300) + "...(truncated)" : msg;
    }

    record LogContext(String httpMethod, String uri, String classMethod) {}
}
