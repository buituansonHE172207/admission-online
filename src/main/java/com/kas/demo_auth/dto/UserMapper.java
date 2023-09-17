package com.kas.demo_auth.dto;

import java.util.ArrayList;
import java.util.stream.Collectors;

import com.kas.demo_auth.model.Role;
import com.kas.demo_auth.model.School;
import com.kas.demo_auth.model.User;

public class UserMapper {
        public static UserDTO toDTO(User user) {
                return UserDTO.builder()
                                .id(user.getId())
                                .firstName(user.getFirstName())
                                .lastName(user.getLastName())
                                .email(user.getEmail())
                                .roles(user.getRoles().stream()
                                                .map(role -> role.getName())
                                                .collect(Collectors.toList()))
                                .schoolName(user.getSchools().stream()
                                                .map(school -> school.getSchoolName())
                                                .findFirst()
                                                .orElse(""))
                                .password(user.getPassword())
                                .build();
        }

        public static User toEntity(UserDTO userDTO) {
                User user = User
                                .builder()
                                .id(userDTO.getId())
                                .firstName(userDTO.getFirstName())
                                .lastName(userDTO.getLastName())
                                .email(userDTO.getEmail())
                                .password(userDTO.getPassword())
                                .build();
                user.setSchools(new ArrayList<School>());
                user.setRoles(new ArrayList<Role>());
                return user;
        }
}
