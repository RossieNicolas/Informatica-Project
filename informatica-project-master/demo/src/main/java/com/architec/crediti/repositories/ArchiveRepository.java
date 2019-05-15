package com.architec.crediti.repositories;

import com.architec.crediti.models.ArchivedAssignment;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface ArchiveRepository extends PagingAndSortingRepository<ArchivedAssignment, Long> {
    ArchivedAssignment findByAssignmentId(long assignmentId);
    List<ArchivedAssignment> findByTitleContaining(String name);
}
