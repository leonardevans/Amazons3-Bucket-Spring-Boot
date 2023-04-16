package com.bedubyte.amazons3bucketspringboot.controller;

import com.bedubyte.amazons3bucketspringboot.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class S3Controller {
    @Autowired
    FileService fileService;

    @PostMapping("upload")
    public String upload(@RequestParam("file")MultipartFile multipartFile){
        return fileService.saveFile(multipartFile);
    }

    @GetMapping("download/{filename}")
    public ResponseEntity<byte[]> download(@PathVariable("filename") String fileName){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-type", MediaType.ALL_VALUE);
        httpHeaders.add("Content-Disposition", "attachment; filename=" + fileName);
        byte[] bytes = fileService.downloadFile(fileName);
        return ResponseEntity.status(HttpStatus.OK).headers(httpHeaders).body(bytes);
    }

    @DeleteMapping("{filename}")
    public String deleteFile(@PathVariable("filename") String filename){
        return fileService.deleteFile(filename);
    }

    @GetMapping("list")
    public List<String> getAllFiles(){
        return fileService.listAllFiles();
    }
}
