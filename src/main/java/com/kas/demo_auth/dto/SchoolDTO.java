package com.kas.demo_auth.dto;

import java.util.Collection;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SchoolDTO {
    private Long id;
    private String schoolName;
    private String address;
    private String phoneNumber;
    private String email;
    private Long currentNumberSubmission;
    private Long requiredNumberSubmission;
    private String admissionCriteria;
    private Collection<Long> userIds;
    private Collection<Long> admissionIds;
}
