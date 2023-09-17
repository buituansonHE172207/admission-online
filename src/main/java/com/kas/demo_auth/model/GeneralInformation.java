package com.kas.demo_auth.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "general_informations")
public class GeneralInformation {
    @Id
    private Long id;

    private String email;

    private String phoneNumber;

    @Lob
    private String supportContact;

    private String pdfName;

    private LocalDate openTime;

    private LocalDate closeTime;

}
