package com.example.enrollingservice.repository;

import com.example.enrollingservice.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student,Long> {
}
