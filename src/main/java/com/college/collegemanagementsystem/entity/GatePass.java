package com.college.collegemanagementsystem.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "gate_passes")
public class GatePass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String applicationNumber;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @NotBlank(message = "Purpose is required")
    @Size(min = 10, max = 500, message = "Purpose must be between 10 and 500 characters")
    @Column(nullable = false, length = 500)
    private String purpose;

    @NotNull(message = "Out date is required")
    @Column(nullable = false)
    private LocalDate outDate;

    @NotNull(message = "Expected return date is required")
    @Column(nullable = false)
    private LocalDate expectedReturnDate;

    @NotBlank(message = "Destination is required")
    @Size(min = 2, max = 200, message = "Destination must be between 2 and 200 characters")
    @Column(nullable = false, length = 200)
    private String destination;

    @NotBlank(message = "Contact number is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "Contact number must be 10 digits")
    @Column(nullable = false, length = 10)
    private String contactNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GatePassStatus status = GatePassStatus.PENDING;

    // Warden approval fields
    @Column(name = "warden_approval_date")
    private LocalDateTime wardenApprovalDate;

    @Column(name = "warden_remarks", length = 500)
    private String wardenRemarks;

    // Student section approval fields
    @Column(name = "section_approval_date")
    private LocalDateTime sectionApprovalDate;

    @Column(name = "section_remarks", length = 500)
    private String sectionRemarks;

    // Rejection fields
    @Column(name = "rejection_reason", length = 500)
    private String rejectionReason;

    @Column(name = "rejected_by")
    private String rejectedBy; // WARDEN or STUDENT_SECTION

    // Usage tracking
    @Column(name = "used_date")
    private LocalDateTime usedDate;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void generateApplicationNumber() {
        if (applicationNumber == null) {
            applicationNumber = "GP" + System.currentTimeMillis();
        }
    }
}
