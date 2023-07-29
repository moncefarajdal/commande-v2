package com.example.commandev2.service.impl;

import com.example.commandev2.service.facade.MinIOService;
import io.minio.*;
import io.minio.messages.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class MinIOServiceImpl implements MinIOService {

    @Override
    public int bucketExists(String name) {
        try {
            boolean bucketExists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(name).build());
            return bucketExists ? 1 : 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int upload(MultipartFile file, String bucket) {

        if (bucketExists(bucket) != 1) {
            return 0;
        } else {
            try {
                minioClient.putObject(
                        PutObjectArgs.builder()
                                .bucket(bucket)
                                .object(file.getOriginalFilename())
                                .stream(file.getInputStream(), file.getSize(), -1)
                                .contentType(file.getContentType())
                                .build()
                );
                return 1;
            } catch (Exception e) {
                e.printStackTrace();
                return -1;
            }
        }
    }

    @Override
    public int saveBucket(String bucket) {
        if (bucketExists(bucket) == 1) return 0;
        else {
            try {
                minioClient.makeBucket(
                        MakeBucketArgs.builder()
                                .bucket(bucket)
                                .build()
                );
                return 1;
            } catch (Exception e) {
                e.printStackTrace();
                return 0;
            }
        }
    }

    @Override
    public List<String> findAllDocuments(String bucket) {

        List<String> documents = new ArrayList<>();

        if (bucketExists(bucket) != 1) return null;
        else {
            try {
                Iterable<Result<Item>> results = minioClient.listObjects(
                        ListObjectsArgs.builder().bucket(bucket).build()
                );

                for (Result<Item> result : results) {
                    Item item = result.get();
                    documents.add(item.objectName());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return documents;
        }
    }

    @Override
    public byte[] downloadAllDocumentsAsZip(String bucket) {
        if (bucketExists(bucket) != 1) return null;
        else {
            try {
                List<String> documentNames = findAllDocuments(bucket);

                // Create a byte array output stream to hold the zip data
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ZipOutputStream zipOut = new ZipOutputStream(baos);

                // Buffer for reading data
                byte[] buffer = new byte[8192];

                // Loop through each document and add it to the zip
                for (String documentName : documentNames) {
                    // Get the document object from MinIO
                    GetObjectResponse response = minioClient.getObject(
                            GetObjectArgs.builder()
                                    .bucket(bucket)
                                    .object(documentName)
                                    .build()
                    );

                    // Get the input stream containing the document data
                    InputStream documentStream = response;

                    // Create a new entry in the zip for the document
                    ZipEntry zipEntry = new ZipEntry(documentName);
                    zipOut.putNextEntry(zipEntry);

                    // Write the document data to the zip
                    int bytesRead;
                    while ((bytesRead = documentStream.read(buffer)) != -1) {
                        zipOut.write(buffer, 0, bytesRead);
                    }

                    // Close the entry for the document
                    zipOut.closeEntry();

                    // Close the input stream for the current document
                    documentStream.close();
                }

                // Close the zip output stream
                zipOut.close();

                // Return the zip data as a byte array
                return baos.toByteArray();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    @Autowired
    private MinioClient minioClient;
}
