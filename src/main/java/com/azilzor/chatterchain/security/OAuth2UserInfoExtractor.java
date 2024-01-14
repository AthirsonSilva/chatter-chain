package com.azilzor.chatterchain.security;

import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;

public interface OAuth2UserInfoExtractor {

  CustomUserDetails extractUserInfo(OAuth2User oAuth2User);

  Boolean accepts(OAuth2UserRequest userRequest);

  default String retrieveAttribute(String attribute, OAuth2User oAuth2User) {
    Object atribute = oAuth2User.getAttributes().get(attribute);
    return atribute == null ? "" : atribute.toString();
  }

}
