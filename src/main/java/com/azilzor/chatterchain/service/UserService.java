package com.azilzor.chatterchain.service;

import java.util.LinkedHashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.azilzor.chatterchain.dto.UserDto;

@Service
public class UserService {

  private final Set<UserDto> onlineUsers = new LinkedHashSet<>();

  public Set<UserDto> getOnlineUsers() {
    return onlineUsers;
  }

  public void addUser(UserDto user) {
    onlineUsers.add(user);
  }

  public void removeUser(UserDto user) {
    onlineUsers.remove(user);
  }

}
