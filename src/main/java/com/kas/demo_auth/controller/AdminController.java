package com.kas.demo_auth.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.kas.demo_auth.dto.AdmissionDTO;
import com.kas.demo_auth.dto.AdmissionMapper;
import com.kas.demo_auth.dto.SchoolDTO;
import com.kas.demo_auth.dto.SchoolMapper;
import com.kas.demo_auth.dto.UserDTO;
import com.kas.demo_auth.dto.UserMapper;
import com.kas.demo_auth.exception.CanNotDeleteException;
import com.kas.demo_auth.model.Admission;
import com.kas.demo_auth.model.AdmissionStatus;
import com.kas.demo_auth.model.Ethnicity;
import com.kas.demo_auth.model.Gender;
import com.kas.demo_auth.model.GeneralInformation;
import com.kas.demo_auth.model.User;
import com.kas.demo_auth.service.AdmissionService;
import com.kas.demo_auth.service.FileService;
import com.kas.demo_auth.service.GeneralInformationService;
import com.kas.demo_auth.service.SchoolService;
import com.kas.demo_auth.service.UserService;

import lombok.AllArgsConstructor;

@Controller
@RequestMapping("/admin")
@AllArgsConstructor
public class AdminController {
    @Autowired
    GeneralInformationService generalInformationService;
    SchoolService schoolService;
    UserService userService;
    AdmissionService admissionService;

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

    private final FileService fileService;

    @GetMapping({ "/trang-chu", "" })
    public String getHomePage(Model model) {
        model.addAttribute("schools", schoolService.findAllSchool());
        return "trang-chu-admin";
    }

    @GetMapping(value = "/quan-ly-tuyen-sinh")
    public String quanLyTuyenSinh(Model model) {
        
        return "quan-ly-tuyen-sinh";
    }

    @PostMapping(value = "/quan-ly-tuyen-sinh", consumes = { "multipart/form-data" })
    public String quanLyTuyenSinhPost(@ModelAttribute("generalInformation") GeneralInformation generalInformation,
            @RequestParam("file") MultipartFile file,
            Model model) {
        if (file != null && !file.isEmpty()) {
            try {
                fileService.saveFile(file, generalInformation);
                generalInformationService.saveGeneralInformation(generalInformation);
            } catch (IOException | IllegalArgumentException e) {
                String encodedErrorMessage = URLEncoder.encode("Cập nhật thất bại." + e.getMessage(),
                        StandardCharsets.UTF_8);
                return "redirect:/admin/quan-ly-tuyen-sinh?error=" + encodedErrorMessage;
            }
        }
        String encodedSuccessMessage = URLEncoder.encode("Cập nhật thông tin thành công.", StandardCharsets.UTF_8);
        return "redirect:/admin/quan-ly-tuyen-sinh?success=" + encodedSuccessMessage;
    }

