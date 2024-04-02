package com.example.enrollingservice.serviceImpl;

import com.example.enrollingservice.dto.AnswerDto;
import com.example.enrollingservice.dto.QuizCorrectionDto;
import com.example.enrollingservice.dto.QuizzCorrection.*;
import com.example.enrollingservice.exception.ResourceNotFoundException;
import com.example.enrollingservice.model.Quizzes.ExplanatoryQuestion;
import com.example.enrollingservice.model.Quizzes.MultipleChoiceQuestion;
import com.example.enrollingservice.model.Quizzes.Quiz;
import com.example.enrollingservice.model.Quizzes.TrueFalseQuestion;
import com.example.enrollingservice.model.StudentQuiz;
import com.example.enrollingservice.repository.QuizRepository;
import com.example.enrollingservice.repository.StudentQuizRepository;
import com.example.enrollingservice.repository.StudentRepository;
import com.example.enrollingservice.response.CorrectionResponse;
import com.example.enrollingservice.response.ExplanatoryQuestionsCorrectionResponse;
import com.example.enrollingservice.response.QuizCorrectionResponse;
import com.example.enrollingservice.service.AiService;
import com.example.enrollingservice.service.StudentQuizService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;


@Service
public class StudentQuizServiceImpl implements StudentQuizService {

    final
    StudentRepository studentRepository;

    final
    StudentQuizRepository studentQuizRepository;

    final
    QuizRepository quizRepository;

    final
    AiService aiService;

    public StudentQuizServiceImpl(QuizRepository quizRepository, StudentQuizRepository studentQuizRepository, StudentRepository studentRepository, AiService aiService) {
        this.quizRepository = quizRepository;
        this.studentQuizRepository = studentQuizRepository;
        this.studentRepository = studentRepository;
        this.aiService = aiService;
    }


