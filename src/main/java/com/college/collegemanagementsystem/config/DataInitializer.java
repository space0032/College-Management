package com.college.collegemanagementsystem.config;

import com.college.collegemanagementsystem.entity.*;
import com.college.collegemanagementsystem.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            DepartmentRepository departmentRepository,
            HostelRepository hostelRepository,
            StudentRepository studentRepository) {
        return args -> {
            System.out.println("🚀 Starting data initialization...");

            // Create Department if not exists
            Department csDept = departmentRepository.findByName("Computer Science")
                    .orElseGet(() -> {
                        Department dept = new Department();
                        dept.setName("Computer Science");
                        dept.setDescription("Department of Computer Science and Engineering");
                        return departmentRepository.save(dept);
                    });
            System.out.println("✅ Department created/found: " + csDept.getName());

            // Create Hostel if not exists
            Hostel boysHostel = hostelRepository.findByName("Boys Hostel 1")
                    .orElseGet(() -> {
                        Hostel hostel = new Hostel();
                        hostel.setName("Boys Hostel 1");
                        hostel.setCapacity(200);
                        hostel.setAddress("Campus North Block");
                        return hostelRepository.save(hostel);
                    });
            System.out.println("✅ Hostel created/found: " + boysHostel.getName());

            // Create Warden user
            if (userRepository.findByUsername("warden").isEmpty()) {
                User warden = new User();
                warden.setUsername("warden");
                warden.setPassword(passwordEncoder.encode("warden"));
                warden.setFullName("Hostel Warden");
                warden.setEmail("warden@college.edu");
                warden.setRole(UserRole.WARDEN);
                warden.setEnabled(true);
                warden.setHostel(boysHostel);
                userRepository.save(warden);

                // Update hostel with warden
                boysHostel.setWarden(warden);
                hostelRepository.save(boysHostel);

                System.out.println("✅ Warden user created - Username: warden, Password: warden");
            }

            // Create admin user
            if (userRepository.findByUsername("admin").isEmpty()) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin"));
                admin.setFullName("System Administrator");
                admin.setEmail("admin@college.edu");
                admin.setRole(UserRole.STUDENT_SECTION);
                admin.setEnabled(true);

                userRepository.save(admin);
                System.out.println("✅ Admin user created - Username: admin, Password: admin");
            }

            // Create security user
            if (userRepository.findByUsername("security").isEmpty()) {
                User security = new User();
                security.setUsername("security");
                security.setPassword(passwordEncoder.encode("security"));
                security.setFullName("Gate Security");
                security.setEmail("security@college.edu");
                security.setRole(UserRole.SECURITY);
                security.setEnabled(true);

                userRepository.save(security);
                System.out.println("✅ Security user created - Username: security, Password: security");
            }

            // Create hostel student
            if (userRepository.findByUsername("student").isEmpty()) {
                // First create student record
                Student student = new Student();
                student.setFirstName("Rahul");
                student.setLastName("Kumar");
                student.setEmail("rahul@student.edu");
                student.setRollNumber("CS2024001");
                student.setDepartment(csDept);
                student.setIsHostelite(true);
                student.setHostel(boysHostel);
                student = studentRepository.save(student);

                // Then create user linked to student
                User studentUser = new User();
                studentUser.setUsername("student");
                studentUser.setPassword(passwordEncoder.encode("student"));
                studentUser.setFullName("Rahul Kumar");
                studentUser.setEmail("rahul@student.edu");
                studentUser.setRole(UserRole.STUDENT);
                studentUser.setEnabled(true);
                studentUser.setStudent(student);

                userRepository.save(studentUser);
                System.out.println("✅ Student user created - Username: student, Password: student");
            }

            System.out.println("\n🎉 Database initialization complete!");
            System.out.println("\n📋 Login Credentials:");
            System.out.println("┌────────────────────────────────────────┐");
            System.out.println("│ Role          │ Username  │ Password   │");
            System.out.println("├────────────────────────────────────────┤");
            System.out.println("│ Student       │ student   │ student    │");
            System.out.println("│ Warden        │ warden    │ warden     │");
            System.out.println("│ Admin/Section │ admin     │ admin      │");
            System.out.println("│ Security      │ security  │ security   │");
            System.out.println("└────────────────────────────────────────┘");
        };
    }
}
