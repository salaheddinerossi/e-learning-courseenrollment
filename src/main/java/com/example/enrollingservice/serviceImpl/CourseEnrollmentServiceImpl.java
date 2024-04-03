package com.example.enrollingservice.serviceImpl;

import com.example.enrollingservice.Enums.StudentLessonStatus;
import com.example.enrollingservice.exception.BadRequestException;
import com.example.enrollingservice.exception.ResourceNotFoundException;
import com.example.enrollingservice.mapper.LessonMapper;
import com.example.enrollingservice.model.*;
import com.example.enrollingservice.model.Quizzes.Quiz;
import com.example.enrollingservice.repository.CourseEnrollmentRepository;
import com.example.enrollingservice.repository.CourseRepository;
import com.example.enrollingservice.repository.StudentLessonRepository;
import com.example.enrollingservice.repository.StudentRepository;
import com.example.enrollingservice.response.ChapterResponse;
import com.example.enrollingservice.response.CourseEnrollmentResponse;
import com.example.enrollingservice.response.StudentLessonStatusResponse;
import com.example.enrollingservice.service.CourseEnrollmentService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class CourseEnrollmentServiceImpl implements CourseEnrollmentService {

    final
    CourseRepository courseRepository;

    final
    StudentRepository studentRepository;

    final
    CourseEnrollmentRepository courseEnrollmentRepository;

    final
    StudentLessonRepository studentLessonRepository;

    final
    LessonMapper lessonMapper;

    public CourseEnrollmentServiceImpl(CourseEnrollmentRepository courseEnrollmentRepository, CourseRepository courseRepository, StudentRepository studentRepository, LessonMapper lessonMapper, StudentLessonRepository studentLessonRepository) {
        this.courseEnrollmentRepository = courseEnrollmentRepository;
        this.courseRepository = courseRepository;
        this.studentRepository = studentRepository;
        this.lessonMapper = lessonMapper;
        this.studentLessonRepository = studentLessonRepository;
    }

    @Override
    @Transactional
    public void enrollCourse(Long courseId, String email) {
        CourseEnrollment courseEnrollment = new CourseEnrollment();

        Course course = findCourseById(courseId);
        Student student =findStudentByEmail(email);

        if(courseEnrollmentRepository.findByCourseIdAndStudent(courseId,student).isPresent()){
            throw  new BadRequestException("you have already enrolled this course");
        }


        courseEnrollment.setCourse(course);
        courseEnrollment.setStudent(student);

        for (Lesson lesson:getAllLessons(course)){

            if (lesson.getIsDeleted()){
                continue;
            }

            if (courseEnrollment.getCurrentLessonId()==null){
                courseEnrollment.setCurrentLessonId(lesson.getId());
            }else {
                if (courseEnrollment.getCurrentLessonId()> lesson.getId()){
                    courseEnrollment.setCurrentLessonId(lesson.getId());
                }
            }

            StudentLesson studentLesson = new StudentLesson();

            studentLesson.setCourseEnrollment(courseEnrollment);
            studentLesson.setLesson(lesson);
            studentLesson.setStudentLessonStatus(StudentLessonStatus.INITIAL);

            for (Quiz quiz:lesson.getQuizzes()){

                if(quiz.getIsDeleted()){
                    continue;
                }

                StudentQuiz studentQuiz = new StudentQuiz();
                studentQuiz.setQuiz(quiz);
                studentQuiz.setStudentLesson(studentLesson);
                studentLesson.getStudentQuizzes().add(studentQuiz);

            }

            courseEnrollment.getStudentLessons().add(studentLesson);

        }

        courseEnrollmentRepository.save(courseEnrollment);

    }

    @Override
    @Transactional
    public CourseEnrollmentResponse getCourseEnrollmentResponse(Long id) {

        CourseEnrollment courseEnrollment = findCourseEnrollmentById(id);
        Course course = courseEnrollment.getCourse();

        CourseEnrollmentResponse courseEnrollmentResponse = new CourseEnrollmentResponse();
        courseEnrollmentResponse.setId(courseEnrollment.getId());

        List<ChapterResponse> chapterResponses = mapChaptersToChapterResponses(course.getChapters(),courseEnrollment);
        courseEnrollmentResponse.setChapterResponses(chapterResponses);

        for (Chapter chapter:course.getChapters()){




        }




        return courseEnrollmentResponse;
    }


    private List<Lesson> getAllLessons(Course course) {
        List<Lesson> allLessons = new ArrayList<>();
        List<Chapter> chapters = course.getChapters();

        for (Chapter chapter : chapters) {
            if (chapter.getContainsChapters()) {
                allLessons.addAll(getLessonsFromChapterWithSubChapters(chapter));
            } else {
                allLessons.addAll(chapter.getLessons());
            }
        }

        return allLessons;
    }

    private List<Lesson> getLessonsFromChapterWithSubChapters(Chapter chapter) {
        List<Lesson> allLessons = new ArrayList<>();
        List<Chapter> subChapters = chapter.getChildChapters();

        for (Chapter subChapter : subChapters) {
            if (subChapter.getContainsChapters()) {
                allLessons.addAll(getLessonsFromChapterWithSubChapters(subChapter));
            } else {
                allLessons.addAll(subChapter.getLessons());
            }
        }
        return allLessons;
    }


    private List<ChapterResponse> mapChaptersToChapterResponses(List<Chapter> chapters,CourseEnrollment courseEnrollment) {
        List<ChapterResponse> chapterResponses = new ArrayList<>();

        for (Chapter chapter : chapters) {
            ChapterResponse chapterResponse = new ChapterResponse();
            chapterResponse.setId(chapter.getId());
            chapterResponse.setTitle(chapter.getTitle());
            chapterResponse.setContainsChapters(chapter.getContainsChapters());


            if (chapter.getContainsChapters()) {
                chapterResponse.setChapterResponses(mapChaptersToChapterResponses(chapter.getChildChapters(),courseEnrollment));
            } else {
                List<StudentLesson> studentLessons = studentLessonRepository.findByLessonChapterAndCourseEnrollment(chapter,courseEnrollment);
                chapterResponse.setStudentLessonStatusResponses(lessonMapper.studentLessonsToStudentLessonStatusResponses(studentLessons));
            }

            chapterResponses.add(chapterResponse);
        }

        return chapterResponses;
    }




    private Course findCourseById(Long id){
        return courseRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Course not found with this id: "+id)
        );
    }

    private Student findStudentById(Long id){
        return studentRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Student not found with this id: "+id)
        );
    }

    private CourseEnrollment findCourseEnrollmentById(Long id){
        return courseEnrollmentRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("no course enrollment with this id: " +id)
        );
    }

    private Student findStudentByEmail(String email){
        return studentRepository.findByEmail(email).orElseThrow(
                () -> new RuntimeException("Student not found with this email: "+email)
        );

    }



}
