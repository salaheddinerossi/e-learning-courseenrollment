package com.example.enrollingservice.serviceImpl;

import com.example.enrollingservice.dto.StudentSkillDto;
import com.example.enrollingservice.service.EventPublisherService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;


@Service
public class EventPublisherServiceImpl implements EventPublisherService {

    private final StringRedisTemplate redisTemplate;

    private final ObjectMapper objectMapper;

    public EventPublisherServiceImpl(StringRedisTemplate redisTemplate, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    public void publishStudentSkillEvent(StudentSkillDto studentSkillDto) {
        try {
            String message = objectMapper.writeValueAsString(studentSkillDto);
            redisTemplate.convertAndSend("studentSkillTopic", message);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing message", e);
        }
    }

}
