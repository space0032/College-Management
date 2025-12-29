package com.college.collegemanagementsystem.controller;

import com.college.collegemanagementsystem.entity.User;
import com.college.collegemanagementsystem.service.GatePassService;
import com.college.collegemanagementsystem.service.HostelService;
import com.college.collegemanagementsystem.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/warden")
public class WardenDashboardController {

    private final UserService userService;
    private final GatePassService gatePassService;
    private final HostelService hostelService;

    public WardenDashboardController(UserService userService, GatePassService gatePassService,
            HostelService hostelService) {
        this.userService = userService;
        this.gatePassService = gatePassService;
        this.hostelService = hostelService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication auth, Model model) {
        User user = userService.findByUsername(auth.getName()).orElseThrow();
        model.addAttribute("user", user);

        if (user.getHostel() != null) {
            long pendingCount = gatePassService.getPendingForWarden(user.getHostel()).size();
            model.addAttribute("pendingCount", pendingCount);
            model.addAttribute("hostel", user.getHostel());
        } else {
            model.addAttribute("pendingCount", 0);
        }

        return "warden/dashboard";
    }

    @GetMapping("/gatepass/pending")
    public String pendingApplications(Authentication auth, Model model) {
        User user = userService.findByUsername(auth.getName()).orElseThrow();

        if (user.getHostel() != null) {
            model.addAttribute("gatePasses", gatePassService.getPendingForWarden(user.getHostel()));
            model.addAttribute("hostel", user.getHostel());
        } else {
            model.addAttribute("gatePasses", java.util.Collections.emptyList());
        }

        return "warden/pending-list";
    }

    @GetMapping("/gatepass/{id}")
    public String viewApplication(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            model.addAttribute("gatePass", gatePassService.getGatePassById(id).orElseThrow());
            return "warden/gatepass-detail";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/warden/gatepass/pending";
        }
    }

    @PostMapping("/gatepass/{id}/approve")
    public String approveApplication(@PathVariable Long id,
            @RequestParam(required = false) String remarks,
            RedirectAttributes redirectAttributes) {
        try {
            gatePassService.approveByWarden(id, remarks);
            redirectAttributes.addFlashAttribute("successMessage", "Application approved successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
        }
        return "redirect:/warden/gatepass/pending";
    }

    @PostMapping("/gatepass/{id}/reject")
    public String rejectApplication(@PathVariable Long id,
            @RequestParam String reason,
            RedirectAttributes redirectAttributes) {
        try {
            gatePassService.rejectByWarden(id, reason);
            redirectAttributes.addFlashAttribute("successMessage", "Application rejected.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
        }
        return "redirect:/warden/gatepass/pending";
    }
}
