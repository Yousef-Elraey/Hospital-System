package com.hospital.billing.repository;

import com.hospital.entity.Billing;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BillingRepository extends JpaRepository<Billing, Long> {

    @Query("SELECT b FROM Billing b " +
            "WHERE (:patientId IS NULL OR b.patient.id = :patientId) " +
            "AND (:amount IS NULL OR b.amount = :amount)")
    Page<Billing> searchBilling(@Param("patientId") Long patientId,
                                @Param("amount") Long amount,
                                Pageable pageable);
}
