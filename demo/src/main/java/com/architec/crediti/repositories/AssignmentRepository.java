package com.architec.crediti.repositories;

import com.architec.crediti.models.Assignment;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssignmentRepository extends PagingAndSortingRepository<Assignment, Long> {
    Assignment findByAssignmentId(long assignmentId);
    List<Assignment> findByTitleContainingAndArchived(String title, boolean archived);
}