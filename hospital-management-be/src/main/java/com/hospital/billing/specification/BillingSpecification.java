package com.hospital.billing.specification;

import com.hospital.entity.Appointment;
import com.hospital.entity.Billing;
import org.springframework.data.jpa.domain.Specification;

public class BillingSpecification {
    public static Specification<Billing> hasPatientId(Long patientId) {
        return (root, query, cb) ->
                patientId == null
                        ? null
                        : cb.equal(root.get("patient").get("id"), patientId);
    }

    public static Specification<Billing> hasAmount(Long amount) {
        return (root, query, cb) ->
                amount == null
                        ? null
                        : cb.equal(root.get("amount"), amount);
    }
}
