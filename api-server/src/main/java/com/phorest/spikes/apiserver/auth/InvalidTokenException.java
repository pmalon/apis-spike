package com.phorest.spikes.apiserver.auth;

import org.springframework.security.core.AuthenticationException;

public class InvalidTokenException extends AuthenticationException {

  public InvalidTokenException(String msg, Throwable t) {
    super(msg, t);
  }
}
