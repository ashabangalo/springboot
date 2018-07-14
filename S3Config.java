package com.example.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
 
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
 
@Configuration
public class S3Config {
	@Value("${amazon.s3.accessKey}")
	private String awsId;
 
	@Value("${amazon.s3.secretKey}")
	private String awsKey;
	
	@Value("${amazon.s3.region}")
	private String region;
 
	@Bean
	public AmazonS3 s3client() {
		
		System.out.println("accesskey="+awsId);
		System.out.println("awskey="+awsKey);
		System.out.println("region="+region);
		
		BasicAWSCredentials awsCreds = new BasicAWSCredentials(awsId, awsKey);
		AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
								.withRegion(Regions.fromName(region))
		                        .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
		                        .build();
		
		return s3Client;
	}
}