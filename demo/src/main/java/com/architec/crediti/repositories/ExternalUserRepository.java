package com.architec.crediti.repositories;

import com.architec.crediti.models.ExternalUser;

import com.architec.crediti.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExternalUserRepository extends JpaRepository<ExternalUser, Long> {

    ExternalUser findByUserId(User user);
    ExternalUser findByResettoken(String resetToken);
    boolean existsByUserId(User user);
    int countByApproved(boolean approved);
}
