package com.phorest.spikes.apiserver.kms;

import org.springframework.cloud.bootstrap.config.PropertySourceLocator;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;

import java.util.Collections;

@Configuration
public class KmsPropertySourceLocator implements PropertySourceLocator {

  @Override
  public PropertySource<?> locate(Environment environment) {
    //todo retrieve from KMS
     return new MapPropertySource("secretKey", Collections.<String, Object>singletonMap("auth.secret-key", "Sup3rS3cr37"));
  }
}
