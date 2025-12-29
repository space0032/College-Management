package com.college.collegemanagementsystem.controller;

import com.college.collegemanagementsystem.entity.Student;
import com.college.collegemanagementsystem.service.DepartmentService;
import com.college.collegemanagementsystem.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/students")
public class StudentWebController {

    private final StudentService studentService;
    private final DepartmentService departmentService;

    public StudentWebController(StudentService studentService, DepartmentService departmentService) {
        this.studentService = studentService;
        this.departmentService = departmentService;
    }

    @GetMapping
    public String listStudents(Model model) {
        model.addAttribute("students", studentService.getAllStudents());
        return "students/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("student", new Student());
        model.addAttribute("departments", departmentService.getAllDepartments());
        return "students/form";
    }

    @PostMapping
    public String saveStudent(@Valid @ModelAttribute("student") Student student,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("departments", departmentService.getAllDepartments());
            return "students/form";
        }

        try {
            studentService.saveStudent(student);
            redirectAttributes.addFlashAttribute("successMessage", "Student saved successfully!");
            return "redirect:/students";
        } catch (Exception e) {
            model.addAttribute("departments", departmentService.getAllDepartments());
            model.addAttribute("errorMessage", "Error saving student: " + e.getMessage());
            return "students/form";
        }
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Student student = studentService.getStudentById(id)
                .orElse(new Student());
        model.addAttribute("student", student);
        model.addAttribute("departments", departmentService.getAllDepartments());
        return "students/form";
    }

    @PostMapping("/delete/{id}")
    public String deleteStudent(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            studentService.deleteStudent(id);
            redirectAttributes.addFlashAttribute("successMessage", "Student deleted successfully!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting student: " + e.getMessage());
        }
        return "redirect:/students";
    }
}