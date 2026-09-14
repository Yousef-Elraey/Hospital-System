package com.hospital.diagnose.specification;

import com.hospital.entity.Appointment;
import com.hospital.entity.Diagnose;
import org.springframework.data.jpa.domain.Specification;

public class DiagnoseSpecification {
    public static Specification<Diagnose> hasNameAr(String nameAr) {
        return (root, query, cb) ->
                nameAr == null
                        ? null
                        : cb.like(
                        cb.lower(root.get("nameAr")),
                        "%" + nameAr.toLowerCase() + "%");
    }
    public static Specification<Diagnose> hasNameEn(String nameEn) {
        return (root, query, cb) ->
                nameEn == null
                        ? null
                        : cb.like(
                        cb.lower(root.get("nameEn")),
                        "%" + nameEn.toLowerCase() + "%");
    }
}
