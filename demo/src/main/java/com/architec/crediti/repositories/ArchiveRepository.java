package com.architec.crediti.repositories;

import com.architec.crediti.models.ArchivedAssignment;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Repository
public interface ArchiveRepository extends PagingAndSortingRepository<ArchivedAssignment, Long> {
    ArchivedAssignment findByAssignmentId(long assignmentId);

    Page<ArchivedAssignment> findAllByOrderByAssignmentIdDesc(Pageable page);

    Page<ArchivedAssignment> findByTitleContainingOrTagNameContainingOrderByAssignmentIdDesc(String name, String tagName, Pageable page);

    Page<ArchivedAssignment> findByAssignmentIdOrderByAssignmentIdDesc(long assignmentId, Pageable page);

    //Used for testing
    List<ArchivedAssignment> findByTitleContaining(String title);
}
