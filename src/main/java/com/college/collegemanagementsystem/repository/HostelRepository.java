package com.college.collegemanagementsystem.repository;

import com.college.collegemanagementsystem.entity.Hostel;
import com.college.collegemanagementsystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HostelRepository extends JpaRepository<Hostel, Long> {

    Optional<Hostel> findByName(String name);

    Optional<Hostel> findByWarden(User warden);

    boolean existsByName(String name);
}
