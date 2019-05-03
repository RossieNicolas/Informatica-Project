package com.architec.demo.repositories;

import com.architec.demo.models.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepo extends JpaRepository<Tag, Integer> {
    List<Tag> findBytagId(int tag_id);
}