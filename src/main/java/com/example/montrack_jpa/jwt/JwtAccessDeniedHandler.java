package com.example.montrack_jpa.jwt;

import java.io.IOException;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler{
  @Override
  public void handle(HttpServletRequest request, 
                     HttpServletResponse response, 
                     AccessDeniedException authException) throws IOException {
    response.setContentType("application/json");
    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
    response.getOutputStream().println(
    "{ \"status\": 403, \"statusMessage\": \"Forbidden\", \"message\": \"Access Denied, You don't have access\" }"
    );
  }
}