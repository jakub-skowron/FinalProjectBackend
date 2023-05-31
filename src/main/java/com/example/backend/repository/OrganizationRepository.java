package com.example.backend.repository;

import com.example.backend.model.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long> {
    boolean existsByName(String string);

}
