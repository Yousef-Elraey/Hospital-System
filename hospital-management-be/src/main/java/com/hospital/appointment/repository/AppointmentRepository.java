package com.hospital.appointment.repository;

import com.hospital.entity.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long>, JpaSpecificationExecutor<Appointment> {
    @Query("from Appointment a where a.status.id <= 3 order by a.createdAt")
    List<Appointment> appointmentsStatusNewPaidPending();

    @Query("from Appointment a where a.status.id = 2 OR a.status.id = 3")
    List<Appointment> appointmentsStatusPaidPending();

    Optional<Appointment> findFirstByPatientIdAndStatusIdOrderByCreatedAtAsc(Long id, Long statusId);

    Optional<Appointment> findFirstByPatientIdOrderByCreatedAtAsc(Long id);

    @Query("from Appointment a where a.status.id = 3 order by a.updatedAt")
    List<Appointment> appointmentsStatusPending();


}
