package com.architec.crediti.repositories;

import com.architec.crediti.models.File;
import com.architec.crediti.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {

    List<File> findByUser(User user);
}
