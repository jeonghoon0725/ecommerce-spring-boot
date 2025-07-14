package com.home.java_02.global.config;

import static org.springframework.security.config.Customizer.withDefaults;

import com.home.java_02.global.filter.AuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  private static final String[] SECURITY_EXCLUDE_PATHS = {
      "/public/**", "/api/swagger-ui/**", "/swagger-ui/**", "/swagger-ui.html",
      "/api/v3/api-docs/**", "/v3/api-docs/**", "/favicon.ico", "/actuator/**",
      "/swagger-resources/**", "/external/**", "/api/auth/**", "/api/users/availability",
      "/api/users",
  };

  private final AuthenticationFilter authenticationFilter;

  public SecurityConfig(AuthenticationFilter authenticationFilter) {
    this.authenticationFilter = authenticationFilter;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .cors(withDefaults())
        .csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(authorize -> authorize
            .requestMatchers(SECURITY_EXCLUDE_PATHS).permitAll()
            .requestMatchers("/api/user").hasRole("USER")  // ROLE_ 접두사 자동 추가
            .requestMatchers("/api/admin").hasRole("ADMIN")  // ROLE_ 접두사 자동 추가
            .anyRequest().authenticated()
        )
        .sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)  //기존 세션을 사용
            .maximumSessions(1)  //계정당이 아닌 클라이언트당
            .maxSessionsPreventsLogin(false)  //계정당 최대 세션 개수 - true의 경우 기존 세션 삭제, 신규 세션 사용
        )
        .addFilterBefore(authenticationFilter,
            UsernamePasswordAuthenticationFilter.class);  //앞에 spring filter interceptor 보다 먼저 실행되는 filter

    return http.build();
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
      throws Exception {
    return config.getAuthenticationManager();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}

