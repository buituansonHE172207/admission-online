package com.kas.demo_auth.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.kas.demo_auth.model.Admission;
import com.kas.demo_auth.model.School;



public interface AdmissionRepository extends JpaRepository<Admission, Long> {
    Optional<Admission> findByPersonalId(String personalId);
    Page<Admission> findBySchool(School school, Pageable pageable);
    Long countBySchool(School school);
    List<Admission> findByStatus(String status);
    
}
