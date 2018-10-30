package com.renga.aws.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileHandlerService {

    String uploadFile(MultipartFile multipartFile);
    void deleteFile(String fileUrl);
    void downloadFile();
}
