package com.example.commandev2.service.facade;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MinIOService {

    int bucketExists(String bucket);

    int upload(MultipartFile file, String bucket);

    int saveBucket(String bucket);

    List<String> findAllDocuments(String bucket);

    byte[] downloadAllDocumentsAsZip(String bucket);

}
