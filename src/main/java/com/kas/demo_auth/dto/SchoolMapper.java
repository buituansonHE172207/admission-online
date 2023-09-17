package com.kas.demo_auth.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.kas.demo_auth.model.Admission;
import com.kas.demo_auth.model.School;
import com.kas.demo_auth.model.User;

public class SchoolMapper {
    public static SchoolDTO toDTO(School school) {
        List<Long> userIds = school.getUsers().stream()
                .map(User::getId)
                .collect(Collectors.toList());

        List<Long> admissionIds = school.getAdmissions().stream()
                .map(Admission::getId)
                .collect(Collectors.toList());

        return SchoolDTO.builder()
                .id(school.getId())
                .schoolName(school.getSchoolName())
                .address(school.getAddress())
                .phoneNumber(school.getPhoneNumber())
                .email(school.getEmail())
                .currentNumberSubmission(school.getCurrentNumberSubmission())
                .requiredNumberSubmission(school.getRequiredNumberSubmission())
                .admissionCriteria(school.getAdmissionCriteria())
                .userIds(userIds)
                .admissionIds(admissionIds)
                .build();
    }

    public static School toEntity(SchoolDTO schoolDTO) {
        School school = School.builder()
                .id(schoolDTO.getId())
                .schoolName(schoolDTO.getSchoolName())
                .address(schoolDTO.getAddress())
                .phoneNumber(schoolDTO.getPhoneNumber())
                .email(schoolDTO.getEmail())
                .currentNumberSubmission(schoolDTO.getCurrentNumberSubmission())
                .requiredNumberSubmission(schoolDTO.getRequiredNumberSubmission())
                .admissionCriteria(schoolDTO.getAdmissionCriteria())
                .build();

        school.setUsers(new ArrayList<>());
        school.setAdmissions(new ArrayList<>());
        return school;
    }
}
