package com.CareGenius.book.Config;

import com.CareGenius.book.Service.ServiceImp.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Component
@RequiredArgsConstructor

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();

        if (path.startsWith("/user/register") || path.startsWith("/user/login") || path.startsWith("/user/forgot-password")) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwtToken = request.getHeader("Authorization");

        if (jwtToken == null || jwtToken.isEmpty() || !jwtToken.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            jwtToken = jwtToken.substring(7);

            if (jwtToken.isEmpty() || jwtToken.trim().isEmpty()) {
                filterChain.doFilter(request, response);
                return;
            }

            if (!jwtService.isTokenValid(jwtToken)) {
                filterChain.doFilter(request, response);
                return;
            }

            var user = userDetailsService.loadUserByUsername(jwtService.extractSubject(jwtToken));
            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                var authenticationToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                authenticationToken.setDetails(request);
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        } catch (Exception e) {
            System.err.println("JWT Token validation error: " + e.getMessage());
        }

        filterChain.doFilter(request, response);
    }
}
