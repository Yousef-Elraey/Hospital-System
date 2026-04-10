package com.hospital.service;

import com.hospital.dto.*;
import com.hospital.entity.Appointment;
import com.hospital.entity.AppointmentStatus;
import com.hospital.entity.Patient;
import com.hospital.exception.HospitalBusinessException;
import com.hospital.repository.AppointmentRepository;
import com.hospital.repository.AppointmentStatusRepository;
import com.hospital.repository.DoctorRepository;
import com.hospital.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.hospital.entity.AppointmentType.online;
import static com.hospital.entity.AppointmentType.onsite;
@Service
@RequiredArgsConstructor
public class AppointmentService {
   private final AppointmentRepository appointmentRepository;
   private final PatientRepository patientRepository;
   private final DoctorRepository doctorRepository;
   private final MedicalRecordService medicalRecordService;
   private final AppointmentStatusRepository appointmentStatusRepository;


    public List<AppointmentDto> getAllAppointments() {
        List<Appointment> appointments = appointmentRepository.findAll();
        if (appointments.isEmpty()){
            throw new HospitalBusinessException("no appointments found");
        }
        List<AppointmentDto> appointmentDtos = new ArrayList<>();

        for (Appointment appointment : appointments) {
            AppointmentDto appointmentDto = new AppointmentDto();
            appointmentDto.setId(appointment.getId());
            appointmentDto.setTiming(appointment.getTiming());
            appointmentDto.setAppointmentType(appointment.getAppointmentType());
            appointmentDto.setDoctorId(appointment.getDoctor().getId());
            appointmentDto.setPatientId(appointment.getPatient().getId());
            appointmentDto.setCreatedBy(appointment.getCreatedBy());
            appointmentDto.setCreatedAt(appointment.getCreatedAt());
            appointmentDto.setUpdatedBy(appointment.getUpdatedBy());
            appointmentDto.setUpdatedAt(LocalDateTime.now());
            appointmentDto.setStatusId(appointment.getDoctor().getId());
            appointmentDtos.add(appointmentDto);
        }

        return appointmentDtos;

    }

    public AppointmentDto getAppointmentById(Long id) {
        Optional<Appointment> appointment = appointmentRepository.findById(id);
       if (appointment.isEmpty()){
           throw new HospitalBusinessException("no appointment found");
       }
       Appointment appointmentDb = appointment.get();
        AppointmentDto appointmentDto = new AppointmentDto();

        appointmentDto.setId(appointmentDb.getId());
            appointmentDto.setTiming(appointmentDb.getTiming());
            appointmentDto.setAppointmentType(appointmentDb.getAppointmentType());
            appointmentDto.setDoctorId(appointmentDb.getDoctor().getId());
        appointmentDto.setPatientId(appointmentDb.getPatient().getId());
        appointmentDto.setCreatedBy(appointmentDb.getCreatedBy());
        appointmentDto.setCreatedAt(appointmentDb.getCreatedAt());
        appointmentDto.setUpdatedBy(appointmentDb.getUpdatedBy());
            appointmentDto.setUpdatedAt(LocalDateTime.now());
            appointmentDto.setStatusId(appointmentDb.getStatus().getId());
        return appointmentDto;

    }

    public AppointmentDto createAppointment(AppointmentDto appointmentDto) {
        if (appointmentDto.getAppointmentType() == online) {
            appointmentDto.setStatusId(1L);
        } else if (appointmentDto.getAppointmentType() == onsite) {
            appointmentDto.setStatusId(2L);
        }

        if (patientRepository.findById(appointmentDto.getPatientId()).isEmpty()) {
            throw new HospitalBusinessException("no patient found");
        }
        if (doctorRepository.findById(appointmentDto.getDoctorId()).isEmpty()) {
            throw new HospitalBusinessException("no doctor found");
        }
        if (appointmentStatusRepository.findById(appointmentDto.getStatusId()).isEmpty()) {
            throw new HospitalBusinessException("invalid status id");
        }
        Appointment appointment = new Appointment();
        appointment.setId(appointmentDto.getId());
        appointment.setTiming(appointmentDto.getTiming());
        appointment.setAppointmentType(appointmentDto.getAppointmentType());
        appointment.setCreatedBy(appointmentDto.getCreatedBy());
        appointment.setCreatedAt(LocalDateTime.now());
        appointment.setUpdatedBy(appointmentDto.getUpdatedBy());
        appointment.setUpdatedAt(LocalDateTime.now());
        appointment.setPatient(patientRepository.findById(appointmentDto.getPatientId()).get());
        appointment.setDoctor(doctorRepository.findById(appointmentDto.getDoctorId()).get());
        appointment.setStatus(appointmentStatusRepository.findById(appointmentDto.getStatusId()).get());
        appointmentRepository.save(appointment);
    return appointmentDto;
    }

