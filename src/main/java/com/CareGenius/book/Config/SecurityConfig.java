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
                        .requestMatchers("/user/register",
                                "/user/login",
                                "/caregivers/getAll",
                                "/caregivers/getByUid/**",
                                "/user/forgot-password/**",
                                "/test-momo",
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/swagger-resources/**"
                        ).permitAll()

                        .requestMatchers("/ai-recommend/match",
                                "/booking/create")
                        .hasRole("SEEKER")

                        .requestMatchers("/care-seekers/create")
                        .hasAnyRole("USER", "SEEKER")

                        .requestMatchers("/booking/decide",
                                "/notification/getAll",
                                "/booking/getAll",
                                "/booking/getById/**",
                                "/tasks/getById/**",
                                "/tasks/getAllByBooking"
                        )
                        .hasAnyRole("SEEKER", "GIVER")

                        .requestMatchers("/tasks/create",
                                "/tasks/update/**",
                                "/tasks/delete/**"
                                )
                        .hasRole("GIVER")

                        .requestMatchers("/caregivers/create",
                                "/caregivers/linkImage",
                                "/caregivers/delete/**",
                                "/user/delete/**",
                                "/tasks/getAll"
                        )
                        .hasRole("ADMIN")


                        .anyRequest().authenticated()
                );

        return http.build();
    }
}