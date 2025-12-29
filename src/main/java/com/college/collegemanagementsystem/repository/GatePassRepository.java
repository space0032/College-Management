package com.college.collegemanagementsystem.repository;

import com.college.collegemanagementsystem.entity.GatePass;
import com.college.collegemanagementsystem.entity.GatePassStatus;
import com.college.collegemanagementsystem.entity.Hostel;
import com.college.collegemanagementsystem.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GatePassRepository extends JpaRepository<GatePass, Long> {

    Optional<GatePass> findByApplicationNumber(String applicationNumber);

    List<GatePass> findByStudent(Student student);

    List<GatePass> findByStatus(GatePassStatus status);

    List<GatePass> findByStudentOrderByCreatedAtDesc(Student student);

    // Find all gate passes for a specific hostel with a specific status
    @Query("SELECT gp FROM GatePass gp WHERE gp.student.hostel = :hostel AND gp.status = :status ORDER BY gp.createdAt DESC")
    List<GatePass> findByStudentHostelAndStatus(@Param("hostel") Hostel hostel, @Param("status") GatePassStatus status);

    // Find all gate passes for a specific hostel
    @Query("SELECT gp FROM GatePass gp WHERE gp.student.hostel = :hostel ORDER BY gp.createdAt DESC")
    List<GatePass> findByStudentHostel(@Param("hostel") Hostel hostel);

    // Count by status
    long countByStatus(GatePassStatus status);

    // Count by student and status
    long countByStudentAndStatus(Student student, GatePassStatus status);
}
