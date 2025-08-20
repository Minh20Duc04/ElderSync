package com.CareGenius.book.Config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> auth

                        //ko cần role
                        .requestMatchers(
                                "/caregivers/getAll",
                                "/caregivers/getByUid/**",
                                "/caregivers/searchByName**",
                                "/reviews/getByGiverId/**",
                                "/test-momo",
                                "/user/forgot-password/**",
                                "/user/login",
                                "/user/register",
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/swagger-resources/**",
                                "/v3/api-docs/**"
                        ).permitAll()

                        // role seeker
                        .requestMatchers(
                                "/ai-recommend/match",
                                "/booking/create",
                                "/notification/emergency",
                                "/reviews/create",
                                "/reviews/deleteById/**",
                                "/reviews/updateByReviewId/**"
                        ).hasRole("SEEKER")

                        // role giver
                        .requestMatchers(
                                "/tasks/create",
                                "/tasks/delete/**",
                                "/tasks/update/**"
                        ).hasRole("GIVER")

                        // role admin
                        .requestMatchers(
                                "/care-seekers/deleteById/**",
                                "/care-seekers/getAll",
                                "/caregivers/create",
                                "/caregivers/delete/**",
                                "/caregivers/linkImage",
                                "/tasks/getAll",
                                "/user/delete/**"
                        ).hasRole("ADMIN")

                        // role user, seeker
                        .requestMatchers(
                                "/care-seekers/create"
                        ).hasAnyRole("USER", "SEEKER")

                        // role seeker, giver
                        .requestMatchers(
                                "/booking/decide",
                                "/booking/getAll",
                                "/booking/getById/**",
                                "/notification/getAll",
                                "/tasks/getAllByBooking",
                                "/tasks/getById/**"
                        ).hasAnyRole("SEEKER", "GIVER")

                        // role seeker, giver, admin
                        .requestMatchers(
                                "/care-seekers/getById/**"
                        ).hasAnyRole("SEEKER", "GIVER", "ADMIN")

                        .anyRequest().authenticated()
                );

        return http.build();
    }
}