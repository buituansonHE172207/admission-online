package com.kas.demo_auth.controller;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kas.demo_auth.dto.AdmissionDTO;
import com.kas.demo_auth.dto.AdmissionMapper;
import com.kas.demo_auth.dto.SchoolDTO;
import com.kas.demo_auth.dto.SchoolMapper;
import com.kas.demo_auth.dto.UserDTO;
import com.kas.demo_auth.dto.UserMapper;
import com.kas.demo_auth.exception.ResourceNotFoundException;
import com.kas.demo_auth.model.Admission;
import com.kas.demo_auth.model.AdmissionStatus;
import com.kas.demo_auth.model.Ethnicity;
import com.kas.demo_auth.model.Gender;
import com.kas.demo_auth.model.GeneralInformation;
import com.kas.demo_auth.model.School;
import com.kas.demo_auth.model.User;
import com.kas.demo_auth.service.AdmissionService;
import com.kas.demo_auth.service.GeneralInformationService;
import com.kas.demo_auth.service.SchoolService;
import com.kas.demo_auth.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private SchoolService schoolService;
    @Autowired
    private GeneralInformationService generalInformationService;
    @Autowired
    private AdmissionService admissionService;
    @Autowired
    private UserService userService;

    @ModelAttribute("user")
    public UserDTO getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        return UserMapper.toDTO(user);
    }

    @ModelAttribute("generalInformation")
    public GeneralInformation getGeneralInformation() {
        return generalInformationService.getGeneralInformation();
    }

    @ModelAttribute("school")
    public SchoolDTO getSchool(@ModelAttribute("user") UserDTO user) {
        String schoolName = user.getSchoolName();
        if (schoolName == null || schoolName.isEmpty()) {
            throw new ResourceNotFoundException("Tài khoản này chưa được quản lý bất kì trường nào.");
        }
        School school = schoolService.findSchoolBySchoolName(schoolName);
        return SchoolMapper.toDTO(school);
    }


    @GetMapping("/trang-chu")
    public String trangChu(Model model) {
        return "trangchu-user";
    }

    @GetMapping(value = "/quan-ly-ho-so-tuyen-sinh")
    public String quanLyHoSoTuyenSinh(
            Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortField,
            @RequestParam(defaultValue = "asc") String sortDirection) {

        SchoolDTO school = (SchoolDTO) model.getAttribute("school");
        if (school == null) {
            return "loi-phan-quyen";
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDirection), sortField));
        Page<Admission> admissionsPage;

        admissionsPage = admissionService.findAllAdmissionBySchoolID(school.getId(), pageable);
        model.addAttribute("admissions", admissionsPage.getContent()
                .stream()
                .map(AdmissionMapper::toDTO)
                .collect(Collectors.toList()));
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", admissionsPage.getTotalPages());
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDirection", sortDirection);

        return "quan-ly-ho-so-tuyen-sinh-user";
    }

    @GetMapping("/quan-ly-thong-tin-truong")
    public String quanLyThongTinTruong(
            Model model) {
        return "quan-ly-thong-tin-truong-user";
    }

    @PostMapping("/quan-ly-thong-tin-truong")
    public String quanLyThongTinTruong(
            @ModelAttribute("schoolSelected") SchoolDTO school) {
        try {
            schoolService.saveSchool(school);
        } catch (DataIntegrityViolationException e) {
            String encodedErrorMessage = URLEncoder.encode("Cập nhật thất bại. Tên trường này đã được sử dụng!",
                    StandardCharsets.UTF_8);
            return "redirect:/user/quan-ly-thong-tin-truong?error=" + encodedErrorMessage;
        }
        String encodedSuccessMessage = URLEncoder.encode("Cập nhật thành công!", StandardCharsets.UTF_8);
        String encodedSchoolName = URLEncoder.encode(school.getSchoolName(), StandardCharsets.UTF_8);
        return "redirect:/user/quan-ly-thong-tin-truong?schoolName=" + encodedSchoolName + "&success="
                + encodedSuccessMessage;
    }

    @GetMapping("/xet-duyet-ho-so")
    public String xetDuyetHoSo(@RequestParam(value = "id", required = false) Long id, Model model) {
        Admission admission = admissionService.findAdmissionById(id);
        AdmissionDTO admissionDTO = AdmissionMapper.toDTO(admission);
        model.addAttribute("admission", admissionDTO);
        model.addAttribute("ethnicities", Ethnicity.values());
        model.addAttribute("schools", schoolService.findAllSchool());
        model.addAttribute("statuses", AdmissionStatus.values());
        model.addAttribute("genders", Gender.values());
        model.addAttribute("admissionStatuses", AdmissionStatus.values());
        return "xet-duyet-ho-so-user";
    }

    @PostMapping("/xet-duyet-ho-so")
    public String xetDuyetHoSoPost(Model model,
            @ModelAttribute("admission") AdmissionDTO admissionDTO) {
        try {
            admissionService.updateAdmission(admissionDTO);
        } catch (DataIntegrityViolationException e) {
            String encodedErrorMessage = URLEncoder.encode("Mã căn cước này đã được sử dụng. Cập nhật thất bại",
                    StandardCharsets.UTF_8);
            return "redirect:/user/xet-duyet-ho-so?id=" + admissionDTO.getId() + "&error=" + encodedErrorMessage;
        }
        String encodedSuccessMessage = URLEncoder.encode("Cập nhật tài khoản thành công!", StandardCharsets.UTF_8);
        return "redirect:/user/xet-duyet-ho-so?id=" + admissionDTO.getId() + "&success=" + encodedSuccessMessage;
    }

    @GetMapping("/cai-dat-tai-khoan-ca-nhan")
    public String caiDatTaiKhoanCaNhan() {
        return "cai-dat-tai-khoan-ca-nhan";
    }

    @PostMapping("/cai-dat-tai-khoan-ca-nhan")
    public String caiDatTaiKhoanCaNhanPost(Model model,
            @ModelAttribute("account") UserDTO userDTO,
            @RequestParam(value = "newPassword", defaultValue = "") String password) {
        userService.updateUser(userDTO, password);
        try {
            userService.updateUser(userDTO, password);

        } catch (DataIntegrityViolationException e) {
            String encodedErrorMessage = URLEncoder.encode("Địa chỉ email này đã đuọc sử dụng. Cập nhật thất bại",
                    StandardCharsets.UTF_8);
            return "redirect:/user/cai-dat-tai-khoan-ca-nhan?error="
                    + encodedErrorMessage;
        }
        String encodedSuccessMessage = URLEncoder.encode("Cập nhật tài khoản thành công!", StandardCharsets.UTF_8);
        return "redirect:/user/cai-dat-tai-khoan-ca-nhan?success="
                + encodedSuccessMessage;
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public String handleResourceNotFoundException(ResourceNotFoundException ex, Model model) {
        model.addAttribute("code", "500");
        model.addAttribute("Message", ex.getMessage());
        return "error-page";
    }
}
