package com.azilzor.chatterchain.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;

import com.azilzor.chatterchain.service.OAuth2Service;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

  private final OAuth2Service oAuth2Service;

  public static final String CHAT_USER = "CHAT_USER";

  @Bean
  protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    return http
        .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
            .requestMatchers("/").hasAuthority(CHAT_USER)
            .requestMatchers("/login", "/oauth2/**", "/websocket/**").permitAll()
            .anyRequest().authenticated())
        .oauth2Login(oauth2Login -> oauth2Login
            .loginPage("/login").defaultSuccessUrl("/")
            .userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint.userService(oAuth2Service)))
        .logout(logout -> logout.logoutSuccessUrl("/").permitAll())
        .csrf(AbstractHttpConfigurer::disable)
        .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
        .build();
  }
}
