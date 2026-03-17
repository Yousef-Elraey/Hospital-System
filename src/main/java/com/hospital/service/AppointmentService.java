package com.hospital.service;

import com.hospital.dto.AppointmentDto;
import com.hospital.dto.BookRequestDto;
import com.hospital.dto.BookResponseDto;
import com.hospital.dto.PatientDto;
import com.hospital.entity.Appointment;
import com.hospital.entity.AppointmentStatus;
import com.hospital.entity.Patient;
import com.hospital.exception.HospitalBusinessException;
import com.hospital.repository.AppointmentRepository;
import com.hospital.repository.AppointmentStatusRepository;
import com.hospital.repository.DoctorRepository;
import com.hospital.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.hospital.entity.AppointmentType.online;
import static com.hospital.entity.AppointmentType.onsite;
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
    @Autowired
    AppointmentStatusRepository appointmentStatusRepository;


    public List<AppointmentDto> getAllAppointments() {
        List<Appointment> appointments = appointmentRepository.findAll();
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
        Appointment appointment = appointmentRepository.findById(id).orElse(null);
        AppointmentDto appointmentDto = new AppointmentDto();

        if (appointment != null){
        appointmentDto.setId(appointment.getId());
            appointmentDto.setTiming(appointment.getTiming());
            appointmentDto.setAppointmentType(appointment.getAppointmentType());
            appointmentDto.setDoctorId(appointment.getDoctor().getId());
        appointmentDto.setPatientId(appointment.getPatient().getId());
        appointmentDto.setCreatedBy(appointment.getCreatedBy());
        appointmentDto.setCreatedAt(appointment.getCreatedAt());
        appointmentDto.setUpdatedBy(appointment.getUpdatedBy());
            appointmentDto.setUpdatedAt(LocalDateTime.now());
            appointmentDto.setStatusId(appointment.getStatus().getId());
        return appointmentDto;
        }else {
            return null;
        }
    }

    public void createAppointment(AppointmentDto appointmentDto) {
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
    }

    public void updateAppointment(Long id, AppointmentDto appointmentDto) {
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
        } else {
            throw new HospitalBusinessException("no appointment found");
        }

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

    // still need for handle
    public PatientDto currentPatient() {
        Optional<Appointment> appointment = appointmentRepository.findFirstByOrderByIdAsc();

        if (appointment.isPresent()) {
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
        } else
            return new PatientDto();
    }


    //still need for handle
    public PatientDto next() {
        Optional<Appointment> appointment = appointmentRepository.findFirstByOrderByIdAsc();
        PatientDto patientDto = currentPatient();
        if (appointment.isPresent()) {
            appointmentRepository.delete(appointment.get());
        } else {
            throw new HospitalBusinessException("there is no appointments");
        }
        return patientDto;
    }



}
