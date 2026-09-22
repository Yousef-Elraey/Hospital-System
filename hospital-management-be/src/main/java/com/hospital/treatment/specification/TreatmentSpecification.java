package com.hospital.treatment.specification;

import com.hospital.entity.Diagnose;
import com.hospital.entity.Treatment;
import org.springframework.data.jpa.domain.Specification;

public class TreatmentSpecification {
    public static Specification<Treatment> hasNameAr(String nameAr) {
        return (root, query, cb) ->
                nameAr == null
                        ? null
                        : cb.like(
                        cb.lower(root.get("nameAr")),
                        "%" + nameAr.toLowerCase() + "%");
    }
    public static Specification<Treatment> hasNameEn(String nameEn) {
        return (root, query, cb) ->
                nameEn == null
                        ? null
                        : cb.like(
                        cb.lower(root.get("nameEn")),
                        "%" + nameEn.toLowerCase() + "%");
    }
    public static Specification<Treatment> hasActiveIngredient(String activeIngredient) {
        return (root, query, cb) ->
                activeIngredient == null
                        ? null
                        : cb.like(
                        cb.lower(root.get("activeIngredient")),
                        "%" + activeIngredient.toLowerCase() + "%");
    }
}
