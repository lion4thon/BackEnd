package com.api.mov.global.coverter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Converter // 이 클래스가 JPA 변환기임을 알림
public class SimpleStringListConverter implements AttributeConverter<List<String>, String> {

    private static final String DELIMITER = ","; // 콤마로 구분

    // List<String> -> DB에 저장될 String으로 변환
    // 예: ["어깨", "가슴"] -> "어깨,가슴"
    @Override
    public String convertToDatabaseColumn(List<String> attribute) {
        if (attribute == null || attribute.isEmpty()) {
            return null;
        }
        return attribute.stream().collect(Collectors.joining(DELIMITER));
    }

    // DB의 String -> List<String>으로 변환
    // 예: "어깨,가슴" -> ["어깨", "가슴"]
    @Override
    public List<String> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return null;
        }
        return Arrays.asList(dbData.split(DELIMITER));
    }
}