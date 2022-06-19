package com.example.crypto.application;

import com.example.crypto.infrastructure.InfrastructureConfiguration;
import com.example.crypto.infrastructure.persistence.CryptoDocumentRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({InfrastructureConfiguration.class, SwaggerConfiguration.class})
public class ApplicationConfiguration {

    @Bean
    public CryptoApplicationService cryptoApplicationService(CryptoDocumentRepository cryptoDocumentRepository) {

        return new CryptoApplicationServiceImpl(cryptoDocumentRepository);
    }
}
