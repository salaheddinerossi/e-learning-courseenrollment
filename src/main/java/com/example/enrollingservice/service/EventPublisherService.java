package com.example.enrollingservice.service;

import com.example.enrollingservice.dto.StudentSkillDto;

public interface EventPublisherService {
    public void publishStudentSkillEvent(StudentSkillDto studentSkillDto);
}
