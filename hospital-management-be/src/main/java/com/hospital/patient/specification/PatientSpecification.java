package com.hospital.patient.specification;

import com.hospital.entity.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class PatientSpecification {
    public static Specification<Patient> hasName(String name) {
        return (root, query, cb) ->
                name == null
                        ? null
                        : cb.like(
                        cb.lower(root.get("name")),
                        "%" + name.toLowerCase() + "%");
    }
    public static Specification<Patient> hasPhone(String phone) {
        return (root, query, cb) ->
                phone == null
                        ? null
                        : cb.like(
                        cb.lower(root.get("phone")),
                        "%" + phone.toLowerCase() + "%");
    }

    public static Specification<Patient> hasGender(Gender gender) {
        return (root, query, cb) ->
                gender == null
                        ? null
                        : cb.equal(root.get("gender"), gender);
    }
    public static Specification<Patient> hasDateOfBirth(LocalDate dateOfBirth) {
        return (root, query, cb) ->
                dateOfBirth == null
                        ? null
                        : cb.equal(root.get("dateOfBirth"), dateOfBirth);
    }
}
