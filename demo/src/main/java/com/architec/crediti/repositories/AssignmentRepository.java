package com.architec.crediti.repositories;

import com.architec.crediti.models.Assignment;

import com.architec.crediti.models.Tag;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface AssignmentRepository extends PagingAndSortingRepository<Assignment, Long> {
    List<Assignment> findByMaxStudents(int maxStudents);
    List<Assignment> findByAssignerUserId(long assignerUserId);
    Assignment findByAssignmentId(long assignmentId);
    List<Assignment> findByTitleContainingAndArchived(String title, boolean archived);
}
