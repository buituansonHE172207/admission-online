package com.kas.demo_auth.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.kas.demo_auth.dto.UserDTO;
import com.kas.demo_auth.dto.UserMapper;
import com.kas.demo_auth.exception.CanNotDeleteException;
import com.kas.demo_auth.model.Role;
import com.kas.demo_auth.model.User;
import com.kas.demo_auth.repository.RoleRepository;
import com.kas.demo_auth.repository.SchoolRepository;
import com.kas.demo_auth.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional

public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final SchoolRepository schoolRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public User saveUser(UserDTO userDTO) throws DataIntegrityViolationException {
        User user = UserMapper.toEntity(userDTO);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User newUser = userRepository.save(user);
        if (userDTO.getRoles() != null)
            userDTO.getRoles().forEach(roleName -> {
                newUser.getRoles().add(roleRepository.findByName(roleName).get());
            });
        if (userDTO.getSchoolName() != null && !userDTO.getSchoolName().isEmpty())
            newUser.getSchools().add(schoolRepository.findBySchoolName(userDTO.getSchoolName()).get());
        return newUser;
    }

    @Override
    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public void addRoleToUser(String email, String roleName) {
        User user = userRepository.findByEmail(email).get();
        Role role = roleRepository.findByName(roleName).get();
        user.getRoles().add(role);
    }

    @Override
    public List<User> findBySchools(Long schoolId) {
        return userRepository.findBySchools(schoolRepository.findById(schoolId).get());
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id).get();
        Role admin = roleRepository.findByName("ADMIN").get();
        if (user.getRoles().contains(admin)) {
            throw new CanNotDeleteException(
                    "Bạn không thể xóa tài khoản admin! Nếu muốn xóa thì bạn cần chuyển tài khoản admin về tài khoản người dùng.");
        }
        userRepository.deleteById(id);
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User updateUser(UserDTO userDTO, String password) {
        User user = UserMapper.toEntity(userDTO);
        if (userDTO.getRoles() != null)
            userDTO.getRoles().forEach(roleName -> {
                user.getRoles().add(roleRepository.findByName(roleName).get());
            });
        if (!userDTO.getSchoolName().isEmpty())
            user.getSchools().add(schoolRepository.findBySchoolName(userDTO.getSchoolName()).get());
        if (!password.isEmpty())
            user.setPassword(passwordEncoder.encode(password));
        return userRepository.save(user);
    }
}
