package com.test.app.imagecompare.controller;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HashControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void groupSuccessTest() {
        ResponseEntity<List> response = this.restTemplate.postForEntity("http://localhost:" + port + "/hash", json(), List.class);
        List body = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(body);

        assertEquals(3, body.size());

        assertTrue(body.get(0) instanceof Map);
        assertTrue(body.get(1) instanceof String);
        assertTrue(body.get(2) instanceof List);

        // first item
        Map<String, Map<String, Map<String, String>>> first = (Map<String, Map<String, Map<String, String>>>) body.get(0);
        assertEquals(1, first.size());
        assertEquals(1, first.get("json").size());
        assertEquals(1, first.get("json").get("object").size());
        assertEquals("3ce89d936eca1e50ffda2a13f392d49e", first.get("json").get("object").get("with"));

        // second item
        assertEquals("63c9af1dbfb40b7f7109f9a174479484", body.get(1));

        // third item
        List<Map<String, String>> third = (List<Map<String, String>>) body.get(2);
        assertEquals(1, third.size());
        assertEquals(1, third.get(0).size());
        assertEquals("f1f713c9e000f5d3f280adbd124df4f5", third.get(0).get("an"));
    }

    private String json() {
        return "[{\"json\": {\"object\": {\"with\": \"deepStructure\"}}}, \"singleValue\", [{\"an\": \"array\"}]]";
    }
}
