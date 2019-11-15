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
public class CompareImagesControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void groupSuccessTest() {
        ResponseEntity<List> response = this.restTemplate.postForEntity("http://localhost:" + port + "/group", getRequest(), List.class);
        List body = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(body);

        // get first group
        assertTrue(body.size() > 0);
        Map<String, List<String>> group = (Map<String, List<String>>) body.get(0);

        assertEquals(1, group.size());
        assertTrue(group.containsKey("images"));
        assertTrue(group.get("images").size() > 0);
    }

    private List<String> getRequest() {
        return Arrays.asList(
                "/path/to/images/gray_moon.png",
                "/path/to/images/white_moon.png",
                "/path/to/images/red_moon.png",
                "/path/to/images/bird.png",
                "/path/to/images/diagram.png",
                "/path/to/images/diagram1.png",
                "/path/to/images/diagram2.png"
        );
    }
}
