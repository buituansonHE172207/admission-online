package com.kas.demo_auth.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kas.demo_auth.model.User;
import com.kas.demo_auth.model.School;


public interface UserRepository extends JpaRepository<User, Long> {
     Optional<User> findByEmail(String email);
     List<User> findBySchools(School school);
}
