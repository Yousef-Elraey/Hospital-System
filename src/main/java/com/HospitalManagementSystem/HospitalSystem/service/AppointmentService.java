package com.HospitalManagementSystem.HospitalSystem.service;

import com.HospitalManagementSystem.HospitalSystem.dto.AppointmentDto;
import com.HospitalManagementSystem.HospitalSystem.dto.BookRequestDto;
import com.HospitalManagementSystem.HospitalSystem.dto.BookResponseDto;
import com.HospitalManagementSystem.HospitalSystem.dto.PatientDto;
import com.HospitalManagementSystem.HospitalSystem.entity.Appointment;
import com.HospitalManagementSystem.HospitalSystem.entity.Patient;
import com.HospitalManagementSystem.HospitalSystem.repository.AppointmentRepository;
import com.HospitalManagementSystem.HospitalSystem.repository.DoctorRepository;
import com.HospitalManagementSystem.HospitalSystem.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService {
    @Autowired
    AppointmentRepository appointmentRepository;
    @Autowired
    PatientRepository patientRepository;
    @Autowired
    DoctorRepository doctorRepository;
    @Autowired
    MedicalRecordService medicalRecordService;


    public List<AppointmentDto> getAllAppointments() {
        List<Appointment> appointments = appointmentRepository.findAll();
        List<AppointmentDto> appointmentDtos = new ArrayList<>();

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
            appointmentDto.setStatus(appointment.getStatus());
            appointmentDtos.add(appointmentDto);
        }

        return appointmentDtos;

    }

    public AppointmentDto getAppointmentById(Long id) {
        Appointment appointment = appointmentRepository.findById(id).orElse(null);
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
        appointmentDto.setStatus(appointment.getStatus());
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
        appointment.setStatus(appointmentDto.getStatus());
        appointmentRepository.save(appointment);
    }

    public void updateAppointment(Long id, AppointmentDto appointmentDto) {
        Optional<Appointment> appointmentTemp = appointmentRepository.findById(id);
        if (appointmentTemp.isPresent()) {
            Appointment appointment = appointmentTemp.get();

            appointment.setTiming(appointmentDto.getTiming());
            appointment.setUpdatedAt(LocalDateTime.now());
            appointment.setUpdatedBy(appointmentDto.getUpdatedBy());
            appointment.setDoctor(doctorRepository.findById(appointmentDto.getDoctorId()).get());
            appointment.setPatient(patientRepository.findById(appointmentDto.getDoctorId()).get());
            appointment.setStatus(appointmentDto.getStatus());
            appointmentRepository.save(appointment);
        } else {
            createAppointment(appointmentDto);
        }

    }

    public boolean deleteAppointment(Long id) {
        if (appointmentRepository.findById(id).isPresent()) {
            appointmentRepository.deleteById(id);
            return true;
        } else {
            return false;
        }


    }

    public BookResponseDto book(BookRequestDto request) {

        if (!patientRepository.findById(request.getPatientId()).isPresent()) {
            Patient patient = new Patient();
            patient.setName(request.getPatientName())
                    .setGender(request.getPatientGender())
                    .setPhone(request.getPatientPhone())
                    .setDateOfBirth(request.getPatientDateOfBirth())
                    .setCreatedBy(request.getPatientCreatedBy())
                    .setCreatedAt(LocalDateTime.now())
                    .setUpdatedBy(request.getPatientUpdatedBy())
                    .setUpdatedAt(LocalDateTime.now());
            patientRepository.save(patient);
        }

        Appointment appointment = new Appointment();
        appointment.setTiming(request.getAppointmentTiming())
                .setCreatedBy(request.getAppointmentCreatedBy())
                .setCreatedAt(LocalDateTime.now())
                .setUpdatedBy(request.getAppointmentUpdatedBy())
                .setUpdatedAt(LocalDateTime.now())
                .setPatient(patientRepository.findById(request.getPatientId()).get())
                .setDoctor(doctorRepository.findById(request.getDoctorId()).get())
                .setStatus(request.getStatus());
        appointmentRepository.save(appointment);

        List<Appointment> appointments = appointmentRepository.findAll();
        BookResponseDto responseDto = new BookResponseDto();
        responseDto.setNumberOfWaiting((appointments.size()) - 1)
                .setStatus(appointment.getStatus());

        return responseDto;

    }

    public PatientDto currentPatient() {
        Optional<Appointment> appointment = appointmentRepository.findFirstByOrderByIdAsc();

        if (appointment.isPresent()){
            Appointment appointmentDb = appointment.get();
        Patient patientDb = appointmentDb.getPatient();
        PatientDto patientDto = new PatientDto();
        patientDto.setId(patientDb.getId())
                .setName(patientDb.getName())
                .setGender(patientDb.getGender())
                .setPhone(patientDb.getPhone())
                .setDateOfBirth(patientDb.getDateOfBirth())
                .setMedicalRecords(medicalRecordService.getByPatientId(patientDb.getId()))
                .setCreatedBy(patientDb.getCreatedBy())
                .setCreatedAt(patientDb.getCreatedAt())
                .setUpdatedBy(patientDb.getUpdatedBy())
                .setUpdatedAt(patientDb.getUpdatedAt());
        return patientDto;
        }else
            return new PatientDto();
    }
    public  PatientDto next() {
       Optional<Appointment> appointment = appointmentRepository.findFirstByOrderByIdAsc();
       PatientDto patientDto = currentPatient();
       appointmentRepository.delete(appointment.get());
        return  patientDto;
    }

}
