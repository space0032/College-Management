package com.college.collegemanagementsystem.controller;

import com.college.collegemanagementsystem.entity.GatePass;
import com.college.collegemanagementsystem.entity.User;
import com.college.collegemanagementsystem.service.GatePassService;
import com.college.collegemanagementsystem.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/student/gatepass")
public class StudentGatePassController {

    private final GatePassService gatePassService;
    private final UserService userService;

    public StudentGatePassController(GatePassService gatePassService, UserService userService) {
        this.gatePassService = gatePassService;
        this.userService = userService;
    }

    @GetMapping("/new")
    public String showForm(Model model) {
        model.addAttribute("gatePass", new GatePass());
        return "student/gatepass-form";
    }

    @PostMapping
    public String submitApplication(@Valid @ModelAttribute GatePass gatePass,
            BindingResult result,
            Authentication auth,
            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "student/gatepass-form";
        }

        try {
            User user = userService.findByUsername(auth.getName()).orElseThrow();
            if (user.getStudent() == null) {
                redirectAttributes.addFlashAttribute("errorMessage",
                        "You must be a student to apply for gate pass");
                return "redirect:/student/dashboard";
            }

            // Check if student is a hostelite
            if (!user.getStudent().getIsHostelite() || user.getStudent().getHostel() == null) {
                redirectAttributes.addFlashAttribute("errorMessage",
                        "Only hostel residents can apply for gate pass. Please contact admin if you should have hostel access.");
                return "redirect:/student/dashboard";
            }

            gatePass.setStudent(user.getStudent());
            gatePassService.submitApplication(gatePass);

            redirectAttributes.addFlashAttribute("successMessage",
                    "Gate pass application submitted successfully! Application Number: "
                            + gatePass.getApplicationNumber());
            return "redirect:/student/gatepass/list";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error submitting application: " + e.getMessage());
            return "redirect:/student/gatepass/new";
        }
    }

    @GetMapping("/list")
    public String listApplications(Authentication auth, Model model) {
        User user = userService.findByUsername(auth.getName()).orElseThrow();
        if (user.getStudent() != null) {
            model.addAttribute("gatePasses", gatePassService.getApplicationsByStudent(user.getStudent()));
        } else {
            model.addAttribute("gatePasses", java.util.Collections.emptyList());
        }
        return "student/gatepass-list";
    }

    @GetMapping("/{id}")
    public String viewDetails(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            GatePass gatePass = gatePassService.getGatePassById(id)
                    .orElseThrow(() -> new RuntimeException("Gate pass not found"));
            model.addAttribute("gatePass", gatePass);
            return "student/gatepass-detail";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/student/gatepass/list";
        }
    }
}
