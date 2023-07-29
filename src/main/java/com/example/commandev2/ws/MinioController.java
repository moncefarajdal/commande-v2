package com.example.commandev2.ws;

import com.example.commandev2.service.facade.MinIOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

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

}
