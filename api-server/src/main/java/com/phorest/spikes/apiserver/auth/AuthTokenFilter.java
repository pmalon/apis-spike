package com.phorest.spikes.apiserver.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
public class AuthTokenFilter extends GenericFilterBean {

  @Autowired
  private UserDetailsService userDetailsService;

  @Autowired
  private JwtTokenProvider tokenProvider;

  public static String getAuthToken(HttpServletRequest httpServletRequest) {
    String authHeader = httpServletRequest.getHeader(AUTHORIZATION);
    return authHeader != null ? authHeader.replaceFirst("Bearer ", "") : null;
  }

  private static UsernamePasswordAuthenticationToken getUsernamePasswordAuthenticationToken(UserDetails userDetails) {
    return new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    try {
      HttpServletRequest httpServletRequest = (HttpServletRequest) request;
      authenticateUser(httpServletRequest);
    } catch (Exception e) {
      // ignore
    }
    chain.doFilter(request, response);
  }

  private void authenticateUser(HttpServletRequest httpServletRequest) {
    String authToken = getAuthToken(httpServletRequest);

    if (authToken != null) {
      String userName = tokenProvider.getUserNameFromToken(authToken);
      UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
      tokenProvider.validateAuthToken(authToken, userDetails);
      SecurityContextHolder.getContext().setAuthentication(getUsernamePasswordAuthenticationToken(userDetails));
    }
  }
}
