package com.renga.aws;

import com.renga.aws.properties.AwsProperties;
import com.renga.aws.properties.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({
		FileStorageProperties.class,
		AwsProperties.class
})
public class RengaAwsApplication {

	public static void main(String[] args) {
		SpringApplication.run(RengaAwsApplication.class, args);
	}
}
