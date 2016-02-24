package com.phorest.spikes.apiserver.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("/auth")
public class AuthController {

  @Autowired
  private AuthenticationService authenticationService;

  @RequestMapping(method = POST)
  @ResponseStatus(HttpStatus.CREATED)
  Token authenticate(@RequestBody @Valid AuthenticationRequest authenticationRequest) {
    return authenticationService.authenticate(authenticationRequest);
  }

  @RequestMapping(method = GET, path = "/user")
  public User user(UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) {
    return new User(usernamePasswordAuthenticationToken.getName(), usernamePasswordAuthenticationToken.getAuthorities());
  }

}
