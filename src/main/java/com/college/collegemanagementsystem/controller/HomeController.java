package com.college.collegemanagementsystem.controller;

import com.college.collegemanagementsystem.service.CourseService;
import com.college.collegemanagementsystem.service.DepartmentService;
import com.college.collegemanagementsystem.service.StudentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final StudentService studentService;
    private final DepartmentService departmentService;
    private final CourseService courseService;

    public HomeController(StudentService studentService,
            DepartmentService departmentService,
            CourseService courseService) {
        this.studentService = studentService;
        this.departmentService = departmentService;
        this.courseService = courseService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("studentCount", studentService.getAllStudents().size());
        model.addAttribute("departmentCount", departmentService.getAllDepartments().size());
        model.addAttribute("courseCount", courseService.getAllCourses().size());
        return "index";
    }
}
