package com.architec.crediti.repositories;

import com.architec.crediti.models.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepo extends JpaRepository<Tag, Integer> {
    Tag findBytagId(int tag_id);
    boolean existsByTagName(String tag_name);
}