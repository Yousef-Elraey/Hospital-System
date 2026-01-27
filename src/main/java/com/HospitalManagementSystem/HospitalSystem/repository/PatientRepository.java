package com.HospitalManagementSystem.HospitalSystem.repository;

import com.HospitalManagementSystem.HospitalSystem.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository extends JpaRepository<Patient,Long> {


}
