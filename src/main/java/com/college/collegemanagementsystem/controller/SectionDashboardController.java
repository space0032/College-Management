package com.college.collegemanagementsystem.controller;

import com.college.collegemanagementsystem.entity.User;
import com.college.collegemanagementsystem.service.CourseService;
import com.college.collegemanagementsystem.service.DepartmentService;
import com.college.collegemanagementsystem.service.StudentService;
import com.college.collegemanagementsystem.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/section")
public class SectionDashboardController {

    private final UserService userService;
    private final StudentService studentService;
    private final DepartmentService departmentService;
    private final CourseService courseService;
    private final com.college.collegemanagementsystem.service.GatePassService gatePassService;

    public SectionDashboardController(UserService userService, StudentService studentService,
            DepartmentService departmentService, CourseService courseService,
            com.college.collegemanagementsystem.service.GatePassService gatePassService) {
        this.userService = userService;
        this.studentService = studentService;
        this.departmentService = departmentService;
        this.courseService = courseService;
        this.gatePassService = gatePassService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication auth, Model model) {
        User user = userService.findByUsername(auth.getName()).orElseThrow();

        model.addAttribute("user", user);
        model.addAttribute("studentCount", studentService.getAllStudents().size());
        model.addAttribute("departmentCount", departmentService.getAllDepartments().size());
        model.addAttribute("courseCount", courseService.getAllCourses().size());
        model.addAttribute("pendingCount", gatePassService.getPendingForSection().size());

        return "section/dashboard";
    }

    @GetMapping("/gatepass/pending")
    public String pendingApplications(Model model) {
        model.addAttribute("gatePasses", gatePassService.getPendingForSection());
        return "section/pending-list";
    }

    @GetMapping("/gatepass/{id}")
    public String viewApplication(@org.springframework.web.bind.annotation.PathVariable Long id, Model model,
            org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
        try {
            model.addAttribute("gatePass", gatePassService.getGatePassById(id).orElseThrow());
            return "section/gatepass-detail";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/section/gatepass/pending";
        }
    }

    @org.springframework.web.bind.annotation.PostMapping("/gatepass/{id}/approve")
    public String approveApplication(@org.springframework.web.bind.annotation.PathVariable Long id,
            @org.springframework.web.bind.annotation.RequestParam(required = false) String remarks,
            org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
        try {
            gatePassService.approveBySection(id, remarks);
            redirectAttributes.addFlashAttribute("successMessage", "Application approved successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
        }
        return "redirect:/section/gatepass/pending";
    }

    @org.springframework.web.bind.annotation.PostMapping("/gatepass/{id}/reject")
    public String rejectApplication(@org.springframework.web.bind.annotation.PathVariable Long id,
            @org.springframework.web.bind.annotation.RequestParam String reason,
            org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
        try {
            gatePassService.rejectBySection(id, reason);
            redirectAttributes.addFlashAttribute("successMessage", "Application rejected.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
        }
        return "redirect:/section/gatepass/pending";
    }
}
