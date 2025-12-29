package com.college.collegemanagementsystem.service;

import com.college.collegemanagementsystem.entity.*;
import com.college.collegemanagementsystem.repository.GatePassRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class GatePassService {

    private final GatePassRepository gatePassRepository;

    public GatePassService(GatePassRepository gatePassRepository) {
        this.gatePassRepository = gatePassRepository;
    }

    // Student submits application
    public GatePass submitApplication(GatePass gatePass) {
        gatePass.setStatus(GatePassStatus.PENDING);
        return gatePassRepository.save(gatePass);
    }

    // Get all applications by student
    public List<GatePass> getApplicationsByStudent(Student student) {
        return gatePassRepository.findByStudentOrderByCreatedAtDesc(student);
    }

    // Get pending applications for warden's hostel
    public List<GatePass> getPendingForWarden(Hostel hostel) {
        return gatePassRepository.findByStudentHostelAndStatus(hostel, GatePassStatus.PENDING);
    }

    // Warden approves application
    public GatePass approveByWarden(Long id, String remarks) {
        GatePass gatePass = gatePassRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Gate pass not found"));

        if (gatePass.getStatus() != GatePassStatus.PENDING) {
            throw new RuntimeException("Can only approve pending applications");
        }

        gatePass.setStatus(GatePassStatus.WARDEN_APPROVED);
        gatePass.setWardenApprovalDate(LocalDateTime.now());
        gatePass.setWardenRemarks(remarks);

        return gatePassRepository.save(gatePass);
    }

    // Warden rejects application
    public GatePass rejectByWarden(Long id, String reason) {
        GatePass gatePass = gatePassRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Gate pass not found"));

        if (gatePass.getStatus() != GatePassStatus.PENDING) {
            throw new RuntimeException("Can only reject pending applications");
        }

        gatePass.setStatus(GatePassStatus.REJECTED);
        gatePass.setRejectionReason(reason);
        gatePass.setRejectedBy("WARDEN");

        return gatePassRepository.save(gatePass);
    }

    // Get warden-approved applications for student section
    public List<GatePass> getPendingForSection() {
        return gatePassRepository.findByStatus(GatePassStatus.WARDEN_APPROVED);
    }

    // Student section approves application
    public GatePass approveBySection(Long id, String remarks) {
        GatePass gatePass = gatePassRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Gate pass not found"));

        if (gatePass.getStatus() != GatePassStatus.WARDEN_APPROVED) {
            throw new RuntimeException("Can only approve warden-approved applications");
        }

        gatePass.setStatus(GatePassStatus.SECTION_APPROVED);
        gatePass.setSectionApprovalDate(LocalDateTime.now());
        gatePass.setSectionRemarks(remarks);

        return gatePassRepository.save(gatePass);
    }

    // Student section rejects application
    public GatePass rejectBySection(Long id, String reason) {
        GatePass gatePass = gatePassRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Gate pass not found"));

        if (gatePass.getStatus() != GatePassStatus.WARDEN_APPROVED) {
            throw new RuntimeException("Can only reject warden-approved applications");
        }

        gatePass.setStatus(GatePassStatus.REJECTED);
        gatePass.setRejectionReason(reason);
        gatePass.setRejectedBy("STUDENT_SECTION");

        return gatePassRepository.save(gatePass);
    }

    // Get approved passes for security
    public List<GatePass> getApprovedForSecurity() {
        return gatePassRepository.findByStatus(GatePassStatus.SECTION_APPROVED);
    }

    // Security marks pass as used
    public GatePass markAsUsed(Long id) {
        GatePass gatePass = gatePassRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Gate pass not found"));

        if (gatePass.getStatus() != GatePassStatus.SECTION_APPROVED) {
            throw new RuntimeException("Can only mark approved passes as used");
        }

        gatePass.setStatus(GatePassStatus.USED);
        gatePass.setUsedDate(LocalDateTime.now());

        return gatePassRepository.save(gatePass);
    }

    // Get single gate pass
    public Optional<GatePass> getGatePassById(Long id) {
        return gatePassRepository.findById(id);
    }

    public Optional<GatePass> getGatePassByApplicationNumber(String applicationNumber) {
        return gatePassRepository.findByApplicationNumber(applicationNumber);
    }

    // Get all gate passes
    public List<GatePass> getAllGatePasses() {
        return gatePassRepository.findAll();
    }

    // Statistics
    public long countByStatus(GatePassStatus status) {
        return gatePassRepository.countByStatus(status);
    }

    public long countByStudentAndStatus(Student student, GatePassStatus status) {
        return gatePassRepository.countByStudentAndStatus(student, status);
    }
}
