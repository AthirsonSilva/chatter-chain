package com.azilzor.chatterchain.security.implementation;

import java.util.Collections;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.azilzor.chatterchain.security.CustomUserDetails;
import com.azilzor.chatterchain.security.OAuth2UserInfoExtractor;
import com.azilzor.chatterchain.security.WebSecurityConfig;

@Service
public class GoogleOAuth2UserInfoExtractor implements OAuth2UserInfoExtractor {

  @Override
  public Boolean accepts(OAuth2UserRequest userRequest) {
    return String.valueOf("google")
        .equals(userRequest.getClientRegistration().getRegistrationId());
  }

  @Override
  public CustomUserDetails extractUserInfo(OAuth2User oAuth2User) {
    CustomUserDetails user = new CustomUserDetails();
    user.setUsername(retrieveAttribute("given_name", oAuth2User));
    user.setName(retrieveAttribute("name", oAuth2User));
    user.setAuthorities(oAuth2User.getAuthorities());
    user.setAttributes(oAuth2User.getAttributes());
    user.setAuthorities(Collections.singletonList(new SimpleGrantedAuthority(WebSecurityConfig.CHAT_USER)));
    return user;
  }

}
