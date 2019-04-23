package com.architec.demo;

import org.springframework.data.repository.CrudRepository;
import com.architec.demo.models.User;

public interface Repository extends CrudRepository<User, Integer> {
    User findByName(String name);
}