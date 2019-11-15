package com.test.app.imagecompare.dto;

public class CompareImagesRequest {
    private String firstImagePath;
    private String secondImagePath;

    public CompareImagesRequest() {
    }

    public CompareImagesRequest(String firstImagePath, String secondImagePath) {
        this.firstImagePath = firstImagePath;
        this.secondImagePath = secondImagePath;
    }

    public String getFirstImagePath() {
        return firstImagePath;
    }

    public void setFirstImagePath(String firstImagePath) {
        this.firstImagePath = firstImagePath;
    }

    public String getSecondImagePath() {
        return secondImagePath;
    }

    public void setSecondImagePath(String secondImagePath) {
        this.secondImagePath = secondImagePath;
    }
}
