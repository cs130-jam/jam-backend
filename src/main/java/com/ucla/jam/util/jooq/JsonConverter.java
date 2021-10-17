package com.ucla.jam.util.jooq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.ucla.jam.util.ObjectMapperProvider;
import lombok.extern.slf4j.Slf4j;
import org.jooq.Converter;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

@Slf4j
public class JsonConverter<T> implements Converter<String, T> {

    private final Class<T> toType;
    private final ObjectReader reader;
    private final ObjectWriter writer;

    @SuppressWarnings("unchecked")
    public JsonConverter() {
        Type conversionType = ((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        ObjectMapper objectMapper = ObjectMapperProvider.get();
        toType = (Class<T>)objectMapper.constructType(conversionType).getRawClass();
        TypeReference<T> typeReference = new TypeReference<T>() {
            @Override
            public Type getType() {
                return conversionType;
            }
        };
        reader = objectMapper.readerFor(typeReference);
        writer = objectMapper.writerFor(typeReference);
    }

    @Override
    public T from(String s) {
        try {
            return s == null ? null : reader.readValue(s);
        } catch (JsonProcessingException e) {
            log.error("Failed to read json {} to object: {}", s, e);
            return null;
        }
    }

    @Override
    public String to(T t) {
        try {
            return t == null ? null : writer.writeValueAsString(t);
        } catch (JsonProcessingException e) {
            log.error("Failed to write to {} to json: {}", t, e);
            return null;
        }
    }

    @Override
    public Class<String> fromType() {
        return String.class;
    }

    @Override
    public Class<T> toType() {
        return toType;
    }
}
