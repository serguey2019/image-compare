package com.test.app.imagecompare.controller;

import com.test.app.imagecompare.dto.CompareImagesRequest;
import com.test.app.imagecompare.dto.CompareImagesResponse;
import com.test.app.imagecompare.service.CompareImagesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class CompareImagesController {

    @Autowired
    private CompareImagesService compareImagesService;

    @PostMapping("compare")
    public CompareImagesResponse compare(@RequestBody CompareImagesRequest request) {
        boolean isIdentical = compareImagesService.compareTwoImages(
                request.getFirstImagePath(),
                request.getSecondImagePath()
        );

        return new CompareImagesResponse(isIdentical);
    }

    @PostMapping("groupByName")
    public List<Map> groupByName(@RequestBody ArrayList<String> paths) {
        return compareImagesService.groupImagesByName(paths);
    }

    @PostMapping("group")
    public List<CompareImagesService.Group> group(@RequestBody ArrayList<String> paths) {
        return compareImagesService.groupImages(paths);
    }
}
