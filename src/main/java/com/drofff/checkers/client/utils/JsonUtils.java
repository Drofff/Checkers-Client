package com.drofff.checkers.client.utils;

import com.drofff.checkers.client.exception.CheckersClientException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils {

    private JsonUtils() {}

    public static <T> T parseValueOfClassFromJson(Class<T> valueClass, String json) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(json, valueClass);
        } catch(JsonProcessingException e) {
            throw new CheckersClientException(e.getMessage());
        }
    }

    public static <T> String serializeIntoJson(T obj) {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.valueToTree(obj).toString();
    }

}