package com.test.app.imagecompare.dto;

public class CompareImagesResponse {
    private boolean isIdentical;

    public CompareImagesResponse() {
    }

    public CompareImagesResponse(boolean isIdentical) {
        this.isIdentical = isIdentical;
    }

    public boolean isIdentical() {
        return isIdentical;
    }

    public void setIdentical(boolean identical) {
        isIdentical = identical;
    }
}
