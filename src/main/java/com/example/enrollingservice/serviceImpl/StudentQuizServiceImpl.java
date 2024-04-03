package com.example.enrollingservice.serviceImpl;

import com.example.enrollingservice.Enums.StudentLessonStatus;
import com.example.enrollingservice.dto.AnswerDto;
import com.example.enrollingservice.dto.QuizCorrectionDto;
import com.example.enrollingservice.dto.QuizzCorrection.*;
import com.example.enrollingservice.dto.StudentSkillDto;
import com.example.enrollingservice.exception.BadRequestException;
import com.example.enrollingservice.exception.ResourceNotFoundException;
import com.example.enrollingservice.model.CourseEnrollment;
import com.example.enrollingservice.model.Quizzes.ExplanatoryQuestion;
import com.example.enrollingservice.model.Quizzes.MultipleChoiceQuestion;
import com.example.enrollingservice.model.Quizzes.Quiz;
import com.example.enrollingservice.model.Quizzes.TrueFalseQuestion;
import com.example.enrollingservice.model.StudentLesson;
import com.example.enrollingservice.model.StudentQuiz;
import com.example.enrollingservice.repository.*;
import com.example.enrollingservice.response.CorrectionResponse;
import com.example.enrollingservice.response.ExplanatoryQuestionsCorrectionResponse;
import com.example.enrollingservice.response.QuizCorrectionResponse;
import com.example.enrollingservice.service.AiService;
import com.example.enrollingservice.service.EventPublisherService;
import com.example.enrollingservice.service.StudentQuizService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
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

    final
    StudentLessonRepository studentLessonRepository;

    final
    CourseEnrollmentRepository courseEnrollmentRepository;

    final
    EventPublisherService eventPublisherService;

    public StudentQuizServiceImpl(QuizRepository quizRepository, StudentQuizRepository studentQuizRepository, StudentRepository studentRepository, AiService aiService, StudentLessonRepository studentLessonRepository, CourseEnrollmentRepository courseEnrollmentRepository, EventPublisherService eventPublisherService) {
        this.quizRepository = quizRepository;
        this.studentQuizRepository = studentQuizRepository;
        this.studentRepository = studentRepository;
        this.aiService = aiService;
        this.studentLessonRepository = studentLessonRepository;
        this.courseEnrollmentRepository = courseEnrollmentRepository;
        this.eventPublisherService = eventPublisherService;
    }


    @Override
    @Transactional
    public QuizCorrectionResponse correctQuiz(QuizCorrectionDto quizCorrectionDto,String email) {

        StudentQuiz studentQuiz = findStudentQuiz(email,quizCorrectionDto.getId());

        Quiz quiz = findQuizById(quizCorrectionDto.getId());

        double correctAnswers=0;

        double wrongAnswers=0;

        double mark= 0;

        double questionsNumber=0D;

        QuizCorrectionResponse quizCorrectionResponse = new QuizCorrectionResponse();


        if(studentQuiz.getIsPassed()){
            throw new BadRequestException("you have already passed this quiz");
        }

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
                quizCorrectionResponse.setAdvices(explanatoryQuestionsCorrectionResponse.getAdvices());
                System.out.println(explanatoryQuestionsCorrectionResponse);
                studentQuiz.setAdvices(explanatoryQuestionsCorrectionResponse.getAdvices());
                mark = explanatoryQuestionsCorrectionResponse.getMark();

            }


        }

        studentQuiz.setMark(mark);
        if (mark>5D){
            studentQuiz.setIsPassed(true);
        }else {
            studentQuiz.setIsPassed(false);
        }

        StudentQuiz studentQuiz1 = studentQuizRepository.save(studentQuiz);

        if (isStudentLessonCompleted(studentQuiz1)){
            StudentLesson studentLesson = studentQuiz1.getStudentLesson();
            studentLesson.setStudentLessonStatus(StudentLessonStatus.COMPLETED);
            studentLessonRepository.save(studentLesson);
            quizCorrectionResponse.setLessonCompleted(true);
            if (isCourseCompleted(studentQuiz1,studentLesson.getId())){
                CourseEnrollment courseEnrollment = studentQuiz1.getStudentLesson().getCourseEnrollment();
                courseEnrollment.setIsCompleted(true);
                courseEnrollmentRepository.save(courseEnrollment);
                quizCorrectionResponse.setCourseCompleted(true);

                eventPublisherService.publishStudentSkillEvent(new StudentSkillDto(courseEnrollment.getStudent().getId(),courseEnrollment.getCourse().getId()));



            }else {
                quizCorrectionResponse.setCourseCompleted(false);
            }
        }else {
            quizCorrectionResponse.setLessonCompleted(false);
        }

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

    private Boolean isStudentLessonCompleted(StudentQuiz studentQuiz){
        for (StudentQuiz studentQuiz1 : studentQuiz.getStudentLesson().getStudentQuizzes()){
            if(!studentQuiz1.getIsPassed()){
                return false;
            }
        }
        return true;
    }

    private Boolean isCourseCompleted(StudentQuiz studentQuiz,Long studentLessonId){
        for (StudentLesson studentLesson:studentQuiz.getStudentLesson().getCourseEnrollment().getStudentLessons()){

            // passing the case when the student lesson is already checked that it is completed
            if(Objects.equals(studentLesson.getId(), studentLessonId)){
                continue;
            }

            if(studentLesson.getStudentLessonStatus()!=StudentLessonStatus.COMPLETED && !studentLesson.getStudentQuizzes().isEmpty() ){

                return false;
            }
        }

        return true;
    }

}
