package com.test.app.imagecompare.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.app.imagecompare.service.DeepHashService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
public class HashController {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DeepHashService deepHashService;

    @PostMapping("/hash")
    public Object hash(@RequestBody String json) throws JsonProcessingException {
        ArrayList jsonArray = objectMapper.readValue(json, ArrayList.class);

        return deepHashService.calcValueHash(jsonArray);
    }
}
