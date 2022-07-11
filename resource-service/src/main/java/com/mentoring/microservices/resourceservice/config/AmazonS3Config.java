package com.mentoring.microservices.resourceservice.config;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.net.URI;

@Configuration
public class AmazonS3Config {
    @Value("${config.aws.s3.url}")
    private String s3EndpointUrl;

    @Bean
    @SneakyThrows
    S3Client s3Client() {
        return S3Client.builder()
                .endpointOverride(new URI(s3EndpointUrl))
                .region(Region.US_EAST_1)
                .build();
    }
}