    public AppointmentDto updateAppointment(Long id, AppointmentDto appointmentDto) {
        if (patientRepository.findById(appointmentDto.getPatientId()).isEmpty()) {
            throw new HospitalBusinessException("no patient found");
        }
        if (doctorRepository.findById(appointmentDto.getDoctorId()).isEmpty()) {
            throw new HospitalBusinessException("no doctor found");
        }
        if (appointmentStatusRepository.findById(appointmentDto.getStatusId()).isEmpty()) {
            throw new HospitalBusinessException("invalid status id");
        }
        Optional<Appointment> appointmentTemp = appointmentRepository.findById(id);
        if (appointmentTemp.isPresent()) {
            Appointment appointment = appointmentTemp.get();
            appointment.setTiming(appointmentDto.getTiming());
            appointment.setAppointmentType(appointmentDto.getAppointmentType());
            appointment.setUpdatedAt(LocalDateTime.now());
            appointment.setUpdatedBy(appointmentDto.getUpdatedBy());
            appointment.setDoctor(doctorRepository.findById(appointmentDto.getDoctorId()).get());
            appointment.setPatient(patientRepository.findById(appointmentDto.getPatientId()).get());
            appointment.setStatus(appointmentStatusRepository.findById(appointmentDto.getStatusId()).get());
            appointmentRepository.save(appointment);
        }
        return appointmentDto;
    }

    public void deleteAppointment(Long id) {
        Optional<Appointment> appointment = appointmentRepository.findById(id);
        if (appointment.isEmpty())
            throw new HospitalBusinessException("appointment not found");
        else
            appointmentRepository.deleteById(id);

    }

    public BookResponseDto book(BookRequestDto bookRequestDto) {

        if (patientRepository.findById(bookRequestDto.getPatientId()).isEmpty()) {
            throw new HospitalBusinessException("no patient found");
        }
        if (doctorRepository.findById(bookRequestDto.getDoctorId()).isEmpty()) {
            throw new HospitalBusinessException("no doctor found");
        }
        if (appointmentStatusRepository.findById(1L).isEmpty()) {
            throw new HospitalBusinessException("invalid status id");
        }

        Appointment appointment = new Appointment();
        appointment.setTiming(bookRequestDto.getAppointmentTiming())
                .setAppointmentType(bookRequestDto.getAppointmentType())
                .setCreatedBy(bookRequestDto.getAppointmentCreatedBy())
                .setCreatedAt(LocalDateTime.now())
                .setUpdatedBy(bookRequestDto.getAppointmentUpdatedBy())
                .setUpdatedAt(LocalDateTime.now())
                .setPatient(patientRepository.findById(bookRequestDto.getPatientId()).get())
                .setDoctor(doctorRepository.findById(bookRequestDto.getDoctorId()).get())
                .setStatus(appointmentStatusRepository.findById(1L).get());
        appointmentRepository.save(appointment);

      List<Appointment> appointments = appointmentRepository.appointmentsStatusNewPaidPending();
        if (appointments.isEmpty())
            throw new HospitalBusinessException("there is no appointments before you");
        BookResponseDto responseDto = new BookResponseDto();
        responseDto.setNumberOfWaiting(((long) appointments.size()) - 1) // number of (new + paid + pending)
                .setStatus(appointment.getStatus());

        return responseDto;

    }

