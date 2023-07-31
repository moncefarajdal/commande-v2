package com.example.commandev2.ws;

import com.example.commandev2.service.facade.MinIOService;
import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

//.\minio.exe server C:\minio --console-address :9090


@RestController
@RequestMapping("/minio")
public class MinioController {

    @Autowired
    private MinIOService minIOService;

    @GetMapping("/bucket/{name}")
    public int bucketExists(@PathVariable String name) {
        return minIOService.bucketExists(name);
    }

    @PostMapping("/upload/file/{bucket}")
    public int upload(@RequestParam("file") MultipartFile file, @PathVariable String bucket) {
        return minIOService.upload(file, bucket);
    }

    @PostMapping("/bucket")
    public int saveBucket(@RequestParam("bucketName") String bucket) {
        return minIOService.saveBucket(bucket);
    }

    @GetMapping("/findAll/bucket/{bucket}")
    public List<String> findAllDocuments(@PathVariable String bucket) {
        return minIOService.findAllDocuments(bucket);
    }

    @GetMapping("/downloadAll/bucket/{bucket}")
    public byte[] downloadAllDocumentsAsZip(@PathVariable String bucket) {
        return minIOService.downloadAllDocumentsAsZip(bucket);
    }


    //  curl -o D:/GED/documents.zip http://localhost:8080/minio/download/files/bucket/bucket03/?files=file01.pdf,culture.pptx

    @GetMapping("download/files/bucket/{bucket}")
    public byte[] downloadDocumentsAsZip(@PathVariable String bucket, @RequestParam("files") List<String> filenames) {
        return minIOService.downloadDocumentsAsZip(bucket, filenames);
    }

    //  curl -X POST -F "path=@D:/GED/Figma" http://localhost:8080/upload/folder/bucket/bucket03
    //  curl -X POST --data-binary "@D:/GED/Figma" http://localhost:8080/upload/folder/bucket/bucket03
    @PostMapping("upload/folder/bucket/{bucket}")
    public void uploadDirectoryToBucket(@PathVariable String bucket, @RequestParam String directoryPath) throws IOException {
        minIOService.uploadDirectoryToBucket(bucket, directoryPath);
    }
}
