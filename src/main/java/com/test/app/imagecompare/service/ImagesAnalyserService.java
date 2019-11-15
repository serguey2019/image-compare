package com.test.app.imagecompare.service;

import org.springframework.stereotype.Service;

@Service
public class ImagesAnalyserService {
    public boolean isIdentical(byte[] first, byte[] second) {
        return Math.random() < 0.5;
    }
}
