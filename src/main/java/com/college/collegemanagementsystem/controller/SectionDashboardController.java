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

    public SectionDashboardController(UserService userService, StudentService studentService,
            DepartmentService departmentService, CourseService courseService) {
        this.userService = userService;
        this.studentService = studentService;
        this.departmentService = departmentService;
        this.courseService = courseService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication auth, Model model) {
        User user = userService.findByUsername(auth.getName()).orElseThrow();

        model.addAttribute("user", user);
        model.addAttribute("studentCount", studentService.getAllStudents().size());
        model.addAttribute("departmentCount", departmentService.getAllDepartments().size());
        model.addAttribute("courseCount", courseService.getAllCourses().size());

        return "section/dashboard";
    }
}
