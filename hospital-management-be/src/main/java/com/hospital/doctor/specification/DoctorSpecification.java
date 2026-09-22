package com.hospital.doctor.specification;

import com.hospital.entity.Appointment;
import com.hospital.entity.Diagnose;
import com.hospital.entity.Doctor;
import org.springframework.data.jpa.domain.Specification;

public class DoctorSpecification {

    public static Specification<Doctor> hasName(String name) {
        return (root, query, cb) ->
                name == null
                        ? null
                        : cb.like(
                        cb.lower(root.get("name")),
                        "%" + name.toLowerCase() + "%");
    }

    public static Specification<Doctor> hasSpecialityId(Long specialityId) {
        return (root, query, cb) ->
                specialityId == null
                        ? null
                        : cb.equal(root.get("speciality").get("id"), specialityId);
    }

    public static Specification<Doctor> hasContactNumber(String contactNumber) {
        return (root, query, cb) ->
                contactNumber == null
                        ? null
                        : cb.like(
                        cb.lower(root.get("contactNumber")),
                        "%" + contactNumber.toLowerCase() + "%");
    }
}
