package dev.devdreamer.ecommerce.basketservice.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(ClientConfiguration.ClientProperties.class)
public class ClientConfiguration {


    @Data
    @ConfigurationProperties(prefix = "basket.client")
    public static class ClientProperties {

        private String platzi;
    }
}