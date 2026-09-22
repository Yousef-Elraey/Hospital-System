package com.hospital.medical_record.specification;

import com.hospital.entity.Appointment;
import com.hospital.entity.MedicalRecord;
import org.springframework.data.jpa.domain.Specification;

public class MedicalRecordSpecification {
    public static Specification<MedicalRecord> hasPatientId(Long patientId) {
        return (root, query, cb) ->
                patientId == null
                        ? null
                        : cb.equal(root.get("patient").get("id"), patientId);
    }

    public static Specification<MedicalRecord> hasDoctorId(Long doctorId) {
        return (root, query, cb) ->
                doctorId == null
                        ? null
                        : cb.equal(root.get("doctor").get("id"), doctorId);
    }

    public static Specification<MedicalRecord> hasDiagnoseId(Long diagnoseId) {
        return (root, query, cb) ->
                diagnoseId == null
                        ? null
                        : cb.equal(root.get("diagnose").get("id"), diagnoseId);
    }

    public static Specification<MedicalRecord> hasTreatmentId(Long treatmentId) {
        return (root, query, cb) ->
                treatmentId == null
                        ? null
                        : cb.equal(root.get("treatment").get("id"), treatmentId);
    }


}
