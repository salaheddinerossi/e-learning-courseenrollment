package com.example.enrollingservice.model.Quizzes;

import com.example.enrollingservice.util.StringListToJsonConverter;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class MultipleChoiceQuestion extends Question {
    @Convert(converter = StringListToJsonConverter.class)
    private List<String> options;

    private String correctAnswer;

}
