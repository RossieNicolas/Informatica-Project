package com.architec.demo.repositories;

import com.architec.demo.models.ExternalUser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExternalUserRepository extends JpaRepository<ExternalUser, Long> {
    boolean existsByCompany(String companyName);
}
