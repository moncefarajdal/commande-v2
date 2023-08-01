package com.example.commandev2.service.facade;

import io.minio.errors.MinioException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface MinIOService {

    int bucketExists(String bucket);

    int upload(MultipartFile file, String bucket);

    int saveBucket(String bucket);

    List<String> findAllDocuments(String bucket);

    byte[] downloadAllDocumentsAsZip(String bucket);

    byte[] downloadDocumentsAsZip(String bucket, List<String> filenames);

    void uploadDirectory(String directoryPath, String bucket) throws IOException, NoSuchAlgorithmException, InvalidKeyException, MinioException;
}
