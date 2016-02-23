package com.phorest.spikes.apiserver;

import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ApiController {

  @RequestMapping(method = RequestMethod.GET, path = "/status")
  public Map<String, String> getApiStatus() {
    Map<String, String> status = new HashMap<>();// why java why?!
    status.put("status", "OK");
    return status;
  }

  @Secured(value = {"ROLE_USER"})
  @RequestMapping(method = RequestMethod.GET, path = "/user-data")
  public String userRoleSecuredData() {
    return "only for user role";
  }

  @Secured(value = {"ROLE_ADMIN"})
  @RequestMapping(method = RequestMethod.GET, path = "/admin-data")
  public String adminRoleSecuredData() {
    return "only for admin role";
  }


}
