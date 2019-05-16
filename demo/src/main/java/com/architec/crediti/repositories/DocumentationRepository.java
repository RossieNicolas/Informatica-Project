package com.architec.crediti.repositories;

import com.architec.crediti.models.Documentation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DocumentationRepository extends JpaRepository<Documentation, Long> {
    Documentation findByTitle(String name);

    Optional<Documentation> findByDocumentId(int id);
}
