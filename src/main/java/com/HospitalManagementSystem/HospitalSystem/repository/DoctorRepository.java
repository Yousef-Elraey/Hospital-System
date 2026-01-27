package com.HospitalManagementSystem.HospitalSystem.repository;

import com.HospitalManagementSystem.HospitalSystem.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor,Long> {
}
