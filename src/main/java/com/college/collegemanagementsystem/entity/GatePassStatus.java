package com.college.collegemanagementsystem.entity;

public enum GatePassStatus {
    PENDING, // Just submitted, waiting for warden
    WARDEN_APPROVED, // Warden approved, waiting for student section
    SECTION_APPROVED, // Student section approved, ready for gate
    REJECTED, // Rejected by warden or student section
    USED // Student has exited using this pass
}
