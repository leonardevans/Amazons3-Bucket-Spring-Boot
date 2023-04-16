package com.bedubyte.amazons3bucketspringboot.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FileServiceImpl implements FileService{
    private final AmazonS3 amazonS3;

    @Value("${s3BucketName}")
    private String s3BucketName;

    public FileServiceImpl(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    @Override
    public String saveFile(MultipartFile multipartFile) {
        String originalFileName = multipartFile.getOriginalFilename();
        try {
            File convertedFile = convertMultipartToFile(multipartFile);
            PutObjectResult putObjectResult = amazonS3.putObject(s3BucketName, originalFileName, convertedFile);
            return putObjectResult.getContentMd5();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public byte[] downloadFile(String filename) {
        S3Object s3Object = amazonS3.getObject(s3BucketName, filename);
        S3ObjectInputStream s3ObjectInputStream = s3Object.getObjectContent();
        try {
            return IOUtils.toByteArray(s3ObjectInputStream);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public String deleteFile(String filename) {
        amazonS3.deleteObject(s3BucketName, filename);
        return "File deleted";
    }

    @Override
    public List<String> listAllFiles() {
        ListObjectsV2Result listObjectsV2Result = amazonS3.listObjectsV2(s3BucketName);
        return listObjectsV2Result.getObjectSummaries().stream().map(S3ObjectSummary::getKey).collect(Collectors.toList());
    }

    private File convertMultipartToFile(MultipartFile multipartFile) throws IOException{
        File convertedFile = new File(multipartFile.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convertedFile);
        fos.write(multipartFile.getBytes());
        fos.close();
        return convertedFile;
    }
}
