package com.renga.aws.controller;


import com.renga.aws.endpoints.FileControllerEndpoints;
import com.renga.aws.service.FileHandlerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Slf4j
@Api(value="Renga AWS Project", description="Operations pertaining to documents management")
public class FileController implements FileControllerEndpoints {

    private final FileHandlerService fileHandlerService;

    @Autowired
    public FileController(FileHandlerService fileHandlerService){
        this.fileHandlerService = fileHandlerService;
    }

    @ApiOperation(value = "View a list of available products", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully uploaded file to server"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    }
    )
    @Override
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile multipartFile) {
        try{

            log.info("FileController - start uploading file..");
            String fileUrl = fileHandlerService.uploadFile(multipartFile);
            log.info("fileUrl: {}", fileUrl);

            if(StringUtils.isBlank(fileUrl)){
                log.info("fileUrl is empty or null.");
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }

            return new ResponseEntity<>(fileUrl, HttpStatus.OK);

        } catch (Exception e){
            log.error("Exception while uploading file to S3 e: {}", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @ApiOperation(value = "Delete a file")
    @Override
    public ResponseEntity<Void> deleteFile(String fileUrl) {

        try{

            log.info("FileController - start deleting file fileUrl {} ", fileUrl);
            fileHandlerService.deleteFile(fileUrl);
            log.info("Successfully deleted the fileUrl: {}", fileUrl);

            return ResponseEntity.status(HttpStatus.OK).build();

        } catch (Exception e){
            log.error("Exception while deleting a file in S3 e: {}", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
