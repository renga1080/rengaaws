package com.renga.aws.endpoints;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

public interface FileControllerEndpoints {

    @PostMapping("/api/v1/file/upload")
    @ResponseBody
    ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile multipartFile);

    @DeleteMapping("/api/v1/file/delete")
    @ResponseBody
    ResponseEntity<Void> deleteFile(@RequestParam("fileUrl") String fileUrl);

}
