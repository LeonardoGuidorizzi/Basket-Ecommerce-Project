package dev.devdreamer.ecommerce.basketservice.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
@Configuration
@RequiredArgsConstructor
public class WebClientConfiguraton {
    private final ClientConfiguration.ClientProperties properties;

    @Bean
    public WebClient platziWebClient() {
        return WebClient.builder()
                .baseUrl(properties.getPlatzi())
                .build();
    }
}
