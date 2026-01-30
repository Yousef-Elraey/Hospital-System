package com.HospitalManagementSystem.HospitalSystem.service;

import com.HospitalManagementSystem.HospitalSystem.dto.AppointmentDto;
import com.HospitalManagementSystem.HospitalSystem.entity.Appointment;
import com.HospitalManagementSystem.HospitalSystem.repository.AppointmentRepository;
import com.HospitalManagementSystem.HospitalSystem.repository.DoctorRepository;
import com.HospitalManagementSystem.HospitalSystem.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService {
    @Autowired
    AppointmentRepository repo;
    @Autowired
    PatientRepository patientRepository;
    @Autowired
    DoctorRepository doctorRepository;

    public List<AppointmentDto> getAllAppointments() {
        List<Appointment> appointments = repo.findAll();
        List<AppointmentDto> appointmentDtos = new ArrayList<>();
//        if (!appointments.isEmpty()) {
//            appointments.stream().forEach(appointment -> {
//                AppointmentDto appointmentDto = new AppointmentDto();
//                appointmentDto.setId(appointment.getId());
//                appointmentDto.setTiming(appointment.getTiming());
//                appointmentDto.setCreatedBy(appointment.getCreatedBy());
//                appointmentDto.setCreatedAt(appointment.getCreatedAt());
//                appointmentDto.setUpdatedBy(appointment.getUpdatedBy());
//                appointmentDto.setUpdatedAt(appointment.getUpdatedAt());
//                appointmentDtos.add(appointmentDto);
//            });
//    }
        for (Appointment appointment : appointments) {
            AppointmentDto appointmentDto = new AppointmentDto();
            appointmentDto.setId(appointment.getId());
            appointmentDto.setTiming(appointment.getTiming());
            appointmentDto.setDoctorId(appointment.getDoctor().getId());
            appointmentDto.setPatientId(appointment.getPatient().getId());
            appointmentDto.setCreatedBy(appointment.getCreatedBy());
            appointmentDto.setCreatedAt(appointment.getCreatedAt());
            appointmentDto.setUpdatedBy(appointment.getUpdatedBy());
            appointmentDto.setUpdatedAt(LocalDateTime.now());

            appointmentDtos.add(appointmentDto);
        }

        return appointmentDtos;

    }

    public AppointmentDto getAppointmentById(Long id) {
        Appointment appointment = repo.findById(id).orElse(null);
        AppointmentDto appointmentDto = new AppointmentDto();

        if (appointment != null){
        appointmentDto.setId(appointment.getId());
        appointmentDto.setTiming(appointment.getTiming());
        appointmentDto.setDoctorId(appointment.getDoctor().getId());
        appointmentDto.setPatientId(appointment.getPatient().getId());
        appointmentDto.setCreatedBy(appointment.getCreatedBy());
        appointmentDto.setCreatedAt(appointment.getCreatedAt());
        appointmentDto.setUpdatedBy(appointment.getUpdatedBy());
        appointmentDto.setUpdatedAt(LocalDateTime.now());
        return appointmentDto;
        }else {
            return null;
        }
    }

    public void createAppointment(AppointmentDto appointmentDto) {

        Appointment appointment = new Appointment();
        appointment.setId(appointmentDto.getId());
        appointment.setTiming(appointmentDto.getTiming());
        appointment.setCreatedBy(appointmentDto.getCreatedBy());
        appointment.setCreatedAt(LocalDateTime.now());
        appointment.setUpdatedBy(appointmentDto.getUpdatedBy());
        appointment.setUpdatedAt(LocalDateTime.now());
        appointment.setPatient(patientRepository.findById(appointmentDto.getPatientId()).get());
        appointment.setDoctor(doctorRepository.findById(appointmentDto.getDoctorId()).get());
        repo.save(appointment);
    }

    public void updateAppointment(Long id, AppointmentDto appointmentDto) {
        Optional<Appointment> appointmentTemp = repo.findById(id);
        if (appointmentTemp.isPresent()) {
            Appointment appointment = appointmentTemp.get();

            appointment.setTiming(appointmentDto.getTiming());
            appointment.setUpdatedAt(LocalDateTime.now());
            appointment.setUpdatedBy(appointmentDto.getUpdatedBy());
            appointment.setDoctor(doctorRepository.findById(appointmentDto.getDoctorId()).get());
            appointment.setPatient(patientRepository.findById(appointmentDto.getDoctorId()).get());
            repo.save(appointment);
        } else {
            createAppointment(appointmentDto);
        }

    }

    public boolean deleteAppointment(Long id) {
        if (repo.findById(id).isPresent()) {
            repo.deleteById(id);
            return true;
        } else {
            return false;
        }


    }
}
