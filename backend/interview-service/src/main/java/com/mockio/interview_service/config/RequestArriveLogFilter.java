package com.mockio.interview_service.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class RequestArriveLogFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {
        System.out.println("[INTERVIEW ARRIVED] " + req.getMethod() + " " + req.getRequestURI()
                + " auth=" + req.getHeader("Authorization"));
        chain.doFilter(req, res);
    }
}
