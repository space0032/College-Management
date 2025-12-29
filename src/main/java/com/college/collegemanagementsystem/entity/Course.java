package com.college.collegemanagementsystem.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "courses")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false, unique = true)
    private String code;
    
    @Column
    private Integer credits;
    
    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;
}