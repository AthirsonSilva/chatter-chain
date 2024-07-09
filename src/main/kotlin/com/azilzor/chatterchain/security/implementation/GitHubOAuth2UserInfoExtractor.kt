package com.azilzor.chatterchain.security.implementation

import com.azilzor.chatterchain.security.CustomUserDetails
import com.azilzor.chatterchain.security.OAuth2UserInfoExtractor
import com.azilzor.chatterchain.security.WebSecurityConfig
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service

@Service
class GitHubOAuth2UserInfoExtractor : OAuth2UserInfoExtractor {
    override fun accepts(userRequest: OAuth2UserRequest): Boolean {
        return "github" == userRequest.clientRegistration.registrationId
    }

    override fun extractUserInfo(oAuth2User: OAuth2User): CustomUserDetails {
        return CustomUserDetails(
            username = retrieveAttribute("login", oAuth2User),
            name = retrieveAttribute("name", oAuth2User),
            authorities = listOf(SimpleGrantedAuthority(WebSecurityConfig.CHAT_USER)),
            attributes = oAuth2User.attributes
        )
    }
}
