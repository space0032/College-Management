package com.college.collegemanagementsystem.service;

import com.college.collegemanagementsystem.entity.Hostel;
import com.college.collegemanagementsystem.entity.User;
import com.college.collegemanagementsystem.repository.HostelRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class HostelService {

    private final HostelRepository hostelRepository;

    public HostelService(HostelRepository hostelRepository) {
        this.hostelRepository = hostelRepository;
    }

    public List<Hostel> getAllHostels() {
        return hostelRepository.findAll();
    }

    public Hostel saveHostel(Hostel hostel) {
        return hostelRepository.save(hostel);
    }

    public Optional<Hostel> getHostelById(Long id) {
        return hostelRepository.findById(id);
    }

    public Optional<Hostel> getHostelByName(String name) {
        return hostelRepository.findByName(name);
    }

    public Optional<Hostel> getHostelByWarden(User warden) {
        return hostelRepository.findByWarden(warden);
    }

    public void deleteHostel(Long id) {
        if (!hostelRepository.existsById(id)) {
            throw new RuntimeException("Hostel not found with id: " + id);
        }
        hostelRepository.deleteById(id);
    }

    public boolean hostelExists(String name) {
        return hostelRepository.existsByName(name);
    }
}
