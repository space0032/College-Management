package com.college.collegemanagementsystem.repository;

import com.college.collegemanagementsystem.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    // Find course by code
    Optional<Course> findByCode(String code);

    // Check if course exists by code
    boolean existsByCode(String code);

    // Find all courses by department
    List<Course> findByDepartmentId(Long departmentId);
}
