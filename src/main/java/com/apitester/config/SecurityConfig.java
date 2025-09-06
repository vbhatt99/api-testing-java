package com.apitester.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Security Configuration
 * Configures security settings for the API testing application
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Password encoder bean for hashing passwords
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Security filter chain configuration
     * Disables CSRF for API testing purposes and allows all requests
     * In production, this should be properly configured with authentication
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/api/**").permitAll()
                .requestMatchers("/actuator/health").permitAll()
                .requestMatchers("/swagger-ui/**").permitAll()
                .requestMatchers("/api-docs/**").permitAll()
                .requestMatchers("/h2-console/**").permitAll()
                .anyRequest().authenticated()
            )
            .headers(headers -> headers
                .frameOptions(frameOptions -> frameOptions.sameOrigin()) // Allow H2 console frames
                .contentTypeOptions(contentTypeOptions -> {})
                .httpStrictTransportSecurity(hstsConfig -> hstsConfig
                    .maxAgeInSeconds(31536000)
                )
            )
            .addFilterBefore((request, response, chain) -> {
                HttpServletResponse httpResponse = (HttpServletResponse) response;
                httpResponse.setHeader("X-Content-Type-Options", "nosniff");
                httpResponse.setHeader("X-Frame-Options", "DENY");
                httpResponse.setHeader("X-XSS-Protection", "1; mode=block");
                httpResponse.setHeader("Referrer-Policy", "strict-origin-when-cross-origin");
                chain.doFilter(request, response);
            }, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }
}
