package com.azilzor.chatterchain.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.azilzor.chatterchain.security.OAuth2UserInfoExtractor;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OAuth2Service extends DefaultOAuth2UserService {

  private final List<OAuth2UserInfoExtractor> oauth2InfoExtractors;

  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    OAuth2User oAuth2User = super.loadUser(userRequest);
    Optional<OAuth2UserInfoExtractor> optionalExtractor = oauth2InfoExtractors
        .stream()
        .filter(extractor -> extractor.accepts(userRequest))
        .findFirst();

    if (oauth2InfoExtractors.isEmpty()) {
      throw new InternalAuthenticationServiceException(
          "No OAuth2UserInfoExtractor found for: " + userRequest.getClientRegistration().getRegistrationId());
    }

    return optionalExtractor.get().extractUserInfo(oAuth2User);
  }

}
