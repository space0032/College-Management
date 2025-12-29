package com.college.collegemanagementsystem.controller;

import com.college.collegemanagementsystem.entity.User;
import com.college.collegemanagementsystem.entity.UserRole;
import com.college.collegemanagementsystem.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private final UserService userService;

    public DashboardController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication) {
        // Redirect to appropriate dashboard based on role
        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_STUDENT"))) {
            return "redirect:/student/dashboard";
        } else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_WARDEN"))) {
            return "redirect:/warden/dashboard";
        } else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_STUDENT_SECTION"))) {
            return "redirect:/section/dashboard";
        } else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_SECURITY"))) {
            return "redirect:/security-gate/dashboard";
        }

        return "redirect:/";
    }
}
