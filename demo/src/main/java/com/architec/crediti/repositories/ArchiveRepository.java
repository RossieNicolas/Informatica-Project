package com.architec.crediti.repositories;

import com.architec.crediti.models.ArchivedAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ArchiveRepository extends PagingAndSortingRepository<ArchivedAssignment, Long> {
}
