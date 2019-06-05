package com.architec.crediti.repositories;

import com.architec.crediti.models.Assignment;
import com.architec.crediti.models.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepo extends JpaRepository<Tag, Integer> {
    Tag findBytagId(int tagId);
    boolean existsByTagName(String tagName);
}