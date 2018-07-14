package com.example.service;

import org.springframework.stereotype.Service;

public interface AwsS3Service {

	public void uploadFile(String keyName, String uploadFilePath);
	
}