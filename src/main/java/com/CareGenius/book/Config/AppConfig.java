package com.CareGenius.book.Config;

import com.CareGenius.book.Dto.AIPromptResponse;
import com.CareGenius.book.Repository.UserRepository;
import com.cloudinary.Cloudinary;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Configuration
@RequiredArgsConstructor

public class AppConfig {

    private final UserRepository userRepository;

    @Bean
    public UserDetailsService userDetailsService(){
        return email -> userRepository.findByEmail(email).orElseThrow(()-> new IllegalArgumentException("Can not find email"));
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        var daoProvider = new DaoAuthenticationProvider(userDetailsService());
        daoProvider.setPasswordEncoder(passwordEncoder());
        return daoProvider;
    }

    @Bean
    public AuthenticationManager manager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Value("${cloud.name}")
    private String cloudName;

    @Value("${api.key}")
    private String apiKey;

    @Value("${api.secret}")
    private String apiSecret;

    @Bean
    public Cloudinary cloudinary(){
        Map<String, Object> cloudConfig = new HashMap<>();
        cloudConfig.put("cloud_name", cloudName);
        cloudConfig.put("api_key", apiKey);
        cloudConfig.put("api_secret", apiSecret);
        cloudConfig.put("secure", true);
        return new Cloudinary(cloudConfig);
    }

    @Value("${openai.api.key}")
    private String chatBotApiKey;

    @Value("${openai.api.url}")
    private String apiUrl;

    @Value("${openai.model}")
    private String model;

    private final RestTemplate restTemplate = new RestTemplate();

    public String getAiMatchingResult(String prompt) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(chatBotApiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", model);
        requestBody.put("messages", List.of(Map.of("role", "user", "content", prompt)));

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<AIPromptResponse> response = restTemplate.exchange(
                apiUrl,
                HttpMethod.POST,
                request,
                AIPromptResponse.class
        );

        return response.getBody().getChoices().get(0).getMessage().getContent();
    }


}
