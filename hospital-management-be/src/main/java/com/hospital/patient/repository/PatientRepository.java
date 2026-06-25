package com.hospital.patient.repository;

import com.hospital.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findByPhone(String phoneNumber);

    Optional<Patient> findByNameAndDateOfBirthAndPhone(String name, LocalDate dateOfBirth, String phone);


}
