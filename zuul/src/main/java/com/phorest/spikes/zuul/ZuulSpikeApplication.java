package com.phorest.spikes.zuul;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@SpringBootApplication
@EnableZuulProxy
public class ZuulSpikeApplication {

  public static void main(String[] args) {
    SpringApplication.run(ZuulSpikeApplication.class, args);
  }
}
