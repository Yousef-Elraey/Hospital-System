package com.hospital.patient.repository;

import com.hospital.entity.Doctor;
import com.hospital.entity.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findByPhone(String phoneNumber);

    Optional<Patient> findByNameAndDateOfBirthAndPhone(String name, LocalDate dateOfBirth, String phone);

    @Query("""
                SELECT p
                FROM Patient p
                WHERE (:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')))
                      AND (:dateOfBirth IS NULL OR p.dateOfBirth = :dateOfBirth)
                      AND (:phone IS NULL OR p.phone = :phone)
            """)
    Page<Patient> searchPatient(@Param("name") String name,
                                @Param("dateOfBirth") LocalDate dateOfBirth,
                                @Param("phone") String phone,
                                Pageable pageable);

}
