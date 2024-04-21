package com.johnny.libmgtbackend.dtos;

import java.util.HashMap;
import java.util.Map;

public class ErrorDto {
    public String description;
    public Map<String, String> fieldErrors = new HashMap<>();

    public ErrorDto(String description) {
        this.description = description;
    }
}
