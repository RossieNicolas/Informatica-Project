package com.architec.crediti.repositories;

import com.architec.crediti.models.ArchivedAssignment;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArchiveRepository extends PagingAndSortingRepository<ArchivedAssignment, Long> {
    ArchivedAssignment findByAssignmentId(long assignmentId);
    List<ArchivedAssignment> findByTitleContaining(String name);
}
