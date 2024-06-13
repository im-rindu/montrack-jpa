package com.example.montrack_jpa.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

  @Value("${app.jwt-secret}")
  private String jwtSecret;

  @Value("${app-jwt-expiration-milliseconds}")
  private final long jwtExpirationDate;

  public JwtTokenProvider(JwtProperties jwtProperties) {
    this.jwtExpirationDate = jwtProperties.getExpirationMilliseconds();
  }

  public String generateToken(Authentication authentication){
    String username = authentication.getName();
    Date currentDate = new Date();
    Date expireDate = new Date(currentDate.getTime() + jwtExpirationDate);
    String token = Jwts.builder()
                       .subject(username)
                       .issuedAt(new Date())
                       .expiration(expireDate)
                       .signWith(key())
                       .compact();
    return token;
  }

  private Key key(){
    return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
  }

  public String getUsername(String token){
    return Jwts.parser()
               .verifyWith((SecretKey) key())
               .build()
               .parseSignedClaims(token)
               .getPayload()
               .getSubject();
  }

  public boolean validateToken(String token){
    Jwts.parser()
        .verifyWith((SecretKey) key())
        .build()
        .parse(token);
    return true;
  }

  public UsernamePasswordAuthenticationToken getAuthentication(String token, Authentication existingAuth, UserDetails userDetails) {
    return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
  }
}