    public BookResponseDto bookWithPaid(BookRequestDto bookRequestDto) {
        if (patientRepository.findById(bookRequestDto.getPatientId()).isEmpty()) {
            throw new HospitalBusinessException("no patient found");
        }
        if (doctorRepository.findById(bookRequestDto.getDoctorId()).isEmpty()) {
            throw new HospitalBusinessException("no doctor found");
        }
        if (appointmentStatusRepository.findById(2L).isEmpty()) {
            throw new HospitalBusinessException("invalid status id");
        }

        Appointment appointment = new Appointment();
        appointment.setTiming(bookRequestDto.getAppointmentTiming())
                .setAppointmentType(bookRequestDto.getAppointmentType())
                .setCreatedBy(bookRequestDto.getAppointmentCreatedBy())
                .setCreatedAt(LocalDateTime.now())
                .setUpdatedBy(bookRequestDto.getAppointmentUpdatedBy())
                .setUpdatedAt(LocalDateTime.now())
                .setPatient(patientRepository.findById(bookRequestDto.getPatientId()).get())
                .setDoctor(doctorRepository.findById(bookRequestDto.getDoctorId()).get())
                .setStatus(appointmentStatusRepository.findById(2L).get());
        appointmentRepository.save(appointment);

        List<Appointment> appointments = appointmentRepository.appointmentsStatusPaidPending();
        if (appointments.isEmpty())
            throw new HospitalBusinessException("there is no appointments before you");
        BookResponseDto responseDto = new BookResponseDto();
        responseDto.setNumberOfWaiting(((long) appointments.size()) - 1) // number of (paid + pending)
                .setStatus(appointment.getStatus());

        return responseDto;
    }

    public BookResponseDto confirmBook(String phoneNumber) {
        Optional<Patient> patient = patientRepository.findByPhone(phoneNumber);
        if (patient.isEmpty()) {
            throw new HospitalBusinessException("no patient found");
        }
        Optional<Appointment> appointment = appointmentRepository.findFirstByPatientIdAndStatusIdOrderByCreatedAtAsc(patient.get().getId(),2L);
        if (appointment.isEmpty()) {
            throw new HospitalBusinessException("no appointments found");
        }
        Optional<AppointmentStatus> appointmentStatus = appointmentStatusRepository.findById(3L);
        if (appointmentStatus.isEmpty()) {
            throw new HospitalBusinessException("invalid status id");
        }
        appointment.get().setStatus(appointmentStatus.get());
        appointment.get().setUpdatedAt(LocalDateTime.now());
        appointmentRepository.save(appointment.get());
        BookResponseDto bookResponseDto = new BookResponseDto();
        bookResponseDto.setNumberOfWaiting(((long) appointmentRepository.appointmentsStatusPending().size())-1)              // number of (pending)
                .setStatus(appointment.get().getStatus());
        return bookResponseDto;
    }


    public PatientDto currentPatient() {
        List<Appointment> appointments = appointmentRepository.appointmentsStatusPending();
        if (appointments.isEmpty()) {
            throw new HospitalBusinessException("no appointments in pending");
        }
            Optional<Appointment> appointmentDb = appointments.stream().findFirst();
            Patient patientDb = appointmentDb.get().getPatient();
      List<MedicalRecordDto> medicalRecordDtos =  medicalRecordService.getByPatientId(patientDb.getId());
            PatientDto patientDto = new PatientDto();
            if (medicalRecordDtos.isEmpty()){
                patientDto.setId(patientDb.getId())
                        .setName(patientDb.getName())
                        .setGender(patientDb.getGender())
                        .setPhone(patientDb.getPhone())
                        .setDateOfBirth(patientDb.getDateOfBirth())
                        .setMedicalRecords(new ArrayList<>())
                        .setCreatedBy(patientDb.getCreatedBy())
                        .setCreatedAt(patientDb.getCreatedAt())
                        .setUpdatedBy(patientDb.getUpdatedBy())
                        .setUpdatedAt(patientDb.getUpdatedAt());
            }else{
            patientDto.setId(patientDb.getId())
                    .setName(patientDb.getName())
                    .setGender(patientDb.getGender())
                    .setPhone(patientDb.getPhone())
                    .setDateOfBirth(patientDb.getDateOfBirth())
                    .setMedicalRecords(medicalRecordDtos)
                    .setCreatedBy(patientDb.getCreatedBy())
                    .setCreatedAt(patientDb.getCreatedAt())
                    .setUpdatedBy(patientDb.getUpdatedBy())
                    .setUpdatedAt(patientDb.getUpdatedAt());
            }
            return patientDto;
    }


    public PatientDto next() {
        List<Appointment> appointments = appointmentRepository.appointmentsStatusPending();
        PatientDto patientDto = currentPatient();
       if (appointments.isEmpty()){
           throw new HospitalBusinessException("no appointments in pending");
       }
        Optional<Appointment> currentAppointment = appointments.stream().findFirst();
   currentAppointment.get().setStatus(new AppointmentStatus(5L,"finished","انتهاء"));
   currentAppointment.get().setUpdatedAt(LocalDateTime.now());
   appointmentRepository.save(currentAppointment.get());

        return patientDto;
    }



}
