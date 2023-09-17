package com.kas.demo_auth.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.kas.demo_auth.dto.UserDTO;
import com.kas.demo_auth.model.Role;
import com.kas.demo_auth.service.AdmissionService;
import com.kas.demo_auth.service.GeneralInformationService;
import com.kas.demo_auth.service.SchoolService;
import com.kas.demo_auth.service.UserService;

@Configuration
public class DataConfig {
        @Bean
        CommandLineRunner run(UserService userService, SchoolService schoolService, AdmissionService admissionService, GeneralInformationService generalInformationService) {
                return args -> {
                        Role adminRole = Role.builder().name("ADMIN").build();
                        Role userRole = Role.builder().name("USER").build();

                        userService.saveRole(adminRole);
                        userService.saveRole(userRole);
                        
                        UserDTO tuanSon = UserDTO.builder()
                                        .firstName("Tuấn")
                                        .lastName("Sơn")
                                        .email("buituanson2003@gmail.com")
                                        .password("12312")
                                        .build();  

   

                        userService.saveUser(tuanSon); 


                        userService.addRoleToUser("buituanson2003@gmail.com", "ADMIN");
                        

                        /* SchoolDTO school1 = SchoolDTO.builder()
                                        .schoolName("Thạch Thất")
                                        .address("Thôn Thái Bình Yên Thạch Thất Hà Nội")
                                        .phoneNumber("0989571978")
                                        .email("thpt@edu.vn")
                                        .admissionCriteria("<strong>Tiêu chí tuyển sinh</strong>")
                                        .build();

                        schoolService.saveSchool(school1);

                        AdmissionDTO admissionDTO = AdmissionDTO
                        .builder()
                        .fullName("Bùi Tuấn Sơn")
                        .ethnicity(Ethnicity.KINH.getDisplayName())
                        .dateOfBirth(LocalDate.of(2003, 03, 24))
                        .address("Binh Yen Thach That Ha Noi")
                        .schoolName("Thạch Thất")
                        .gender(Gender.MALE.getDisplayName())
                        .personalId("001203048312")
                        .build();
                        admissionService.saveAdmission(admissionDTO);

                        GeneralInformation generalInformation = GeneralInformation
                        .builder()
                        .pdfName("quy-dinh-tuyen-sinh.pdf")
                        .openTime(LocalDate.of(2023, 6, 30))
                        .closeTime(LocalDate.of(2023, 7, 01))
                        .email("buituanson2003@gmail.com")
                        .phoneNumber("0989571978")
                        .build();
                        generalInformationService.saveGeneralInformation(generalInformation); */
                };
        }
}
