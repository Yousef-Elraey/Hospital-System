package com.hospital.appointment.specification;

import com.hospital.entity.Appointment;
import com.hospital.entity.User;
import org.springframework.data.jpa.domain.Specification;

public class AppointmentSpecification {
    public static Specification<Appointment> hasPatientId(Long patientId) {
        return (root, query, cb) ->
                patientId == null
                        ? null
                        : cb.equal(root.get("patient").get("id"), patientId);
    }

    public static Specification<Appointment> hasDoctorId(Long doctorId) {
        return (root, query, cb) ->
                doctorId == null
                        ? null
                        : cb.equal(root.get("doctor").get("id"), doctorId);
    }

    public static Specification<Appointment> hasStatusId(Long statusId) {
        return (root, query, cb) ->
                statusId == null
                        ? null
                        : cb.equal(root.get("status").get("id"), statusId);
    }
}
