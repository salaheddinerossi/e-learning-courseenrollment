package com.example.enrollingservice.Enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum CourseLevel {
    BEGINNER,
    INTERMEDIATE,
    ADVANCED;

    @JsonValue
    public String toValue() {
        // This method controls the output value in serialization.
        return this.name(); // You can customize this if necessary.
    }

    @JsonCreator
    public static CourseLevel forValue(String value) {
        // This static method provides custom deserialization logic.
        for (CourseLevel level : CourseLevel.values()) {
            if (level.name().equalsIgnoreCase(value)) {
                // Case-insensitive matching.
                return level;
            }
        }
        // If no matching enum constant is found, throw an exception or handle as needed.
        throw new IllegalArgumentException("Unknown course level: " + value);
    }

}
