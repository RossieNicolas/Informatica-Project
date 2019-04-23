package com.architec.demo.Repositories;

import org.springframework.data.repository.CrudRepository;
import com.architec.demo.models.User;

public interface userRepository extends CrudRepository<User, Integer> {
    User findByName(String name);
}