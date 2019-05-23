package com.architec.crediti.repositories;

import com.architec.crediti.models.Assignment;

import com.architec.crediti.models.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssignmentRepository extends PagingAndSortingRepository<Assignment, Long> {
    Assignment findByAssignmentId(long assignmentId);
    List<Assignment> findByTitleContainingAndArchived(String title, boolean archived);
    List<Assignment> findByAssignerUserId(User user);
    Page<Assignment> findByTitleContainingAndArchived(String title, boolean archived,Pageable pageable);
    Page<Assignment> findByFullOrderByAssignmentIdDesc(boolean full, Pageable pageable);
}