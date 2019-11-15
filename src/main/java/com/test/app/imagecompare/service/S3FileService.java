package com.test.app.imagecompare.service;

import org.springframework.stereotype.Service;

@Service
public class S3FileService {

    public String getFileName(String path) {
        String[] parts = path.split("/");
        return parts[parts.length - 1];
    }

    public byte[] downloadImage(String path) {
        return new byte[0];
    }
}
