package com.hospital.timeSlots.repository;

import com.hospital.entity.TimeSlots;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TimeSlotsRepository extends JpaRepository<TimeSlots,Long> {
    @Query(" SELECT t FROM TimeSlots t WHERE t.doctor.id = :doctorId " +
                                "AND t.status = 'AVAILABLE' " +
                                "AND t.day >= CURRENT_DATE")
    List<TimeSlots> getAvailableTimeSlots(@Param("doctorId") Long doctorId);


}

