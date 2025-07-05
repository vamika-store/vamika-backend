package com.vamikastore.vamika.auth.config;

import com.vamikastore.vamika.auth.exceptions.RESTAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableJpaRepositories
public class WebSecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JWTTokenHelper jwtTokenHelper;

    private static final String[] publicApis = {"/v3/api-docs/**", "/swagger-ui/**"};

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOrigins(List.of("http://localhost:3000")); // your frontend URL
                    config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    config.setAllowedHeaders(List.of("Authorization", "Content-Type"));
                    config.setExposedHeaders(List.of("Authorization"));
                    config.setAllowCredentials(true);
                    return config;
                }))
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests((authorize) -> authorize
                .requestMatchers("/v3/api-docs/**", "/swagger-ui/**").permitAll()
                .requestMatchers("/api/auth/login", "/api/address", "/login/*", "/api/auth/register",  "/api/auth/verify").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/products", "/api/category").permitAll()
                    .requestMatchers("/oauth2/success").permitAll()
                .anyRequest().authenticated())
                .oauth2Login((oauth2login) -> oauth2login.defaultSuccessUrl("/oauth2/success").loginPage("/oauth2/authentication/google"))
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
            .addFilterBefore(new JWTAuthenticationFilter(jwtTokenHelper, userDetailsService), UsernamePasswordAuthenticationFilter.class)
            .exceptionHandling().authenticationEntryPoint(new RESTAuthenticationEntryPoint()); // Optional: custom 401 handler
        return http.build();
    }

    /*@Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(publicApis);
    }*/

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(daoAuthenticationProvider);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
