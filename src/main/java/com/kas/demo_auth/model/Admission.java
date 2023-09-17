package com.kas.demo_auth.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@Data
@Entity
@NoArgsConstructor
@Table(name = "admissions",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"personalId"})})
public class Admission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;

    private LocalDate dateOfBirth;

    private String address;

    @Column(unique = true)
    private String personalId;

    @ManyToOne
    @JoinColumn(name = "school_id")
    private School school;

    private String ethnicity;

    private String placeOfBirth;

    private String gender;

    private String phoneNumber;

    private LocalDateTime admissionDate;

    private String status;

    private String note;
}
