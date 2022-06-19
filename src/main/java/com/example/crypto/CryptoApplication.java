package com.example.crypto;

import com.example.crypto.application.ApplicationConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Import;
import org.springframework.web.reactive.config.EnableWebFlux;

@EnableWebFlux
@EnableDiscoveryClient
@SpringBootApplication
@Import(ApplicationConfiguration.class)
public class CryptoApplication {

    /**
     * The main entry point of the application.
     *
     * @param args application arguments
     */
    public static void main(String[] args) {
        long millis = System.currentTimeMillis();
        SpringApplication springApplication = new SpringApplication(CryptoApplication.class);
        springApplication.setWebApplicationType(WebApplicationType.REACTIVE);
        springApplication.run(args);
        System.out.println("Crypto App -- Started in " + (System.currentTimeMillis() - millis) + "(ms)");
    }
}
