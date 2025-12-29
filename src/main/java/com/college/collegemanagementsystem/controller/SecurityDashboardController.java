package com.college.collegemanagementsystem.controller;

import com.college.collegemanagementsystem.entity.User;
import com.college.collegemanagementsystem.service.GatePassService;
import com.college.collegemanagementsystem.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/security-gate")
public class SecurityDashboardController {

    private final UserService userService;
    private final GatePassService gatePassService;

    public SecurityDashboardController(UserService userService, GatePassService gatePassService) {
        this.userService = userService;
        this.gatePassService = gatePassService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication auth, Model model) {
        User user = userService.findByUsername(auth.getName()).orElseThrow();
        model.addAttribute("user", user);

        long approvedCount = gatePassService.getApprovedForSecurity().size();
        model.addAttribute("approvedCount", approvedCount);

        return "security/dashboard";
    }

    @GetMapping("/gatepass/approved")
    public String approvedPasses(@RequestParam(required = false) String search, Model model) {
        var gatePasses = gatePassService.getApprovedForSecurity();

        if (search != null && !search.trim().isEmpty()) {
            gatePasses = gatePasses.stream()
                    .filter(gp -> gp.getApplicationNumber().contains(search) ||
                            gp.getStudent().getFirstName().toLowerCase().contains(search.toLowerCase()) ||
                            gp.getStudent().getLastName().toLowerCase().contains(search.toLowerCase()))
                    .toList();
        }

        model.addAttribute("gatePasses", gatePasses);
        model.addAttribute("search", search);
        return "security/approved-list";
    }

    @GetMapping("/gatepass/{id}")
    public String viewPass(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            model.addAttribute("gatePass", gatePassService.getGatePassById(id).orElseThrow());
            return "security/gatepass-detail";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/security-gate/gatepass/approved";
        }
    }

    @PostMapping("/gatepass/{id}/mark-used")
    public String markAsUsed(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            gatePassService.markAsUsed(id);
            redirectAttributes.addFlashAttribute("successMessage", "Gate pass marked as used. Student may exit.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
        }
        return "redirect:/security-gate/gatepass/approved";
    }
}
