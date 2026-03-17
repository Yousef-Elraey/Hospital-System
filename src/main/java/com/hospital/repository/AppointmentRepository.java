package com.hospital.repository;

import com.hospital.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    Optional<Appointment> findFirstByOrderByIdAsc();

    @Query("from Appointment a where a.status.id <= 3 order by a.createdAt")  //(new + paid + pending)
    List<Appointment> appointmentsStatusNewPaidPending(); //(all appointments of type new,paid,pending)

    @Query("from Appointment a where a.status.id = 2 OR a.status.id = 3")   //(paid+pending)
    List<Appointment> appointmentsStatusPaidPending(); //(all appointments of type paid,pending)

//    @Query("from Appointment a where a.patient.id = :id and a.status.id = 2 order by createdAt")
    Optional<Appointment> findFirstByPatientIdAndStatusIdOrderByCreatedAtAsc(Long id, Long statusId);

    @Query("from Appointment a where a.status.id = 3")          //(pending)
    List<Appointment> appointmentsStatusPending(); //(all appointments of type pending)
}
