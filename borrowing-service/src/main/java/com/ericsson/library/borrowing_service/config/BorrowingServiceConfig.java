package com.ericsson.library.borrowing_service.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;


@Data
@Configuration
@ConfigurationProperties(prefix = "app.borrow")
class BorrowingProperties {
    private int maxBooksPerUser = 5;
    private int loanPeriodDays = 14;
}

@Configuration
class RestTemplateConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}