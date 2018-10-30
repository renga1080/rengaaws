package com.renga.aws.service.impl;

import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.renga.aws.applicationException.FileStorageException;
import com.renga.aws.properties.AwsProperties;
import com.renga.aws.service.AmazonClient;
import com.renga.aws.service.FileHandlerService;
import com.renga.aws.util.RengaAWSUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;

@Service
@Slf4j
public class FileHandlerServiceImpl implements FileHandlerService {

    private final AmazonClient amazonClient;
    private AwsProperties awsProperties;

    @Autowired
    public FileHandlerServiceImpl(AmazonClient amazonClient, AwsProperties awsProperties) {
        this.amazonClient = amazonClient;
        this.awsProperties = awsProperties;
    }

    @Override
    public String uploadFile(MultipartFile multipartFile) {
        String fileUrl;
        File file = convertMultiPartToFile(multipartFile);
        String fileName = generateUniqueFileName(multipartFile);
        fileUrl = awsProperties.getEndpointUrl() + "/" + awsProperties.getBucketName() + "/" + fileName;
        uploadFileToS3(fileName, file);
        file.delete();
        return fileUrl;
    }

    private void uploadFileToS3(String fileName, File file) {

        log.info("Start uploading file to S3");
        amazonClient.getAmazonS3Client().putObject(new PutObjectRequest(awsProperties.getBucketName(), fileName, file).withCannedAcl(CannedAccessControlList.PublicRead));
        log.info("Successfully uploaded file to S3");
    }

    private File convertMultiPartToFile(MultipartFile multipartFile) {

        File file = new File(multipartFile.getOriginalFilename());
        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            fileOutputStream.write(multipartFile.getBytes());
            fileOutputStream.close();
        } catch (IOException e) {
            throw new FileStorageException("Error converting multipart file to standard file");
        }

        return file;
    }

    private String generateUniqueFileName(MultipartFile multipartFile) {

        String fileNameGenerated = multipartFile.getOriginalFilename().replace(" ", "_") + "_" + LocalDateTime.now().toString() + "_" + RengaAWSUtil.randomAlphaNumeric();
        log.info("generateUniqueFileName - fileNameGenerated: {}", fileNameGenerated);
        return fileNameGenerated;
    }

    @Override
    public void deleteFile(String fileUrl) {

        String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
        amazonClient.getAmazonS3Client().deleteObject(new DeleteObjectRequest(awsProperties.getBucketName() + "/", fileName));
        log.info("File/Object has been deleted successfully from S3 bucket: {}", awsProperties.getBucketName());
    }

    @Override
    public void downloadFile() {

    }
}
