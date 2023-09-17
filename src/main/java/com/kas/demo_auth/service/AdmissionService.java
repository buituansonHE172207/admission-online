package com.kas.demo_auth.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.kas.demo_auth.dto.AdmissionDTO;
import com.kas.demo_auth.model.Admission;
import com.kas.demo_auth.model.School;

public interface AdmissionService {
    Admission findAdmissionById(Long id);
    Admission findAdmissionByPersonalId(String personalId);
    Admission saveAdmission(AdmissionDTO admissionRequest);
    Admission updateAdmission(AdmissionDTO admissionDTO);
    void deleteAdmission(Long admissionId);
    Page<Admission> findAllAdmissionBySchoolID(Long schoolId, Pageable pageable);
    Page<Admission> findAllAdmission(Pageable pageable);
    Long countAdmissionBySchool(School school);
}
