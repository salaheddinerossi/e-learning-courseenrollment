package com.example.enrollingservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StudentSkillDto {
    private Long studentId;
    private Long courseId;
}
