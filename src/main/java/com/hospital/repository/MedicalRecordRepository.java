package com.hospital.repository;

import com.hospital.entity.MedicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MedicalRecordRepository extends JpaRepository<MedicalRecord,Long> {
    Optional<List<MedicalRecord>> findMedicalRecordsByPatientId(Long id);
}
