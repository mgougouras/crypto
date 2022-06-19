package com.example.crypto.infrastructure;

import com.example.crypto.infrastructure.json.JsonConfiguration;
import com.example.crypto.infrastructure.persistence.CryptoDocumentRepository;
import com.example.crypto.infrastructure.persistence.CryptoDocumentRepositoryImpl;
import com.example.crypto.infrastructure.util.FileCryptoReader;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({JacksonAutoConfiguration.class, JsonConfiguration.class})
public class InfrastructureConfiguration {

    @Bean
    public FileCryptoReader fileCryptoReader() {
        return new FileCryptoReader();
    }

    @Bean
    public CryptoDocumentRepository cryptoDocumentRepository(FileCryptoReader fileCryptoReader) {
        return new CryptoDocumentRepositoryImpl(fileCryptoReader);
    }
}
