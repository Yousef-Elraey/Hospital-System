package com.HospitalManagementSystem.HospitalSystem.repository;

import com.HospitalManagementSystem.HospitalSystem.entity.MedicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicalRecordRepository extends JpaRepository<MedicalRecord,Long> {
    List<MedicalRecord> findMedicalRecordsByPatientId(Long id);
}
