package com.test.app.imagecompare.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DeepHashServiceTests {
    private DeepHashService deepHashService;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void init() {
        objectMapper = new ObjectMapper();
        deepHashService = new DeepHashService();
    }

    @Test
    void calcValueHashSuccessTest() throws JsonProcessingException {
        ArrayList jsonArray = objectMapper.readValue(json(), ArrayList.class);
        Object array = deepHashService.calcValueHash(jsonArray);

        assertTrue(array instanceof List);
        List list = (List) array;
        assertEquals(3, list.size());

        assertTrue(list.get(0) instanceof Map);
        assertTrue(list.get(1) instanceof String);
        assertTrue(list.get(2) instanceof List);

        // first item
        Map<String, Map<String, Map<String, String>>> first = (Map<String, Map<String, Map<String, String>>>) list.get(0);
        assertEquals(1, first.size());
        assertEquals(1, first.get("json").size());
        assertEquals(1, first.get("json").get("object").size());
        assertEquals("3ce89d936eca1e50ffda2a13f392d49e", first.get("json").get("object").get("with"));

        // second item
        assertEquals("63c9af1dbfb40b7f7109f9a174479484", list.get(1));

        // third item
        List<Map<String, String>> third = (List<Map<String, String>>) list.get(2);
        assertEquals(1, third.size());
        assertEquals(1, third.get(0).size());
        assertEquals("f1f713c9e000f5d3f280adbd124df4f5", third.get(0).get("an"));
    }

    private String json() {
        return "[{\"json\": {\"object\": {\"with\": \"deepStructure\"}}}, \"singleValue\", [{\"an\": \"array\"}]]";
    }
}
