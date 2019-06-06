package com.architec.crediti.repositories;

import com.architec.crediti.models.File;
import com.architec.crediti.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {

    List<File> findByUser(User user);

    List<File> findByUserOrderByAssignmentId(User user);

    List<File> findByDocTypeAndUser(String docType, User user);

    Optional<File> findByFileId(int id);
}
