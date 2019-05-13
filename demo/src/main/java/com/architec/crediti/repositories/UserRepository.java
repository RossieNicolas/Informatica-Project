package com.architec.crediti.repositories;

import java.util.ArrayList;

import com.architec.crediti.models.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
    User findByEmail(String email);
    ArrayList<User> findByFirstnameContainingOrLastnameContaining(String firstname, String lastname);
}
