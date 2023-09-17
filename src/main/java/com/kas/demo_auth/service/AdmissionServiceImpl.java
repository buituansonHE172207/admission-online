package com.kas.demo_auth.service;

import java.time.LocalDateTime;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.kas.demo_auth.dto.AdmissionDTO;
import com.kas.demo_auth.dto.AdmissionMapper;
import com.kas.demo_auth.model.Admission;
import com.kas.demo_auth.model.AdmissionStatus;
import com.kas.demo_auth.model.School;
import com.kas.demo_auth.repository.AdmissionRepository;
import com.kas.demo_auth.repository.SchoolRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AdmissionServiceImpl implements AdmissionService {
    private final SchoolRepository schoolRepository;
    private final AdmissionRepository admissionRepository;

    @Override
    public Admission findAdmissionById(Long id) {
        return admissionRepository.findById(id).get();
    }

    @Override
    public Admission findAdmissionByPersonalId(String personalId) {
        return admissionRepository.findByPersonalId(personalId).orElse(null);
    }

    @Override
    public Admission saveAdmission(AdmissionDTO admissionDTO) throws DataIntegrityViolationException {    
        Admission admission = AdmissionMapper.toEntity(admissionDTO);
        setSchool(admission, admissionDTO.getSchoolName());
        admission.setAdmissionDate(LocalDateTime.now());
        admission.setStatus(AdmissionStatus.PENDING.getDisplayName());
        return admissionRepository.save(admission);
    }

    @Override
    public Page<Admission> findAllAdmissionBySchoolID(Long schoolId, Pageable pageable) {
        School school = schoolRepository.findById(schoolId).get();
        return admissionRepository.findBySchool(school, pageable);
    }

    @Override
    public void deleteAdmission(Long admissionId) {
        admissionRepository.deleteById(admissionId);
    }

    @Override
    public Admission updateAdmission(AdmissionDTO admissionDTO) throws DataIntegrityViolationException {   
        Admission admission = AdmissionMapper.toEntity(admissionDTO);
        setSchool(admission, admissionDTO.getSchoolName());
        return admissionRepository.save(admission);
    }

    private void setSchool(Admission admission, String schoolName) {
        School school = schoolRepository.findBySchoolName(schoolName).get();
        admission.setSchool(school);
    }
    
    @Override
    public Page<Admission> findAllAdmission(Pageable pageable) {
        return admissionRepository.findAll(pageable);
    }

    @Override
    public Long countAdmissionBySchool(School school) {
        return admissionRepository.countBySchool(school);
    }
    
}
