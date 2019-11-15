package com.test.app.imagecompare.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NaturalLanguageProcessingServiceTest {
    private static NaturalLanguageProcessingService SERVICE = null;

    @BeforeAll
    public static void init() {
        SERVICE = new NaturalLanguageProcessingService();
    }

    @Test
    void getImageGroupNameSuccess1() {
        String name = SERVICE.getImageGroupName(
                Arrays.asList(
                        "diagram0.png",
                        "diagram1.png",
                        "diagram2.png"
                )
        );

        assertEquals("diagram", name);
    }

    @Test
    void getImageGroupNameSuccess2() {
        String name = SERVICE.getImageGroupName(
                Arrays.asList(
                        "im_worked.png",
                        "he_was_working.png",
                        "he_works.png",
                        "I_work.png"
                )
        );

        assertEquals("work", name);
    }
}