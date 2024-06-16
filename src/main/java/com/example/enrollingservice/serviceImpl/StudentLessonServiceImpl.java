package com.example.enrollingservice.serviceImpl;

import com.example.enrollingservice.Enums.StudentLessonStatus;
import com.example.enrollingservice.exception.ResourceNotFoundException;
import com.example.enrollingservice.mapper.ChatHistoryMapper;
import com.example.enrollingservice.mapper.CourseNotesMapper;
import com.example.enrollingservice.mapper.StudentQuizMapper;
import com.example.enrollingservice.model.*;
import com.example.enrollingservice.model.Quizzes.ExplanatoryQuestion;
import com.example.enrollingservice.model.Quizzes.MultipleChoiceQuestion;
import com.example.enrollingservice.model.Quizzes.TrueFalseQuestion;
import com.example.enrollingservice.repository.*;
import com.example.enrollingservice.response.*;
import com.example.enrollingservice.service.StudentLessonService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class StudentLessonServiceImpl implements StudentLessonService {

    final
    StudentLessonRepository studentLessonRepository;

    final
    CourseNotesRepository courseNotesRepository;

    final
    CourseNotesMapper courseNotesMapper;

    final
    CourseEnrollmentRepository courseEnrollmentRepository;

    final
    ChatHistoryMapper chatHistoryMapper;

    final
    ChatHistoryRepository chatHistoryRepository;

    final
    StudentQuizRepository studentQuizRepository;

    final
    StudentQuizMapper studentQuizMapper;

    public StudentLessonServiceImpl(CourseNotesMapper courseNotesMapper, CourseNotesRepository courseNotesRepository, StudentLessonRepository studentLessonRepository, ChatHistoryMapper chatHistoryMapper, ChatHistoryRepository chatHistoryRepository, StudentQuizRepository studentQuizRepository, StudentQuizMapper studentQuizMapper, CourseEnrollmentRepository courseEnrollmentRepository) {
        this.courseNotesMapper = courseNotesMapper;
        this.courseNotesRepository = courseNotesRepository;
        this.studentLessonRepository = studentLessonRepository;
        this.chatHistoryMapper =chatHistoryMapper;
        this.chatHistoryRepository =chatHistoryRepository;
        this.studentQuizRepository = studentQuizRepository;
        this.studentQuizMapper = studentQuizMapper;
        this.courseEnrollmentRepository = courseEnrollmentRepository;
    }

    @Override
    public StudentLessonResponse getStudentLesson(Long id) {
        StudentLesson studentLesson = findStudentLessonById(id);
        StudentLessonResponse studentLessonResponse = new StudentLessonResponse();
        studentLessonResponse.setId(studentLesson.getId());
        studentLessonResponse.setStudentLessonStatus(studentLesson.getStudentLessonStatus());
        studentLessonResponse.setLessonId(studentLesson.getLesson().getId());

        CourseEnrollment courseEnrollment = studentLesson.getCourseEnrollment();

        courseEnrollment.setCurrentLessonId(id);

        List<CourseNotes> courseNotes = courseNotesRepository.findByStudentLesson(studentLesson);
        List<CourseNotesResponse> courseNotesResponses = courseNotesMapper.courseNotesListToCourseNotesResponseList(courseNotes);

        Optional<ChatHistory> chatHistoryOptional = chatHistoryRepository.findByStudentLesson(studentLesson);

        if (chatHistoryOptional.isPresent()){
            studentLessonResponse.setIsChatExist(true);
            ChatHistory chatHistory = chatHistoryOptional.get();

            studentLessonResponse.setChatId(chatHistory.getId());
            List<ChatHistoryResponse> chatHistoryResponses = chatHistoryMapper.chatRecordListToChatHistoryResponseList(chatHistory.getChatRecords());

            studentLessonResponse.setChatHistoryResponseList(chatHistoryResponses);
        }else{
            studentLessonResponse.setIsChatExist(false);
        }

        System.out.println(id);

        List<StudentQuiz> studentQuizzes = studentQuizRepository.findByStudentLessonId(id);

        List<StudentQuizResponse> studentQuizResponses = new ArrayList<>();

        for(StudentQuiz studentQuiz:studentQuizzes){

            StudentQuizResponse  studentQuizResponse = studentQuizMapper.studentQuizToStudentQuizResponse(studentQuiz);
            List<QuestionResponses> questionResponsesList = new ArrayList<>();

            Object firstQuestion = studentQuiz.getQuiz().getQuestions().get(0);
            if (firstQuestion instanceof TrueFalseQuestion){
                for (TrueFalseQuestion trueFalseQuestion:(List<TrueFalseQuestion>)(List<?>)studentQuiz.getQuiz().getQuestions()){
                    QuestionResponses questionResponses = new QuestionResponses();
                    studentQuizResponse.setType("TrueFalse");
                    questionResponses.setQuestion(trueFalseQuestion.getPrompt());
                    questionResponses.setId(trueFalseQuestion.getId());

                    questionResponsesList.add(questionResponses);
                }

            }

            if (firstQuestion instanceof ExplanatoryQuestion){
                for (ExplanatoryQuestion explanatoryQuestion:(List<ExplanatoryQuestion>)(List<?>)studentQuiz.getQuiz().getQuestions()){
                    QuestionResponses questionResponses = new QuestionResponses();
                    studentQuizResponse.setType("Explanatory");
                    questionResponses.setQuestion(explanatoryQuestion.getPrompt());
                    questionResponses.setId(explanatoryQuestion.getId());

                    questionResponsesList.add(questionResponses);

                }
            }

            if (firstQuestion instanceof MultipleChoiceQuestion){
                for (MultipleChoiceQuestion multipleChoiceQuestion:(List<MultipleChoiceQuestion>)(List<?>)studentQuiz.getQuiz().getQuestions()){
                    QuestionResponses questionResponses = new QuestionResponses();
                    studentQuizResponse.setType("MultipleChoice");
                    questionResponses.setQuestion(multipleChoiceQuestion.getPrompt());
                    questionResponses.setId(multipleChoiceQuestion.getId());
                    questionResponses.setOptions(multipleChoiceQuestion.getOptions());

                    questionResponsesList.add(questionResponses);
                }
            }

            studentQuizResponse.setQuestionResponsesList(questionResponsesList);

            studentQuizResponses.add(studentQuizResponse);

        }

        studentQuizMapper.studentQuizListToStudentQuizResponseList(studentQuizzes);
        studentLessonResponse.setStudentQuizResponses(studentQuizResponses);
        studentLessonResponse.setCourseNotesResponses(courseNotesResponses);


        if (studentLesson.getStudentLessonStatus()==StudentLessonStatus.INITIAL){
            studentLesson.setStudentLessonStatus(StudentLessonStatus.READ);
            studentLessonRepository.save(studentLesson);
        }

        courseEnrollmentRepository.save(courseEnrollment);

        return studentLessonResponse;
    }



    StudentLesson findStudentLessonById(Long id){
        return studentLessonRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("no student lesson with this id: "+id)
        );
    }

}
