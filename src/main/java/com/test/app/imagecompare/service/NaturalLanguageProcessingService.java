package com.test.app.imagecompare.service;

import org.springframework.stereotype.Service;
import snowball.SnowballStemmer;
import snowball.ext.englishStemmer;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class NaturalLanguageProcessingService {

    public String getImageGroupName(List<String> filenames) {
        List<String> domainWords = filenames
                .stream()
                // remove file extension
                .map(filename -> {
                    return filename.substring(0, filename.lastIndexOf('.'));
                })
                // get domain words
                .flatMap(filename -> Stream.of(
                        filename.split("[^a-zA-Z']+")
                ))
                // stemming words
                .map(this::stemmingWord)
                .collect(Collectors.toList());

        // just count words and see which one is used frequently
        HashMap<String, Integer> countMap = new HashMap<>();

        // init count map
        new HashSet<>(domainWords)
                .forEach(word -> countMap.put(word, 0));

        // count words
        domainWords.forEach(word -> {
            Integer currentValue = countMap.get(word);
            countMap.replace(word, currentValue + 1);
        });

        // get order list of entries
        return countMap
                .entrySet()
                .stream()
                .max(Comparator.comparingInt(Map.Entry::getValue))
                .map(Map.Entry::getKey)
                .orElse("unknown");
    }

    private String stemmingWord(String word) {
        SnowballStemmer stemmer = new englishStemmer();
        stemmer.setCurrent(word);
        stemmer.stem();
        return stemmer.getCurrent();
    }
}
