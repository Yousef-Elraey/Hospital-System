package com.hospital.medical_record.repository;

import com.hospital.entity.MedicalRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {
    List<MedicalRecord> findMedicalRecordsByPatientId(Long id);

    @Query("""
            SELECT m FROM MedicalRecord m
            WHERE (:patientId IS NULL OR m.patient.id = :patientId)
             AND (:doctorId IS NULL OR m.doctor.id = :doctorId)
             AND (:diagnoseId IS NULL OR m.diagnose.id = :diagnoseId)
             """)
    Page<MedicalRecord> searchMedicalRecord(@Param("patientId") Long patientId,
                                            @Param("doctorId") Long doctorId,
                                            @Param("diagnoseId") Long diagnoseId,
                                            Pageable pageable);
}