    @Override
    public QuizCorrectionResponse correctQuiz(QuizCorrectionDto quizCorrectionDto,String email) {

        StudentQuiz studentQuiz = findStudentQuiz(email,quizCorrectionDto.getId());

        Quiz quiz = findQuizById(quizCorrectionDto.getId());

        double correctAnswers=0;

        double wrongAnswers=0;

        double mark= 0;

        double questionsNumber=0D;

        QuizCorrectionResponse quizCorrectionResponse = new QuizCorrectionResponse();


        if (!quiz.getQuestions().isEmpty()) {
            Object firstQuestion = quiz.getQuestions().get(0);

            if (firstQuestion instanceof MultipleChoiceQuestion) {

                List<MultipleChoiceQuestion> multipleChoiceQuestions = (List<MultipleChoiceQuestion>)(List<?>) quiz.getQuestions();
                MultipleChoiceQuestionsDto multipleChoiceQuestionsDto = new MultipleChoiceQuestionsDto();
                multipleChoiceQuestionsDto.setTranscribe(studentQuiz.getQuiz().getLesson().getTranscribe());
                questionsNumber=multipleChoiceQuestions.size();

                for (AnswerDto answerDto : quizCorrectionDto.getAnswerDtos()) {

                    for (MultipleChoiceQuestion multipleChoiceQuestion : multipleChoiceQuestions) {
                        if (Objects.equals(multipleChoiceQuestion.getId(), answerDto.getId())){

                            StringQuestionDto stringQuestionDto = new StringQuestionDto(multipleChoiceQuestion.getId(), multipleChoiceQuestion.getPrompt(), answerDto.getAnswer(), multipleChoiceQuestion.getCorrectAnswer());

                            multipleChoiceQuestionsDto.getStringQuestionDtos().add(stringQuestionDto);
                            if(Objects.equals(answerDto.getAnswer(), multipleChoiceQuestion.getCorrectAnswer())){
                                correctAnswers++;
                            }else {
                                wrongAnswers++;
                            }
                        }
                    }
                }

                CorrectionResponse correctionResponse = aiService.correctMultipleChoiceQuiz(multipleChoiceQuestionsDto);
                quizCorrectionResponse.setAdvices(correctionResponse.getAdvices());
                studentQuiz.setAdvices(correctionResponse.getAdvices());
                mark = (correctAnswers/questionsNumber) *10;


            } else if (firstQuestion instanceof TrueFalseQuestion) {
                List<TrueFalseQuestion> trueFalseQuestions = (List<TrueFalseQuestion>)(List<?>) quiz.getQuestions();

                TrueFalseQuestionsDto trueFalseQuestionsDto = new TrueFalseQuestionsDto();
                trueFalseQuestionsDto.setTranscribe(studentQuiz.getQuiz().getLesson().getTranscribe());
                questionsNumber=trueFalseQuestions.size();

                for (AnswerDto answerDto : quizCorrectionDto.getAnswerDtos()) {

                    for (TrueFalseQuestion trueFalseQuestion : trueFalseQuestions) {
                        if (Objects.equals(trueFalseQuestion.getId(), answerDto.getId())){

                            BooleanQuestionDto booleanQuestionDto = new BooleanQuestionDto(trueFalseQuestion.getId(), trueFalseQuestion.getPrompt(), answerDto.getTrueFalseAnswer(), trueFalseQuestion.getCorrectAnswer());

                            trueFalseQuestionsDto.getBooleanQuestionDtos().add(booleanQuestionDto);

                            if(Objects.equals(answerDto.getTrueFalseAnswer(), trueFalseQuestion.getCorrectAnswer())){

                                correctAnswers++;
                            }else {
                                wrongAnswers++;
                            }
                        }
                    }
                }

                CorrectionResponse correctionResponse = aiService.correctTrueFalseQuiz(trueFalseQuestionsDto);
                quizCorrectionResponse.setAdvices(correctionResponse.getAdvices());
                studentQuiz.setAdvices(correctionResponse.getAdvices());
                mark = (correctAnswers/questionsNumber) *10;


            } else if (firstQuestion instanceof ExplanatoryQuestion) {
                List<ExplanatoryQuestion> explanatoryQuestions = (List<ExplanatoryQuestion>)(List<?>) quiz.getQuestions();
                ExplanatoryQuestionsDto explanatoryQuestionsDto = new ExplanatoryQuestionsDto();
                explanatoryQuestionsDto.setTranscribe(studentQuiz.getQuiz().getLesson().getTranscribe());
                questionsNumber=explanatoryQuestions.size();

                for (AnswerDto answerDto : quizCorrectionDto.getAnswerDtos()) {

                    for (ExplanatoryQuestion explanatoryQuestion : explanatoryQuestions) {
                        if (Objects.equals(explanatoryQuestion.getId(), answerDto.getId())){

                            ExplanatoryQuestionDto explanatoryQuestionDto = new ExplanatoryQuestionDto(explanatoryQuestion.getId(), explanatoryQuestion.getPrompt(), answerDto.getAnswer(), explanatoryQuestion.getCorrectExplanation());

                            explanatoryQuestionsDto.getExplanatoryQuestionDtos().add(explanatoryQuestionDto);
                        }
                    }
                }

                ExplanatoryQuestionsCorrectionResponse explanatoryQuestionsCorrectionResponse = aiService.correctExplanatoryQuiz(explanatoryQuestionsDto);
                quizCorrectionResponse.setAdvices(explanatoryQuestionsCorrectionResponse.getAdvice());
                studentQuiz.setAdvices(explanatoryQuestionsCorrectionResponse.getAdvice());
                mark = explanatoryQuestionsCorrectionResponse.getMark();

            }


        }

        studentQuiz.setMark(mark);
        if (mark>7D){
            studentQuiz.setIsPassed(true);
        }else {
            studentQuiz.setIsPassed(false);
        }

        studentQuizRepository.save(studentQuiz);

        quizCorrectionResponse.setIsPassed(studentQuiz.getIsPassed());
        quizCorrectionResponse.setMark(mark);
        quizCorrectionResponse.setId(studentQuiz.getId());

        return quizCorrectionResponse;
    }


    private StudentQuiz findStudentQuiz(String email,Long quizId){
        return studentQuizRepository.findByStudentLessonCourseEnrollmentStudentEmailAndQuizId(email,quizId).orElseThrow(
                () -> new ResourceNotFoundException("studentQuiz Not Found")
        );
    }

    private Quiz findQuizById(Long quizId){
        return quizRepository.findById(quizId).orElseThrow(
                () -> new ResourceNotFoundException("quiz not found with this id ")
        );

    }

}
