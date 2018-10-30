package com.renga.aws.service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.renga.aws.properties.AwsProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class AmazonClient {

    private final AwsProperties awsProperties;
    private AmazonS3 amazonS3Client;

    @Autowired
    public AmazonClient(AwsProperties awsProperties) {

        this.awsProperties = awsProperties;
    }


    @PostConstruct
    private void initalizingAmazon() {
        AWSCredentials awsCredentials = new BasicAWSCredentials(awsProperties.getAccessKey(), awsProperties.getSecretKey());
        this.amazonS3Client = AmazonS3ClientBuilder.standard()
                .withRegion("us-east-2")
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();
    }

    public AmazonS3 getAmazonS3Client(){
        return this.amazonS3Client;
    }

}
