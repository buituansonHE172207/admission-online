package com.kas.demo_auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kas.demo_auth.model.GeneralInformation;

public interface GeneralInformationRepository extends JpaRepository<GeneralInformation, Long> {
    
}
