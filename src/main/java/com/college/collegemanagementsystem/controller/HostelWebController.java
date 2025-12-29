package com.college.collegemanagementsystem.controller;

import com.college.collegemanagementsystem.entity.Hostel;
import com.college.collegemanagementsystem.entity.User;
import com.college.collegemanagementsystem.entity.UserRole;
import com.college.collegemanagementsystem.service.HostelService;
import com.college.collegemanagementsystem.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/hostels")
public class HostelWebController {

    private final HostelService hostelService;
    private final UserService userService;

    public HostelWebController(HostelService hostelService, UserService userService) {
        this.hostelService = hostelService;
        this.userService = userService;
    }

    @GetMapping
    public String listHostels(Model model) {
        model.addAttribute("hostels", hostelService.getAllHostels());
        return "hostels/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("hostel", new Hostel());
        model.addAttribute("wardens", userService.findByRole(UserRole.WARDEN));
        return "hostels/form";
    }

    @PostMapping
    public String createHostel(@Valid @ModelAttribute Hostel hostel,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("wardens", userService.findByRole(UserRole.WARDEN));
            return "hostels/form";
        }

        try {
            hostelService.saveHostel(hostel);
            redirectAttributes.addFlashAttribute("successMessage", "Hostel created successfully!");
            return "redirect:/hostels";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error creating hostel: " + e.getMessage());
            model.addAttribute("wardens", userService.findByRole(UserRole.WARDEN));
            return "hostels/form";
        }
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Hostel hostel = hostelService.getHostelById(id)
                    .orElseThrow(() -> new RuntimeException("Hostel not found"));
            model.addAttribute("hostel", hostel);
            model.addAttribute("wardens", userService.findByRole(UserRole.WARDEN));
            return "hostels/form";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/hostels";
        }
    }

    @PostMapping("/{id}")
    public String updateHostel(@PathVariable Long id,
            @Valid @ModelAttribute Hostel hostel,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("wardens", userService.findByRole(UserRole.WARDEN));
            return "hostels/form";
        }

        try {
            hostel.setId(id);
            hostelService.saveHostel(hostel);
            redirectAttributes.addFlashAttribute("successMessage", "Hostel updated successfully!");
            return "redirect:/hostels";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error updating hostel: " + e.getMessage());
            model.addAttribute("wardens", userService.findByRole(UserRole.WARDEN));
            return "hostels/form";
        }
    }

    @PostMapping("/{id}/delete")
    public String deleteHostel(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            hostelService.deleteHostel(id);
            redirectAttributes.addFlashAttribute("successMessage", "Hostel deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting hostel: " + e.getMessage());
        }
        return "redirect:/hostels";
    }
}
