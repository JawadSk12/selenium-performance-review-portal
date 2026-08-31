package com.review.portal.repository;

import com.review.portal.model.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA Repository for Manager entity management.
 */
@Repository
public interface ManagerRepository extends JpaRepository<Manager, Long> {

    Optional<Manager> findByEmail(String email);

    List<Manager> findByDepartment(String department);

    boolean existsByEmail(String email);
}
