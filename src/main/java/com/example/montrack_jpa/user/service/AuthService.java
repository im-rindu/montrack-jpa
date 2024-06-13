package com.example.montrack_jpa.user.service;

import com.example.montrack_jpa.user.dto.LoginDto;
import com.example.montrack_jpa.user.entity.User;

public interface AuthService {
  String login(LoginDto loginDto);
  User register(User user);
}
