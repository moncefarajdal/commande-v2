package com.example.commandev2.ws;

import com.example.commandev2.service.facade.MinIOService;
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

//  curl -X POST -F "folder=@/path/to/your/folder" -F "bucket=my-bucket" http://localhost:8080/minio/upload/folder
//  curl -X POST -F "folder=@'./Figma'" -F "bucket=my-bucket" http://localhost:8080/minio/upload/folder

    @PostMapping("/upload/folder/{bucket}")
    public void uploadFolder(@RequestParam("folder") MultipartFile folder, @RequestParam("bucket") String bucket) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        minIOService.uploadFolder(folder, bucket);
    }

//  curl -X POST -F "directory=@./Figma" http://localhost:8080/folder/upload/my-bucket
    @PostMapping("/folder/upload/{bucket}")
    public ResponseEntity<String> uploadToMinio(@RequestParam("directory") String directory, @PathVariable String bucket) {
        File directoryToUpload = new File(directory);
        return minIOService.uploadDirectoryToMinio(directoryToUpload, bucket);
    }
}
