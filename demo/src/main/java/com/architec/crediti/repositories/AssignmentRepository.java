package com.architec.crediti.repositories;

import com.architec.crediti.models.Assignment;

import com.architec.crediti.models.Tag;
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
    List<Assignment> findByTags(Tag tag);
    Page<Assignment> findAllByOrderByAssignmentIdDesc(Pageable pageable);
    Page<Assignment> findByTitleContainingAndFullOrderByAssignmentIdDesc(String title, boolean full,Pageable pageable);
    Page<Assignment> findByFullOrderByAssignmentIdDesc(boolean full, Pageable pageable);
    List<Assignment> findByTitleContainingAndFullOrderByAssignmentIdDesc(String title, boolean full);
    Page<Assignment> findByTitleContainingAndFullAndTypeEqualsOrderByAssignmentIdDesc(String title, boolean full,Pageable pageable, String type);
    List<Assignment> findByTitleContainingAndArchived(String name, boolean archived);
    int countByValidated(boolean validated);

    @Query(value ="select * from assignments a where a.assignment_id in :tagId",nativeQuery=true)
    Page<Assignment> findByTagsOrderByAssignmentIdDesc(@Param("tagId") List<Long> tagId, Pageable pageable );

}