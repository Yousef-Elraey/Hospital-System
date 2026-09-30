package com.hospital.time_slots.repository;

import com.hospital.entity.TimeSlots;
import com.hospital.entity.TimeSlotsStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface TimeSlotsRepository extends JpaRepository<TimeSlots, Long>, JpaSpecificationExecutor<TimeSlots> {
    @Query(" SELECT t FROM TimeSlots t WHERE t.doctor.id = :doctorId " +
            "AND t.timeSlotsStatus = 'AVAILABLE' " +
            "AND t.day >= CURRENT_DATE")
    List<TimeSlots> getAvailableTimeSlots(@Param("doctorId") Long doctorId);

}

