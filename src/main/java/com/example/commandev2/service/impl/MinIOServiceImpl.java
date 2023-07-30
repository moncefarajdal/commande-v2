package com.example.commandev2.service.impl;

import com.example.commandev2.service.facade.MinIOService;
import io.minio.*;
import io.minio.errors.*;
import io.minio.messages.Item;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
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

    // Create download files method here

    @Override
    public void uploadFolder(MultipartFile folder, String bucket) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        // Create a temporary directory to extract the folder content.
        File tempDirectory = new File(System.getProperty("java.io.tmpdir"), folder.getOriginalFilename());
        tempDirectory.mkdirs();

        try {
            // Extract the folder content to the temporary directory.
            FileUtils.copyInputStreamToFile(folder.getInputStream(), tempDirectory);

            // Upload the folder and its content to the MinIO bucket.
            uploadFilesInDirectory(tempDirectory, "", bucket);
        } finally {
            // Delete the temporary directory and its content after uploading.
            FileUtils.deleteDirectory(tempDirectory);
        }
    }

    private void uploadFilesInDirectory(File directory, String objectPrefix, String bucketName) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        for (File file : directory.listFiles()) {
            if (file.isDirectory()) {
                // Recursively upload subdirectories.
                uploadFilesInDirectory(file, objectPrefix + file.getName() + "/", bucketName);
            } else {
                // Upload individual files.
                String objectName = objectPrefix + file.getName();
                try (InputStream inputStream = Files.newInputStream(file.toPath())) {
                    minioClient.putObject(PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(inputStream, file.length(), -1)
                            .build());
                } catch (io.minio.errors.MinioException e) {
                    // Handle MinIO-specific exceptions here.
                    e.printStackTrace();
                    // Handle the exception as per your application's requirements (e.g., log it, return an error response, etc.).
                    // You can also throw a custom exception or return a specific error message if needed.
                    throw new IOException("Error uploading file to MinIO: " + e.getMessage());
                } catch (IOException | InvalidKeyException | NoSuchAlgorithmException e) {
                    // Handle other IO-related exceptions (e.g., if there's an error reading the file).
                    e.printStackTrace();
                    throw e;
                }
            }
        }
    }

    @Override
    public ResponseEntity<String> uploadDirectoryToMinio(File directory, String bucketName) {
        if (!directory.exists() || !directory.isDirectory()) {
            return ResponseEntity.badRequest().body("Invalid directory path or directory does not exist.");
        }

        try {
            uploadDirectoryContents(directory, bucketName);
            return ResponseEntity.ok("Directory uploaded successfully to MinIO.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading directory to MinIO.");
        }
    }

    private void uploadDirectoryContents(File directory, String bucketName) throws IOException,
            InvalidKeyException, NoSuchAlgorithmException {
        File[] files = directory.listFiles();
        if (files == null) {
            return;
        }

        for (File file : files) {
            if (file.isDirectory()) {
                uploadDirectoryContents(file, bucketName); // Recursively upload subdirectories.
            } else {
                uploadFileToMinio(file, bucketName); // Upload individual files.
            }
        }
    }

    private void uploadFileToMinio(File file, String bucketName) throws IOException,
            InvalidKeyException, NoSuchAlgorithmException {
        String objectName = file.getAbsolutePath().replace("\\", "/");

        try (FileInputStream fis = new FileInputStream(file)) {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(fis, fis.available(), -1)
                            .build());
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (InsufficientDataException e) {
            e.printStackTrace();
        } catch (ErrorResponseException e) {
            e.printStackTrace();
        } catch (InvalidResponseException e) {
            e.printStackTrace();
        } catch (XmlParserException e) {
            e.printStackTrace();
        } catch (InternalException e) {
            e.printStackTrace();
        }

        System.out.println("Uploaded " + objectName + " to MinIO.");
    }

    @Autowired
    private MinioClient minioClient;
}
