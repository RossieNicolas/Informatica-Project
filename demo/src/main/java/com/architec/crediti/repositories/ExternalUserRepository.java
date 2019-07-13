package com.architec.crediti.repositories;

import com.architec.crediti.models.ExternalUser;

import com.architec.crediti.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExternalUserRepository extends JpaRepository<ExternalUser, Long> {

    ExternalUser findByUserId(User user);

    ExternalUser findByResettoken(String resetToken);

    List<User> findByFirstnameContainingOrLastnameContaining(String firstname, String lastname);

    @Query(value = "select * from external_users s where s.user_id in :users", nativeQuery = true)
    Page<ExternalUser> findByUserids(@Param("users") List<Long> users, Pageable pageable);

    Page<ExternalUser> findByUserIdContainingOrderByUserId(String userId, Pageable page);

    List<ExternalUser> findByApproved(boolean bln);

    boolean existsByUserId(User user);

    int countByApproved(boolean approved);
}
