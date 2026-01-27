package com.HospitalManagementSystem.HospitalSystem.repository;

import com.HospitalManagementSystem.HospitalSystem.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment,Long> {

}
