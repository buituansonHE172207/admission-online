package com.kas.demo_auth.model;

import java.util.Collection;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "schools",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"schoolName"})})
public class School {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String schoolName;

  private String address;

  private String phoneNumber;

  private String email;

  @Lob
  private String admissionCriteria;
  
  @Transient
  private Long currentNumberSubmission;

  private Long requiredNumberSubmission;

  @OneToMany(mappedBy = "school")
  private List<Admission> admissions;
  @ManyToMany(mappedBy = "schools")
  private Collection<User> users;
}
