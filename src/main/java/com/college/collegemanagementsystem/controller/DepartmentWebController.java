package com.college.collegemanagementsystem.controller;

import com.college.collegemanagementsystem.entity.Department;
import com.college.collegemanagementsystem.service.DepartmentService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/departments")
public class DepartmentWebController {

    private final DepartmentService departmentService;

    public DepartmentWebController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping
    public String listDepartments(Model model) {
        model.addAttribute("departments", departmentService.getAllDepartments());
        return "departments/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("department", new Department());
        return "departments/form";
    }

    @PostMapping
    public String saveDepartment(@Valid @ModelAttribute("department") Department department,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "departments/form";
        }

        try {
            departmentService.saveDepartment(department);
            redirectAttributes.addFlashAttribute("successMessage", "Department saved successfully!");
            return "redirect:/departments";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error saving department: " + e.getMessage());
            return "departments/form";
        }
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Department department = departmentService.getDepartmentById(id)
                .orElse(new Department());
        model.addAttribute("department", department);
        return "departments/form";
    }

    @PostMapping("/delete/{id}")
    public String deleteDepartment(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            departmentService.deleteDepartment(id);
            redirectAttributes.addFlashAttribute("successMessage", "Department deleted successfully!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting department: " + e.getMessage());
        }
        return "redirect:/departments";
    }
}
