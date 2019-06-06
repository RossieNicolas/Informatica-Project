package com.architec.crediti.repositories;

import com.architec.crediti.models.Enrolled;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnrolledRepository extends JpaRepository<Enrolled, Long> {
    Enrolled findByEnrolledId(long id);

    int countAllByEnrolledIdNotNull();
}
