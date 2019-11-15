package com.test.app.imagecompare.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CompareImagesService {
    @Autowired
    private S3FileService s3FileService;

    @Autowired
    private ImagesAnalyserService imagesAnalyserService;

    @Autowired
    private NaturalLanguageProcessingService naturalLanguageProcessingService;

    public boolean compareTwoImages(String firstImage, String secondImage) {
        return imagesAnalyserService.isIdentical(
                s3FileService.downloadImage(firstImage),
                s3FileService.downloadImage(secondImage)
        );
    }

    public List<Map> groupImagesByName(List<String> paths) {
        List<Group> groups = groupImages(paths);

        // iterate over groups and find the group domain by filenames
        return groups
                .stream()
                .map(group -> {
                    String imageGroupName = naturalLanguageProcessingService.getImageGroupName(group.getImages());
                    return Collections.singletonMap(imageGroupName, group.getImages());
                })
                .collect(Collectors.toList());
    }

    public List<Group> groupImages(List<String> paths) {
        List<List<Image>> groups = new ArrayList<>();

        List<Image> images = paths
                .stream()
                .map(path -> {
                    byte[] data = s3FileService.downloadImage(path);
                    String fileName = s3FileService.getFileName(path);
                    return new Image(path, fileName, data);
                })
                .collect(Collectors.toList());

        // iterate over images and compare them
        images
                .forEach(image -> {
                    // find identical image in groups
                    Optional<List<Image>> group = groups
                            .stream()
                            .filter(groupImages -> groupImages
                                    .stream()
                                    .anyMatch(groupImage -> imagesAnalyserService.isIdentical(groupImage.getData(), image.getData()))
                            )
                            .findFirst();

                    // in case if identical images found in any group
                    // then put the image there
                    // otherwise create new group
                    if (group.isPresent()) {
                        group.get().add(image);
                    } else {
                        ArrayList<Image> imageArrayList = new ArrayList<>();
                        imageArrayList.add(image);
                        groups.add(imageArrayList);
                    }
                });
        return groups
                .stream()
                .map(groupedImages -> new Group(
                        groupedImages
                                .stream()
                                .map(Image::getFileName)
                                .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());
    }

    public static class Image {
        private String path;
        private String fileName;
        private byte[] data;

        public Image(String path, String fileName, byte[] data) {
            this.path = path;
            this.fileName = fileName;
            this.data = data;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public byte[] getData() {
            return data;
        }

        public void setData(byte[] data) {
            this.data = data;
        }
    }

    public static class Group {
        private List<String> images;

        public Group(List<String> images) {
            this.images = images;
        }

        public List<String> getImages() {
            return images;
        }

        public void setImages(List<String> images) {
            this.images = images;
        }
    }
}
