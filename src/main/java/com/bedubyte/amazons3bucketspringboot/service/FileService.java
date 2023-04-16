package com.bedubyte.amazons3bucketspringboot.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {
    String saveFile(MultipartFile multipartFile);
    byte[] downloadFile(String filename);
    String deleteFile(String filename);
    List<String> listAllFiles();
}
