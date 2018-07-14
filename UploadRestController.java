package com.example.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.service.AwsS3Service;

@RestController
@EnableAutoConfiguration
public class UploadRestController {
	 	
	 	
	 	private Environment env;
	 	
		AwsS3Service s3Service;
	 	
	 	@Value("${amazon.s3.accessKey}")
	    private String accessKey;
	 	
	 	@Autowired
	 	public UploadRestController(AwsS3Service s3Service, Environment env ) {
	 		this.s3Service = s3Service;
	 		this.env = env;
			
		}
	 	
	 	//Upload to Amazon
	 	@PostMapping("/uploadFile")
	    public String uploadFile(@RequestPart(value = "file") MultipartFile multipartFile) {
	 		
	        if (multipartFile.isEmpty()) {
	            return "Please click back button and select a file to upload";
	        }
	        
	        String uploadFilePath = "";
	        String fileName  = "";
	        try {
	            File file = convertMultiPartToFile(multipartFile);
	            fileName = generateFileName(multipartFile);
	            uploadFilePath = file.getAbsolutePath();
	            System.out.println("filepath="+file.getAbsolutePath());
	            System.out.println("canonical path="+file.getCanonicalPath()); 
	            System.out.println("filename="+fileName);
	            
	        } catch (Exception e) {
	           e.printStackTrace();
	        }
	        
	        System.out.println("Uploaded file");
	        
	        System.out.println("---------------- START UPLOAD FILE ----------------");
			//s3Service.uploadFile(accessKey, uploadFilePath);
	        s3Service.uploadFile(fileName, uploadFilePath);
			
	        return "Upload success, please click back button to upload another file";
	    }
	 	
	 	// Upload to local folders
	 	@PostMapping("/upload") 
	    public String singleFileUpload(@RequestParam("file") MultipartFile file,
	                                   RedirectAttributes redirectAttributes) {

	 		final String UPLOADED_FOLDER = env.getProperty("upload.file.folder");
	 		System.out.println("UPLOADED_FOLDER=="+UPLOADED_FOLDER);
	 		
	        if (file.isEmpty()) {
	            //redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
	            return "Please click back button and select a file to upload";
	        }

	        try {

	            // Get the file and save it somewhere
	            byte[] bytes = file.getBytes();
	            Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
	            Files.write(path, bytes);
	           // redirectAttributes.addFlashAttribute("message", "You successfully uploaded '" + file.getOriginalFilename() + "'");

	        } catch (IOException e) {
	            e.printStackTrace();
	        }

	        return "You successfully uploaded " + file.getOriginalFilename();
	    }

	    @GetMapping("/uploadStatus")
	    public String uploadStatus() {
	        return "uploadStatus";
	    }
	    
	    private File convertMultiPartToFile(MultipartFile file) throws IOException {
	        File convFile = new File(file.getOriginalFilename());
	        FileOutputStream fos = new FileOutputStream(convFile);
	        fos.write(file.getBytes());
	        fos.close();
	        return convFile;
	    }
	    
	    private String generateFileName(MultipartFile multiPart) {
	        return multiPart.getOriginalFilename();
	    }
}
