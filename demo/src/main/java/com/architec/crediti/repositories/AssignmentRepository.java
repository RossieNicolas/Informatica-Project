package com.architec.crediti.repositories;

import com.architec.crediti.models.Assignment;

import com.architec.crediti.models.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssignmentRepository extends PagingAndSortingRepository<Assignment, Long> {
    Assignment findByAssignmentId(long assignmentId);

    List<Assignment> findByAssignerUserId(User user);

    Page<Assignment> findByTitleContainingAndFullOrderByAssignmentIdDesc(String title, boolean full, Pageable pageable);

    Page<Assignment> findByValidatedAndTitleContainingAndFullOrderByAssignmentIdDesc(Boolean validated, String title, boolean full, Pageable pageable);

    Page<Assignment> findByFullAndTypeEqualsOrderByAssignmentIdDesc(boolean full, Pageable pageable, String type);

    Page<Assignment> findByValidatedAndFullAndTypeEqualsOrderByAssignmentIdDesc(boolean validated, boolean full, Pageable pageable, String type);

    Page<Assignment> findByFullOrderByAssignmentIdDesc(boolean full, Pageable pageable);

    Page<Assignment> findByValidatedAndFullOrderByAssignmentIdDesc(boolean validated, boolean full, Pageable pageable);

    List<Assignment> findByValidatedAndTitleContainingAndFullOrderByAssignmentIdDesc(boolean validated, String title, boolean full);

    List<Assignment> findByTitleContainingAndFullOrderByAssignmentIdDesc(String title, boolean full);

    List<Assignment> findByTitleContainingAndArchived(String name, boolean archived);

    int countByValidated(boolean validated);

    @Query(value = "select * from assignments a where a.assignment_id in :tagId and validated =:validated", nativeQuery = true)
    Page<Assignment> findByValidatedAndTagsOrderByAssignmentIdDesc(@Param("tagId") List<Long> tagId, Pageable pageable, @Param("validated") boolean validated);

    @Query(value = "select * from assignments a where a.assignment_id in :tagId", nativeQuery = true)
    Page<Assignment> findByTagsOrderByAssignmentIdDesc(@Param("tagId") List<Long> tagId, Pageable pageable);

    @Query(value = "select * from assignments a where a.assignment_id in :tagId and a.type = :type and validated =:validated", nativeQuery = true)
    Page<Assignment> findByValidatedAndTagsAndTypeEqualsOrderByAssignmentIdDesc(@Param("tagId") List<Long> tagId, Pageable pageable, @Param("type") String type, @Param("validated") boolean validated);


    @Query(value = "select * from assignments a where a.amount_students_full = 1 and a.assignment_id in :tagId", nativeQuery = true)
    Page<Assignment> findByTagsAndFullOrderByAssignmentIdDesc(@Param("tagId") List<Long> tagId, Pageable pageable);

}