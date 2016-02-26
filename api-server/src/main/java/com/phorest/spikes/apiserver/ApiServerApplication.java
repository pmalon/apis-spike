package com.phorest.spikes.apiserver;


import com.phorest.spikes.apiserver.kms.KmsPropertySourceLocator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication(exclude = {KmsPropertySourceLocator.class})
@EnableSwagger2
public class ApiServerApplication {

  public static void main(String[] args) {
    SpringApplication.run(ApiServerApplication.class, args);
  }

}
