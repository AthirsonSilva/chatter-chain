package com.azilzor.chatterchain.security

import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.user.OAuth2User

interface OAuth2UserInfoExtractor {
    fun extractUserInfo(oAuth2User: OAuth2User): CustomUserDetails

    fun accepts(userRequest: OAuth2UserRequest): Boolean

    fun retrieveAttribute(attribute: String?, oAuth2User: OAuth2User): String {
        val attributes = oAuth2User.attributes[attribute]
        return attributes?.toString() ?: ""
    }
}
