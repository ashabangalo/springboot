package com.example.service;

import java.io.File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.configuration.S3Config;

@Service
public class AwsS3ServiceImpl implements AwsS3Service {

	 
	private Logger logger = LoggerFactory.getLogger(AwsS3ServiceImpl.class);
		
		@Autowired
		private S3Config s3config;
	    
	    @Value("${amazon.s3.bucketName}")
	    private String bucketName;
	    
    @Override
	public void uploadFile(String keyName, String uploadFilePath) {
		
		try {
			
			File file = new File(uploadFilePath);
			//s3config.s3client().putObject(new PutObjectRequest(bucketName, keyName, file));
			s3config.s3client().putObject(new PutObjectRequest(bucketName, keyName, file));
	        logger.info("===================== Upload File - Done! =====================");
	        
		} catch (AmazonServiceException ase) {
			logger.info("Caught an AmazonServiceException from PUT requests, rejected reasons:");
			logger.info("Error Message:    " + ase.getMessage());
			logger.info("HTTP Status Code: " + ase.getStatusCode());
			logger.info("AWS Error Code:   " + ase.getErrorCode());
			logger.info("Error Type:       " + ase.getErrorType());
			logger.info("Request ID:       " + ase.getRequestId());
        } catch (AmazonClientException ace) {
            logger.info("Caught an AmazonClientException: ");
            logger.info("Error Message: " + ace.getMessage());
        }
	}
}
