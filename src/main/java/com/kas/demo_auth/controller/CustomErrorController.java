package com.kas.demo_auth.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController implements ErrorController {
    @RequestMapping("/error")
public String handleError(Model model, HttpServletRequest request) {
    Integer statusCode = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
    String errorMessage = (String) request.getAttribute(RequestDispatcher.ERROR_MESSAGE);

    if (statusCode != null) {
        if (statusCode == HttpStatus.NOT_FOUND.value()) {
            model.addAttribute("errorCode", "404");
            model.addAttribute("message", "Không tìm thấy trang bạn yêu cầu");
        } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
            model.addAttribute("errorCode", "500");
            model.addAttribute("message", "Lỗi máy chủ nội bộ");
        } else {
            model.addAttribute("errorCode", statusCode.toString());
            model.addAttribute("message", errorMessage);
        }
    } else {
        model.addAttribute("errorCode", "Unknown");
        model.addAttribute("message", "Lỗi không xác định");
    }

    return "error-page";
}
    
    
}