package com.architec.crediti.repositories;

import com.architec.crediti.models.ExternalUser;

import com.architec.crediti.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExternalUserRepository extends JpaRepository<ExternalUser, Long> {

    ExternalUser findByUserId(User user);

}
