package com.college.collegemanagementsystem.controller;

import com.college.collegemanagementsystem.entity.Course;
import com.college.collegemanagementsystem.service.CourseService;
import com.college.collegemanagementsystem.service.DepartmentService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/courses")
public class CourseWebController {

    private final CourseService courseService;
    private final DepartmentService departmentService;

    public CourseWebController(CourseService courseService, DepartmentService departmentService) {
        this.courseService = courseService;
        this.departmentService = departmentService;
    }

    @GetMapping
    public String listCourses(Model model) {
        model.addAttribute("courses", courseService.getAllCourses());
        return "courses/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("course", new Course());
        model.addAttribute("departments", departmentService.getAllDepartments());
        return "courses/form";
    }

    @PostMapping
    public String saveCourse(@Valid @ModelAttribute("course") Course course,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("departments", departmentService.getAllDepartments());
            return "courses/form";
        }

        try {
            courseService.saveCourse(course);
            redirectAttributes.addFlashAttribute("successMessage", "Course saved successfully!");
            return "redirect:/courses";
        } catch (Exception e) {
            model.addAttribute("departments", departmentService.getAllDepartments());
            model.addAttribute("errorMessage", "Error saving course: " + e.getMessage());
            return "courses/form";
        }
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Course course = courseService.getCourseById(id)
                .orElse(new Course());
        model.addAttribute("course", course);
        model.addAttribute("departments", departmentService.getAllDepartments());
        return "courses/form";
    }

    @PostMapping("/delete/{id}")
    public String deleteCourse(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            courseService.deleteCourse(id);
            redirectAttributes.addFlashAttribute("successMessage", "Course deleted successfully!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting course: " + e.getMessage());
        }
        return "redirect:/courses";
    }
}
