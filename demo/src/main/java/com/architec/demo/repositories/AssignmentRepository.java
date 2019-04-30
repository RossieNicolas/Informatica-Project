package com.architec.demo.repositories;

import com.architec.demo.models.Assignment;

import com.architec.demo.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    List<Assignment> findByMaxStudents(int maxStudents);
    List<Assignment> findByAssignerUserId(long assignerUserId);
    List<Assignment> findByAssignmentId(long assignmentId);
}
