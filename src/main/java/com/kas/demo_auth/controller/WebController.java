package com.kas.demo_auth.controller;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.ZoneId;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kas.demo_auth.dto.AdmissionDTO;
import com.kas.demo_auth.dto.AdmissionMapper;
import com.kas.demo_auth.dto.SchoolDTO;
import com.kas.demo_auth.dto.SchoolMapper;
import com.kas.demo_auth.model.Admission;
import com.kas.demo_auth.model.Ethnicity;
import com.kas.demo_auth.model.Gender;
import com.kas.demo_auth.model.GeneralInformation;
import com.kas.demo_auth.service.AdmissionService;
import com.kas.demo_auth.service.GeneralInformationService;
import com.kas.demo_auth.service.SchoolService;

@Controller
@RequestMapping("/")
public class WebController {

    @Autowired
    SchoolService schoolService;
    @Autowired
    private AdmissionService admissionService;
    @Autowired
    private GeneralInformationService generalInformationService;

    @ModelAttribute("generalInformation")
    public GeneralInformation getGeneralInformation() {
        return generalInformationService.getGeneralInformation();
    }
    
    @GetMapping("")
    public String indexPage() {
        return "index";
    }

    @GetMapping("quy-dinh-tuyen-sinh")
    public String quyDinhTuyenSinh(Model model) {
        model.addAttribute("general", generalInformationService.getGeneralInformation());
        return "quy-dinh-tuyen-sinh";
    }

    @GetMapping({ "dang-ky", "add" })
    public String dangKyForm(Model model) {
        GeneralInformation generalInformation = (GeneralInformation) model.getAttribute("generalInformation");
        ZoneId zone = ZoneId.of("Asia/Ho_Chi_Minh");
        if (generalInformation != null) {

            if (generalInformation.getOpenTime().isAfter(LocalDate.now(zone))) {
                model.addAttribute("message",
                        "Chưa đến thời gian đăng ký. Thời gian đăng ký: " + generalInformation.getOpenTime());
                return "thong-bao-dang-ki";
            } else if (generalInformation.getCloseTime().isBefore(LocalDate.now(zone))) {
                model.addAttribute("message",
                        "Hệ thống đã đóng, đã hết thời hạn đăng ký: " + generalInformation.getOpenTime());
                return "thong-bao-dang-ki";
            }

        }
        model.addAttribute("admissionRequest", new AdmissionDTO());
        model.addAttribute("ethnicities", Ethnicity.values());
        model.addAttribute("schools", schoolService.findAllSchool());
        model.addAttribute("genders", Gender.values());
        return "dang-ky";
    }

    @PostMapping({ "dang-ky", "add" })
    public String xuLiDangKy(@ModelAttribute("admissionRequest") AdmissionDTO admissionRequest, Model model) {
        try {
            admissionService.saveAdmission(admissionRequest);
        } catch (DataIntegrityViolationException e) {
            String message = "Số căn cước công dân này đã được sử dụng";
            String encodedMessage = URLEncoder.encode(message, StandardCharsets.UTF_8);
            return "redirect:/dang-ky?message=" + encodedMessage;
        }
        model.addAttribute("admission", admissionRequest);
        return "dang-ky-thanh-cong";
    }

    @GetMapping("huong-dan-dang-ky")
    public String huongDanDangKy() {
        return "huong-dan-dang-ky";
    }

    @GetMapping("thong-tin-tuyen-sinh")
    public String thongTinTuyenSinh(@RequestParam(value = "schoolName", required = false) String schoolName,
            Model model) {
        if (schoolName != null) {
            SchoolDTO schoolDTO = SchoolMapper.toDTO(schoolService.findSchoolBySchoolName(schoolName));
            model.addAttribute("schoolSelected", schoolDTO);
        }
        model.addAttribute("schools", schoolService.findAllSchool());
        return "thong-tin-tuyen-sinh";
    }

    @GetMapping("tra-cuu-ket-qua")
    public String traCuuKetQua() {
        return "tra-cuu-ket-qua";
    }

    @PostMapping("tra-cuu-ket-qua")
    public String traCuuKetQuaPost(@RequestParam(value = "personalId", required = false) String personalId,
            Model model) {
        if (personalId != null) {
            Admission admission = admissionService.findAdmissionByPersonalId(personalId);
            if (admission != null) {
                model.addAttribute("admission", AdmissionMapper.toDTO(admission));
            } else {
                model.addAttribute("notification", "Không thấy hồ sơ phù hợp với mã số định danh này");
            }
        }
        return "tra-cuu-ket-qua";
    }
}
