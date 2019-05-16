package com.architec.crediti.repositories;


import com.architec.crediti.models.Student;
import com.architec.crediti.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    boolean existsByStudentennummer(String studentennummer);
    Student findByUserId(User userId);
    Student findByStudentId(long studentId);
}

