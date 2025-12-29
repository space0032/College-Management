package com.college.collegemanagementsystem.config;

import com.college.collegemanagementsystem.entity.User;
import com.college.collegemanagementsystem.entity.UserRole;
import com.college.collegemanagementsystem.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Check if admin user already exists
            if (userRepository.findByUsername("admin").isEmpty()) {
                // Create admin user
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

            // Check if test student exists
            if (userRepository.findByUsername("student").isEmpty()) {
                User student = new User();
                student.setUsername("student");
                student.setPassword(passwordEncoder.encode("student"));
                student.setFullName("Test Student");
                student.setEmail("student@college.edu");
                student.setRole(UserRole.STUDENT);
                student.setEnabled(true);

                userRepository.save(student);
                System.out.println("✅ Student user created - Username: student, Password: student");
            }

            System.out.println("✅ Database initialization complete!");
        };
    }
}
