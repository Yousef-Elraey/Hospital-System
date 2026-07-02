package com.hospital.time_slots.repository;

import com.hospital.entity.TimeSlots;
import com.hospital.entity.TimeSlotsStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TimeSlotsRepository extends JpaRepository<TimeSlots, Long> {
    @Query(" SELECT t FROM TimeSlots t WHERE t.doctor.id = :doctorId " +
            "AND t.status = 'AVAILABLE' " +
            "AND t.day >= CURRENT_DATE")
    List<TimeSlots> getAvailableTimeSlots(@Param("doctorId") Long doctorId);

    @Query("""
            select t from TimeSlots t
            where (:doctorId is null or t.doctor.id = :doctorId)
                and (:timeSlotsStatus is null or t.status = :timeSlotsStatus)
                and (:from is null or (t.day >= :from and t.day <= :to))
            """)
    Page<TimeSlots> searchTimeSlots(@Param("doctorId") Long doctorId,
                                    @Param("timeSlotsStatus") TimeSlotsStatus timeSlotsStatus,
                                    @Param("from") LocalDate from,
                                    @Param("to") LocalDate to,
                                    Pageable pageable);
}

