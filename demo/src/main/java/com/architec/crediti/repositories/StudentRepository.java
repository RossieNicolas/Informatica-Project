package com.architec.crediti.repositories;


import com.architec.crediti.models.Student;
import com.architec.crediti.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    boolean existsByStudentNumber(String studentNumber);
    Student findByUserId(User user);
    Student findByStudentNumber(String id);
}

