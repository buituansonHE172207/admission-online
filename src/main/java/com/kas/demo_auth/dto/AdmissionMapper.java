package com.kas.demo_auth.dto;

import com.kas.demo_auth.model.Admission;

public class AdmissionMapper {

    public static AdmissionDTO toDTO(Admission admission) {
        return AdmissionDTO.builder()
                .id(admission.getId())
                .fullName(admission.getFullName())
                .dateOfBirth(admission.getDateOfBirth())
                .address(admission.getAddress())
                .personalId(admission.getPersonalId())
                .schoolName(admission.getSchool().getSchoolName())
                .ethnicity(admission.getEthnicity())
                .placeOfBirth(admission.getPlaceOfBirth())
                .phoneNumber(admission.getPhoneNumber())
                .gender(admission.getGender())
                .admissionDate(admission.getAdmissionDate())
                .status(admission.getStatus())
                .note(admission.getNote())
                .build();
    }

    public static Admission toEntity(AdmissionDTO dto) {
        return Admission.builder()
                .id(dto.getId())
                .fullName(dto.getFullName())
                .dateOfBirth(dto.getDateOfBirth())
                .address(dto.getAddress())
                .personalId(dto.getPersonalId())
                .ethnicity(dto.getEthnicity())
                .placeOfBirth(dto.getPlaceOfBirth())
                .phoneNumber(dto.getPhoneNumber())
                .gender(dto.getGender())
                .admissionDate(dto.getAdmissionDate())
                .status(dto.getStatus())
                .note(dto.getNote())
                .build();
    }

}