    @GetMapping(value = "/quan-ly-ho-so-tuyen-sinh")
    public String quanLyHoSoTuyenSinh(
            Model model,
            @RequestParam(value = "schoolName", required = false) String schoolName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortField,
            @RequestParam(defaultValue = "asc") String sortDirection) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDirection), sortField));
        model.addAttribute("schools", schoolService.findAllSchool());
        Page<Admission> admissionsPage;
        if (schoolName != null && !schoolName.isEmpty()) {
            SchoolDTO schoolDTO = SchoolMapper.toDTO(schoolService.findSchoolBySchoolName(schoolName));
            model.addAttribute("schoolName", schoolDTO.getSchoolName());
            admissionsPage = admissionService.findAllAdmissionBySchoolID(schoolDTO.getId(), pageable);
        } else {
            admissionsPage = admissionService.findAllAdmission(pageable);
        }
        model.addAttribute("admissions", admissionsPage.getContent()
                .stream()
                .map(AdmissionMapper::toDTO)
                .collect(Collectors.toList()));
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", admissionsPage.getTotalPages());
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDirection", sortDirection);

        return "quan-ly-ho-so-tuyen-sinh";
    }

    @GetMapping("/quan-ly-thong-tin-truong")
    public String quanLyThongTinTruong(
            Model model,
            @RequestParam(value = "schoolName", defaultValue = "") String schoolName) {
        model.addAttribute("schools", schoolService.findAllSchool());
        if (!schoolName.isBlank()) {
            SchoolDTO school = SchoolMapper.toDTO(schoolService.findSchoolBySchoolName(schoolName));
            model.addAttribute("schoolSelected", school);
        }

        return "quan-ly-thong-tin-truong";
    }

    @PostMapping("/quan-ly-thong-tin-truong")
    public String quanLyThongTinTruong(
            @ModelAttribute("schoolSelected") SchoolDTO school,
            Model model) {
        try {
            schoolService.saveSchool(school);
        } catch (DataIntegrityViolationException e) {
            String encodedErrorMessage = URLEncoder.encode("Cập nhật thất bại. Tên trường này đã được sử dụng!",
                    StandardCharsets.UTF_8);
            return "redirect:/admin/quan-ly-thong-tin-truong?error=" + encodedErrorMessage;
        }
        String encodedSuccessMessage = URLEncoder.encode("Cập nhật thành công!", StandardCharsets.UTF_8);
        String encodedSchoolName = URLEncoder.encode(school.getSchoolName(), StandardCharsets.UTF_8);
        return "redirect:/admin/quan-ly-thong-tin-truong?schoolName=" + encodedSchoolName + "&success="
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
        return "xet-duyet-ho-so";
    }

    @PostMapping("/xet-duyet-ho-so")
    public String xetDuyetHoSoPost(Model model,
            @ModelAttribute("admission") AdmissionDTO admissionDTO) {    
        try {
            admissionService.updateAdmission(admissionDTO);
        } catch(DataIntegrityViolationException e) {
            String encodedErrorMessage = URLEncoder.encode("Mã căn cước này đã được sử dụng. Cập nhật thất bại" , StandardCharsets.UTF_8);
            return "redirect:/admin/xet-duyet-ho-so?id=" + admissionDTO.getId() + "&error=" + encodedErrorMessage;
        } 
        String encodedSuccessMessage = URLEncoder.encode("Cập nhật tài khoản thành công!", StandardCharsets.UTF_8);
        return "redirect:/admin/xet-duyet-ho-so?id=" + admissionDTO.getId() +"&success=" + encodedSuccessMessage ;
    }

    @GetMapping("/quan-ly-thong-tin-truong/them-truong")
    public String themTruong(Model model) {
        model.addAttribute("school", new SchoolDTO());
        return "them-truong";
    }

    @PostMapping("/quan-ly-thong-tin-truong/them-truong")
    public String quanLyThongTinTruongPost(
            @ModelAttribute("schoolSelected") SchoolDTO school) {
        try {
            schoolService.saveSchool(school);
        } catch (DataIntegrityViolationException e) {
            String encodedErrorMessage = URLEncoder.encode("Thêm mới thất bại. Tên trường này đã được sử dụng!",
                    StandardCharsets.UTF_8);
            return "redirect:/admin/quan-ly-thong-tin-truong/them-truong?error=" + encodedErrorMessage;
        }
        String encodedSuccessMessage = URLEncoder.encode("Thêm mới thành công!", StandardCharsets.UTF_8);
        String encodedSchoolName = URLEncoder.encode(school.getSchoolName(), StandardCharsets.UTF_8);
        return "redirect:/admin/quan-ly-thong-tin-truong?schoolName=" + encodedSchoolName + "&success="
                + encodedSuccessMessage;
    }

    @GetMapping(value = "/quan-ly-tai-khoan")
    public String quanLyTaiKhoan(Model model,
            @RequestParam(value = "schoolName", required = false) String schoolName) {
        model.addAttribute("schools", schoolService.findAllSchool());
        List<User> users;
        if (schoolName != null && !schoolName.isEmpty()) {
            SchoolDTO schoolDTO = SchoolMapper.toDTO(schoolService.findSchoolBySchoolName(schoolName));
            model.addAttribute("schoolName", schoolDTO.getSchoolName());
            users = userService.findBySchools(schoolDTO.getId());
        } else {
            users = userService.findAll();
        }

        model.addAttribute("accounts", users
                .stream()
                .map(UserMapper::toDTO)
                .collect(Collectors.toList()));
        return "quan-ly-tai-khoan";
    }

    @GetMapping("/quan-ly-tai-khoan/cai-dat-tai-khoan")
    public String caiDatTaiKhoan(@RequestParam(value = "id", required = false) Long id, Model model) {
        User user = userService.findById(id);
        model.addAttribute("schools", schoolService.findAllSchool());
        model.addAttribute("account", UserMapper.toDTO(user));
        return "cai-dat-tai-khoan";
    }

    @PostMapping("/quan-ly-tai-khoan/cai-dat-tai-khoan")
    public String caiDatTaiKhoanPost(
            Model model,
            @ModelAttribute("account") UserDTO userDTO,
            @RequestParam(value = "newPassword", defaultValue = "") String password) {
        try {
            userService.updateUser(userDTO, password);
            
        } catch (DataIntegrityViolationException e) {
            String encodedErrorMessage = URLEncoder.encode("Địa chỉ email này đã đuọc sử dụng. Cập nhật thất bại",
                    StandardCharsets.UTF_8);
            return "redirect:/admin/quan-ly-tai-khoan/cai-dat-tai-khoan?id=" + userDTO.getId() + "&error="
                    + encodedErrorMessage;
        }
        String encodedSuccessMessage = URLEncoder.encode("Cập nhật tài khoản thành công!", StandardCharsets.UTF_8);
        return "redirect:/admin/quan-ly-tai-khoan/cai-dat-tai-khoan?id=" + userDTO.getId() + "&success="
                + encodedSuccessMessage;
    }

    @GetMapping("/quan-ly-tai-khoan/them-tai-khoan")
    public String themTaiKhoan(Model model) {
        model.addAttribute("schools", schoolService.findAllSchool());
        model.addAttribute("account", new UserDTO());
        return "them-tai-khoan";
    }

    @PostMapping("/quan-ly-tai-khoan/them-tai-khoan")
    public String themTaiKhoanPost(
            Model model,
            @ModelAttribute("account") UserDTO userDTO,
            @RequestParam(value = "newPassword", defaultValue = "") String password) {
        try {
            userService.saveUser(userDTO);
        } catch (DataIntegrityViolationException e) {
            String encodedErrorMessage = URLEncoder.encode("Địa chỉ email này đã đuọc sử dụng. Thêm mới thất bại",
                    StandardCharsets.UTF_8);
            return "redirect:/admin/quan-ly-tai-khoan/them-tai-khoan?error=" + encodedErrorMessage;
        }
        String encodedSuccessMessage = URLEncoder.encode("Thêm mới tài khoản thành công!", StandardCharsets.UTF_8);
        return "redirect:/admin/quan-ly-tai-khoan?success=" + encodedSuccessMessage;
    }

    @GetMapping("/quan-ly-thong-tin-truong/xoa-truong")
    public String xoaTruong(
            Model model,
            @RequestParam(value = "schoolId", required = true) Long schoolId) {
        schoolService.deleteSchool(schoolId);
        String encodedSuccessMessage = URLEncoder.encode("Trường đã được xóa thành công!", StandardCharsets.UTF_8); 
        return "redirect:/admin/quan-ly-thong-tin-truong?success=" + encodedSuccessMessage;
    }

    @GetMapping("/quan-ly-ho-so-tuyen-sinh/xoa-ho-so")
    public String xoaHoSo(
            Model model,
            @RequestParam(value = "admissionId", required = true) Long admissionId) {
        admissionService.deleteAdmission(admissionId);
        String encodedSuccessMessage = URLEncoder.encode("Hồ sơ đã được xóa thành công!", StandardCharsets.UTF_8); 
        return "redirect:/admin/quan-ly-ho-so-tuyen-sinh?success=" + encodedSuccessMessage;
    }

    @GetMapping("/quan-ly-tai-khoan/xoa-tai-khoan")
    public String xoaTaiKhoan(
            Model model,
            @RequestParam(value = "accountId", required = true) Long accountId) {
        try {
            userService.deleteUser(accountId);
        } catch (CanNotDeleteException exception) {
            String encodedErrorMessage = URLEncoder.encode(exception.getMessage(), StandardCharsets.UTF_8); 
            return "redirect:/admin/quan-ly-tai-khoan?error=" + encodedErrorMessage;
        }
        String encodedSuccessMessage = URLEncoder.encode("Tài khoản đẫ được xóa thành công!", StandardCharsets.UTF_8); 
        return "redirect:/admin/quan-ly-tai-khoan?success=" + encodedSuccessMessage;
    }

}
