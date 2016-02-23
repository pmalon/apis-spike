package com.phorest.spikes.apiserver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.kotlin.KotlinModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiServerConfiguration {

  @Autowired
  public void initObjectMapper(ObjectMapper objectMapper) {
    objectMapper.registerModule(new KotlinModule());
  }
}
