package com.example.enrollingservice.repository;

import com.example.enrollingservice.model.CourseNotes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseNotesRepository extends JpaRepository<CourseNotes,Long> {
}
