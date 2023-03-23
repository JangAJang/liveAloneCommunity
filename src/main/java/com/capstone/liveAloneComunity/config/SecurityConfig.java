package com.capstone.liveAloneComunity.config;

import com.capstone.liveAloneComunity.config.jwt.JwtAccessDenialHandler;
import com.capstone.liveAloneComunity.config.jwt.JwtAuthenticationEntryPoint;
import com.capstone.liveAloneComunity.config.jwt.JwtSecurityConfig;
import com.capstone.liveAloneComunity.config.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
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

//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer() {
//        return (web) -> web.ignoring().requestMatchers();
//    }

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
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .formLogin().disable()
                .httpBasic().disable();
        http.exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDenialHandler)
                .and()
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/api/auth/logIn", "/api/auth/join").permitAll()
                        .requestMatchers("/api/auth/reissue").hasAnyAuthority("USER", "MANAGER", "ADMIN")
                        .requestMatchers("/api/members/**").hasAnyAuthority("USER", "MANAGER", "ADMIN")
                        .requestMatchers("/api/posts/**").hasAnyAuthority("USER", "MANAGER", "ADMIN"))
                .apply(new JwtSecurityConfig(tokenProvider));
        return http.build();
    }
}
