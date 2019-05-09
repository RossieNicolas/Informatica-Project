package com.architec.crediti.repositories;

import com.architec.crediti.models.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepo extends JpaRepository<Tag, Integer> {
    Tag findBytagId(int tag_id);

    Tag findBytagDescription(String tag_descr);

    Tag findBytagName(String tag_name);

    boolean existsBytagName(String tagName);

}