package com.architec.crediti.repositories;

import com.architec.crediti.models.User;

import com.architec.crediti.security.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    User findByEmail(String email);

    User findByUserId (long userId);

    List<User> findAllByRole(Role role);
}
