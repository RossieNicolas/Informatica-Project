package com.architec.crediti.repositories;


import com.architec.crediti.models.ArchivedAssignment;
import com.architec.crediti.models.Student;
import com.architec.crediti.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    boolean existsByStudentNumber(String studentNumber);

    Student findByUserId(User user);

    Page<Student> findByUserId(User user, Pageable page);

    Student findByStudentNumber(String id);

    boolean existsByUserId(User user);

    Page<Student> findByStudentNumberContainingOrderByStudentId(String studentNr, Pageable page);


    @Query(value = "select * from students s where s.user_id in :users", nativeQuery = true)
    Page<Student> findByUserids(@Param("users") List<Long> users, Pageable pageable);

}

