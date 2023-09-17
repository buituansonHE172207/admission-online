package com.kas.demo_auth.service;



import java.util.List;

import com.kas.demo_auth.dto.UserDTO;
import com.kas.demo_auth.model.Role;
import com.kas.demo_auth.model.User;

public interface UserService{
    User saveUser(UserDTO userDTO);
    User updateUser(UserDTO userDTO, String password);
    Role saveRole(Role role);
    void addRoleToUser(String email, String roleName);
    List<User> findBySchools(Long SchoolId);
    List<User> findAll();
    void deleteUser(Long id);
    User findById(Long id);
}
