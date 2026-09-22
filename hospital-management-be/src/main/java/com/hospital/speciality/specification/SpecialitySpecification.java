package com.hospital.speciality.specification;

import com.hospital.entity.Diagnose;
import com.hospital.entity.Speciality;
import org.springframework.data.jpa.domain.Specification;

public class SpecialitySpecification {
    public static Specification<Speciality> hasNameAr(String nameAr) {
        return (root, query, cb) ->
                nameAr == null
                        ? null
                        : cb.like(
                        cb.lower(root.get("nameAr")),
                        "%" + nameAr.toLowerCase() + "%");
    }
    public static Specification<Speciality> hasNameEn(String nameEn) {
        return (root, query, cb) ->
                nameEn == null
                        ? null
                        : cb.like(
                        cb.lower(root.get("nameEn")),
                        "%" + nameEn.toLowerCase() + "%");
    }
}
