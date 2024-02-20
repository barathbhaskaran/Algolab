package com.enpm613.algolab.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.regions.providers.AwsRegionProviderChain;
import software.amazon.awssdk.regions.providers.DefaultAwsRegionProviderChain;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;
import java.nio.file.Paths;

@Service
@PropertySource("classpath:application.properties")
public class S3Service {
    private S3Client s3Client;

    @Value("${aws.bucketName}")
    String bucketName;

    @Value("${aws.accessKeyId}")
    String accessKeyId;

    @Value("${aws.secretKey}")
    String secretKey;

    @Value("${aws.region}")
    String region;

    public S3Service(){

//        System.out.println("regions : "+this.secretKey);


    }

    public void uploadFileToS3( String key, File file) {

        if( s3Client==null ){
            this.s3Client = S3Client.builder()
                    .region(Region.of(region))
                    .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKeyId, secretKey)))
                    .build();
        }

        String path = "course-images/"+key;

        s3Client.putObject(PutObjectRequest.builder()
                .bucket(bucketName)
                .key(path)
                .build(), file.toPath());
    }

}
