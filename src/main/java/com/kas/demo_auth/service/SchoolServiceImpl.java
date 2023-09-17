package com.kas.demo_auth.service;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.kas.demo_auth.dto.SchoolDTO;
import com.kas.demo_auth.dto.SchoolMapper;
import com.kas.demo_auth.model.Admission;
import com.kas.demo_auth.model.School;
import com.kas.demo_auth.model.User;
import com.kas.demo_auth.repository.AdmissionRepository;
import com.kas.demo_auth.repository.SchoolRepository;
import com.kas.demo_auth.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class SchoolServiceImpl implements SchoolService {

    private final SchoolRepository schoolRepository;
    private final UserRepository userRepository;
    private final AdmissionRepository admissionRepository;

    @Override
    public void addManagerToSchool(String email, String schoolName) {
        School school = schoolRepository.findBySchoolName(schoolName).get();
        User user = userRepository.findByEmail(email).get();
        user.getSchools().add(school);
    }

    @Override
    public List<School> findAllSchool() {
        List<School> schools = schoolRepository.findAll();
        schools.forEach(school -> school.setCurrentNumberSubmission(admissionRepository.countBySchool(school)));
        return schoolRepository.findAll();
    }

    @Override
    public School findSchoolBySchoolName(String schoolName) {
        School school = schoolRepository.findBySchoolName(schoolName).orElse(null);
        school.setCurrentNumberSubmission(admissionRepository.countBySchool(school));
        return school;
    }

    @Override
    public void deleteSchool(Long schoolId) {
        School school = schoolRepository.findById(schoolId).get();
        if (school != null) {
            school.getAdmissions().forEach(admission -> admissionRepository.deleteById(admission.getId()));
            school.getUsers().forEach(user -> userRepository.deleteById(user.getId()));
            schoolRepository.delete(school);
        }
        
    }

    @Override
    public School saveSchool(SchoolDTO schoolDTO) throws DataIntegrityViolationException {
        School school = SchoolMapper.toEntity(schoolDTO);
        return schoolRepository.save(school);
    }

    @Override
    public School updateSchool(SchoolDTO schoolDTO) {
        School school = SchoolMapper.toEntity(schoolDTO);
        if (schoolDTO.getAdmissionIds() != null && !schoolDTO.getAdmissionIds().isEmpty()) {
            List<Admission> admissions = admissionRepository.findAllById(schoolDTO.getAdmissionIds());
            school.setAdmissions(admissions);
        }
        if (schoolDTO.getUserIds() != null && !schoolDTO.getUserIds().isEmpty()) {
            List<User> users = userRepository.findAllById(schoolDTO.getUserIds());
            school.setUsers(users);
        }
        return schoolRepository.save(school);
    }

    @Override
    public School findSchoolById(Long id) {
        School school = schoolRepository.findById(id).get();
        school.setCurrentNumberSubmission(admissionRepository.countBySchool(school));
        return school;
    }
}
