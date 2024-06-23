package com.example.enrollingservice.Enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Language {
    FRENCH,
    ARABIC,
    ENGLISH;

    @JsonValue
    public String toValue() {
        return this.name(); // Customize if needed
    }

    @JsonCreator
    public static Language forValue(String value) {
        for (Language language : Language.values()) {
            if (language.name().equalsIgnoreCase(value)) {
                return language;
            }
        }
        throw new IllegalArgumentException("Unknown language: " + value);
    }

    }
