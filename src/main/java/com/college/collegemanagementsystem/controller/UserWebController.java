package com.college.collegemanagementsystem.controller;

import com.college.collegemanagementsystem.entity.User;
import com.college.collegemanagementsystem.entity.UserRole;
import com.college.collegemanagementsystem.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/users")
public class UserWebController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public UserWebController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String listUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "users/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", UserRole.values());
        return "users/form";
    }

    @PostMapping
    public String createUser(@Valid @ModelAttribute User user,
            BindingResult result,
            @RequestParam String rawPassword,
            Model model,
            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("roles", UserRole.values());
            return "users/form";
        }

        // Validate password
        if (rawPassword == null || rawPassword.trim().isEmpty()) {
            model.addAttribute("errorMessage", "Password is required");
            model.addAttribute("roles", UserRole.values());
            return "users/form";
        }

        if (rawPassword.length() < 6) {
            model.addAttribute("errorMessage", "Password must be at least 6 characters long");
            model.addAttribute("roles", UserRole.values());
            return "users/form";
        }

        try {
            // Check if username already exists
            if (userService.findByUsername(user.getUsername()).isPresent()) {
                model.addAttribute("errorMessage", "Username already exists!");
                model.addAttribute("roles", UserRole.values());
                return "users/form";
            }

            // Set encrypted password
            user.setPassword(passwordEncoder.encode(rawPassword));
            user.setEnabled(true);

            userService.createUser(user);
            redirectAttributes.addFlashAttribute("successMessage", "User created successfully!");
            return "redirect:/users";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error creating user: " + e.getMessage());
            model.addAttribute("roles", UserRole.values());
            return "users/form";
        }
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            User user = userService.findById(id)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            model.addAttribute("user", user);
            model.addAttribute("roles", UserRole.values());
            return "users/form";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/users";
        }
    }

    @PostMapping("/{id}")
    public String updateUser(@PathVariable Long id,
            @Valid @ModelAttribute User user,
            BindingResult result,
            @RequestParam(required = false) String rawPassword,
            Model model,
            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("roles", UserRole.values());
            return "users/form";
        }

        try {
            User existingUser = userService.findById(id)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            existingUser.setUsername(user.getUsername());
            existingUser.setFullName(user.getFullName());
            existingUser.setEmail(user.getEmail());
            existingUser.setRole(user.getRole());
            existingUser.setEnabled(user.getEnabled());

            // Update password only if provided
            if (rawPassword != null && !rawPassword.trim().isEmpty()) {
                if (rawPassword.length() < 6) {
                    model.addAttribute("errorMessage", "Password must be at least 6 characters long");
                    model.addAttribute("roles", UserRole.values());
                    return "users/form";
                }
                existingUser.setPassword(passwordEncoder.encode(rawPassword));
            }

            userService.updateUser(existingUser);
            redirectAttributes.addFlashAttribute("successMessage", "User updated successfully!");
            return "redirect:/users";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error updating user: " + e.getMessage());
            model.addAttribute("roles", UserRole.values());
            return "users/form";
        }
    }

    @PostMapping("/{id}/delete")
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            userService.deleteUser(id);
            redirectAttributes.addFlashAttribute("successMessage", "User deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting user: " + e.getMessage());
        }
        return "redirect:/users";
    }

    @PostMapping("/{id}/toggle-status")
    public String toggleUserStatus(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            User user = userService.findById(id)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            user.setEnabled(!user.getEnabled());
            userService.updateUser(user);
            redirectAttributes.addFlashAttribute("successMessage", "User status updated!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
        }
        return "redirect:/users";
    }
}
