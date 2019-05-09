package com.architec.crediti.repositories;


import com.architec.crediti.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    boolean existsByStudentennummer(String studentennummer);
}

