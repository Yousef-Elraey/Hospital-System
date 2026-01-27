package com.HospitalManagementSystem.HospitalSystem.service;

import com.HospitalManagementSystem.HospitalSystem.entity.Appointment;
import com.HospitalManagementSystem.HospitalSystem.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class AppointmentService {
    @Autowired
    AppointmentRepository repo;

    public List<Appointment> getAllAppointments() {
            return repo.findAll();

    }

    public Appointment getAppointmentById(Long id) {
       return repo.findById(id).orElse(null);
    }

    public void createAppointment(Appointment appointment) {
        appointment.setCreatedAt(new Date());
        appointment.setUpdatedAt(new Date());
        repo.save(appointment);
    }

    public void updateAppointment(Long id, Appointment appointment) {
        Appointment appointmentTemp = repo.findById(id).get();
        if (repo.findById(id).isPresent()){
            appointmentTemp.setTiming(appointment.getTiming());
            appointmentTemp.setUpdatedAt(new Date());
            appointmentTemp.setUpdatedBy(appointment.getUpdatedBy());
            appointmentTemp.setDoctor(appointment.getDoctor());
            appointmentTemp.setPatient(appointment.getPatient());
            appointmentTemp.setCreatedAt(repo.findById(id).get().getCreatedAt());
            appointmentTemp.setCreatedBy(appointment.getCreatedBy());
            repo.save(appointmentTemp);
        }
        else {
            createAppointment(appointment);        }
    }

    public boolean deleteAppointment(Long id) {
        if (repo.findById(id).isPresent()){
            repo.deleteById(id);
            return true;
        }else{
            return false;
        }


    }
}
