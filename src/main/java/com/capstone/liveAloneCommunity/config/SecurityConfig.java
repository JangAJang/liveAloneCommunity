package com.capstone.liveAloneCommunity.config;

import com.capstone.liveAloneCommunity.config.jwt.JwtAccessDenialHandler;
import com.capstone.liveAloneCommunity.config.jwt.JwtAuthenticationEntryPoint;
import com.capstone.liveAloneCommunity.config.jwt.JwtSecurityConfig;
import com.capstone.liveAloneCommunity.config.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsUtils;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig{
    private final TokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDenialHandler jwtAccessDenialHandler;
    private final CorsConfig config;
    private static final String[] PERMIT_URL_ARRAY = {
            "/v2/api-docs",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/v3/api-docs**",
            "/swagger-ui**", "/error"
    };

    @Bean
    public PasswordEncoder encode(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.httpBasic(withDefaults())
                .authorizeHttpRequests().requestMatchers(CorsUtils::isPreFlightRequest).permitAll();

        http.addFilter(config.corsFilter())
                .csrf().disable()
                .authorizeHttpRequests(request -> request
                        .requestMatchers(PERMIT_URL_ARRAY).permitAll()
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/email/**").permitAll()
                        .requestMatchers("/api/auth/reissue").hasAnyAuthority("USER", "MANAGER", "ADMIN")
                        .requestMatchers("/api/members/**").hasAnyAuthority("USER", "MANAGER", "ADMIN")
                        .requestMatchers("/api/posts/**").hasAnyAuthority("USER", "MANAGER", "ADMIN")
                        .anyRequest().authenticated())
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .formLogin().disable()
                .httpBasic().disable();
        http.exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDenialHandler)
                .and()
                .apply(new JwtSecurityConfig(tokenProvider));
        return http.build();
    }
}
