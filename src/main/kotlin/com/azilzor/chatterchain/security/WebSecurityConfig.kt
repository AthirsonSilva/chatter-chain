package com.azilzor.chatterchain.security

import com.azilzor.chatterchain.service.OAuth2Service
import lombok.RequiredArgsConstructor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer.AuthorizationManagerRequestMatcherRegistry
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer
import org.springframework.security.config.annotation.web.configurers.oauth2.client.OAuth2LoginConfigurer
import org.springframework.security.config.annotation.web.configurers.oauth2.client.OAuth2LoginConfigurer.UserInfoEndpointConfig
import org.springframework.security.config.annotation.web.headers.FrameOptionsDsl
import org.springframework.security.config.web.server.ServerHttpSecurity.HeaderSpec.FrameOptionsSpec
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
class WebSecurityConfig(
    private val oAuth2Service: OAuth2Service
) {

    @Bean
    @Throws(Exception::class)
    protected fun filterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            .authorizeHttpRequests { authorizeHttpRequests ->
                authorizeHttpRequests
                    .requestMatchers("/").hasAuthority(CHAT_USER)
                    .requestMatchers("/login", "/oauth2/**", "/websocket/**").permitAll()
                    .anyRequest().authenticated()
            }
            .oauth2Login { oauth2Login: OAuth2LoginConfigurer<HttpSecurity?> ->
                oauth2Login
                    .loginPage("/login").defaultSuccessUrl("/")
                    .userInfoEndpoint { userInfoEndpoint ->
                        userInfoEndpoint.userService(
                            oAuth2Service
                        )
                    }
            }
            .logout { logout: LogoutConfigurer<HttpSecurity> -> logout.logoutSuccessUrl("/").permitAll() }
            .csrf { obj: CsrfConfigurer<HttpSecurity> -> obj.disable() }
            .headers { headers: HeadersConfigurer<HttpSecurity> ->
                headers.frameOptions { frameOptions -> frameOptions.sameOrigin() }
            }
            .build()
    }

    companion object {
        const val CHAT_USER: String = "CHAT_USER"
    }
}
