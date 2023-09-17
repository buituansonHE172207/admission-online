package com.kas.demo_auth.service;



import java.util.List;

import com.kas.demo_auth.dto.SchoolDTO;
import com.kas.demo_auth.model.School;

public interface SchoolService {
    School saveSchool(SchoolDTO schoolDTO);
    School updateSchool(SchoolDTO schoolDTO);
    void deleteSchool(Long schoolId);
    void addManagerToSchool(String email, String schoolName);
    List<School> findAllSchool();
    School findSchoolBySchoolName(String schoolName);
    School findSchoolById(Long id);
}
