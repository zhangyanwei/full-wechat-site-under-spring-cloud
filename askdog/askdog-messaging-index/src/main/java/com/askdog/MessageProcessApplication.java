package com.askdog;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

import static org.springframework.boot.SpringApplication.run;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class MessageProcessApplication {
    public static void main(String[] args) {
        run(MessageProcessApplication.class, args);
    }
}
