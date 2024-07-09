package com.azilzor.chatterchain.service

import com.azilzor.chatterchain.security.OAuth2UserInfoExtractor
import lombok.RequiredArgsConstructor
import org.springframework.security.authentication.InternalAuthenticationServiceException
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.OAuth2AuthenticationException
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service

@Service
@RequiredArgsConstructor
class OAuth2Service(
    private val oauth2InfoExtractors: List<OAuth2UserInfoExtractor>
) : DefaultOAuth2UserService() {

    @Throws(OAuth2AuthenticationException::class)
    override fun loadUser(userRequest: OAuth2UserRequest): OAuth2User {
        val oAuth2User = super.loadUser(userRequest)
        val optionalExtractor = oauth2InfoExtractors
            .stream()
            .filter { extractor: OAuth2UserInfoExtractor -> extractor.accepts(userRequest) }
            .findFirst()
            .orElseThrow {
                InternalAuthenticationServiceException(
                    "No OAuth2UserInfoExtractor found for: " + userRequest.clientRegistration.registrationId
                )
            }

        return optionalExtractor.extractUserInfo(oAuth2User)
    }
}
