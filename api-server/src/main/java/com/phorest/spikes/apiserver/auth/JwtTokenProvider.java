package com.phorest.spikes.apiserver.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Date;

@Component
public class JwtTokenProvider {

  private static final String SECRET_KEY = "Sup3rS3cr37";

  private static void handleInvalidToken(JwtException e) {
    throw new InvalidTokenException("Invalid token", e);
  }

  public String getUserNameFromToken(String authToken) {
    return getTokenClaims(authToken).getSubject();
  }

  public void validateAuthToken(String authToken, UserDetails userDetails) {
    try {
      Jwts.parser()
        .setSigningKey(SECRET_KEY)
        .requireSubject(userDetails.getUsername())
        .parseClaimsJws(authToken);
    } catch (JwtException e) {
      handleInvalidToken(e);
    }
  }

  public Token createToken(UserDetails userDetails) {
    String tokenValue = Jwts.builder()
      .setExpiration(getTokenExpiration(10))
      .setSubject(userDetails.getUsername())
      .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
      .compact();
    return new Token(tokenValue);
  }

  private Date getTokenExpiration(long tokenValidityInMinutes) {
    return Date.from(ZonedDateTime.now().plusMinutes(tokenValidityInMinutes).toInstant());
  }

  private Claims getTokenClaims(String token) {
    try {
      return Jwts.parser()
        .setSigningKey(SECRET_KEY)
        .parseClaimsJws(token)
        .getBody();
    } catch (JwtException e) {
      handleInvalidToken(e);
      return null;
    }
  }

}
