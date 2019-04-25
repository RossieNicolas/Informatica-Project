package com.architec.demo.Repositories;

import com.architec.demo.models.Fiche;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssignmentRepository extends JpaRepository<Fiche, Long> {
}
