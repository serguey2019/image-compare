package com.test.app.imagecompare.service;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DeepHashService {
    public Object calcValueHash(Object json) {
        return getHashedObject(json);
    }

    /**
     * 3 cases expected here:
     * json is map - calc it recursively
     * json is collection - pass it to the DeepHashService#getHashedMap
     * json is value - just hash it
     */
    private Object getHashedObject(Object json) {
        if (json instanceof Map) {
            HashMap<Object, Object> result = new HashMap<>();
            // iterate over all keys and pass them to this method recursively
            ((Map) json).forEach((key, value) -> {
                result.put(key, getHashedObject(value));
            });

            return result;
        } else if (json instanceof List) {
            return ((List)json)
                    .stream()
                    .map(this::getHashedObject)
                    .collect(Collectors.toList());
        } else {
            return DigestUtils
                    .md5Hex(String.valueOf(json));
        }
    }
}
