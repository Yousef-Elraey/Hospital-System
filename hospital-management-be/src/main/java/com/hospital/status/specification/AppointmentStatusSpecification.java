package com.hospital.status.specification;

import com.hospital.entity.Appointment;
import com.hospital.entity.AppointmentStatus;
import com.hospital.entity.Diagnose;
import org.springframework.data.jpa.domain.Specification;

public class AppointmentStatusSpecification {
    public static Specification<AppointmentStatus> hasId(Long id) {
        return (root, query, cb) ->
                id == null
                        ? null
                        : cb.equal(root.get("id"), id);
    }

    public static Specification<AppointmentStatus> hasNameAr(String nameAr) {
        return (root, query, cb) ->
                nameAr == null
                        ? null
                        : cb.like(
                        cb.lower(root.get("nameAr")),
                        "%" + nameAr.toLowerCase() + "%");
    }
    public static Specification<AppointmentStatus> hasNameEn(String nameEn) {
        return (root, query, cb) ->
                nameEn == null
                        ? null
                        : cb.like(
                        cb.lower(root.get("nameEn")),
                        "%" + nameEn.toLowerCase() + "%");
    }
}
