package com.kas.demo_auth.dto;
import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdmissionDTO {
    private Long id;
    private String fullName;
    private LocalDate dateOfBirth;
    private String address;
    private String personalId;
    private String schoolName;
    private String ethnicity;
    private String placeOfBirth;
    private String phoneNumber;
    private String gender;
    private LocalDateTime admissionDate;
    private String status;
    private String note;
   
}
