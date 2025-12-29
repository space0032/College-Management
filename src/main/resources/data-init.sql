-- Database Initialization Script for Hostel Gate Pass System
-- This script creates initial users and test data

-- Note: Run this after the application has created tables
-- For H2, you can run these queries in H2 Console
-- For MySQL production, run via MySQL client

-- Step 1: Create a Department (if not exists)
INSERT INTO departments (name, description, created_at, updated_at)
VALUES ('Computer Science', 'Computer Science Department', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT DO NOTHING;

-- Step 2: Create a Hostel
INSERT INTO hostels (name, capacity, address, created_at, updated_at)
VALUES ('Boys Hostel 1', 200, 'Campus North Block', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Step 3: Create Users (passwords are BCrypt encrypted 'password123')
-- The encrypted password for 'password123' is: $2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG

-- Warden User
INSERT INTO users (username, password, full_name, email, role, enabled, hostel_id, created_at, updated_at)
VALUES ('warden1', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 
        'Warden Kumar', 'warden@college.edu', 'WARDEN', true, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Student Section User
INSERT INTO users (username, password, full_name, email, role, enabled, created_at, updated_at)
VALUES ('section1', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG',
        'Section Officer', 'section@college.edu', 'STUDENT_SECTION', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Security User
INSERT INTO users (username, password, full_name, email, role, enabled, created_at, updated_at)
VALUES ('security1', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG',
        'Gate Security', 'security@college.edu', 'SECURITY', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Step 4: Create Test Students
INSERT INTO students (first_name, last_name, email, roll_number, is_hostelite, department_id, hostel_id, created_at, updated_at)
VALUES ('Raj', 'Kumar', 'raj@student.edu', 'CS2023001', true, 1, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO students (first_name, last_name, email, roll_number, is_hostelite, department_id, hostel_id, created_at, updated_at)
VALUES ('Priya', 'Sharma', 'priya@student.edu', 'CS2023002', true, 1, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Step 5: Create Student Users (linked to student records)
INSERT INTO users (username, password, full_name, email, role, enabled, student_id, created_at, updated_at)
VALUES ('student1', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG',
        'Raj Kumar', 'raj@student.edu', 'STUDENT', true, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO users (username, password, full_name, email, role, enabled, student_id, created_at, updated_at)
VALUES ('student2', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG',
        'Priya Sharma', 'priya@student.edu', 'STUDENT', true, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Test Credentials:
-- Username: student1, Password: password123 (Student Role)
-- Username: student2, Password: password123 (Student Role)
-- Username: warden1, Password: password123 (Warden Role)
-- Username: section1, Password: password123 (Student Section Role)
-- Username: security1, Password: password123 (Security Role)
