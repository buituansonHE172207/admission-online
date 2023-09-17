package com.kas.demo_auth.controller;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.kas.demo_auth.service.GeneralInformationService;

@Controller
public class LoginController {

    @Autowired
    GeneralInformationService generalInformationService;

    @GetMapping({"/login","/dang-nhap"})
    public String login() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()
                && !authentication.getName().equals("anonymousUser")) {
            return "redirect:/home";
        }
        return "login";
    }

    @GetMapping("/logout")
    public String logout() {
        return "logout";
    }

    @GetMapping("/home")
    public String home(Authentication authentication, Model model) {
        if (authentication == null) {
            return "redirect:/login";
        }
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        if (authorities.contains(new SimpleGrantedAuthority("ADMIN"))) {
            return "redirect:/admin/trang-chu";
        } else if (authorities.contains(new SimpleGrantedAuthority("USER"))) {
            return "redirect:/user/trang-chu";
        }
        model.addAttribute("errorCode", "307");
        model.addAttribute("message", "Tài khoản này chưa được cấp bất kì quyền nào");;
        return "error-page";
    }

}
