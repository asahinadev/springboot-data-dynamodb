package com.example.spring.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
@Configuration
@ConfigurationProperties(prefix = "com.example.spring.config.dynamodb")
public class DynamodbProperties {

	String endPoint;

	String region;

}
