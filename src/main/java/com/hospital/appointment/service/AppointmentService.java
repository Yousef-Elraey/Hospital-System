package com.hospital.appointment.service;

import com.hospital.appointment.dto.request.CreateAppointmentRequest;
import com.hospital.appointment.dto.request.UpdateAppointmentRequest;
import com.hospital.appointment.dto.response.CreateAppointmentResponse;
import com.hospital.appointment.dto.response.GetAppointmentResponse;
import com.hospital.appointment.dto.response.UpdateAppointmentResponse;
import com.hospital.common.security.JWTService;
import com.hospital.dto.*;
import com.hospital.entity.Appointment;
import com.hospital.entity.AppointmentStatus;
import com.hospital.entity.Patient;
import com.hospital.common.exception.HospitalBusinessException;
import com.hospital.appointment.repository.AppointmentRepository;
import com.hospital.medicalRecord.dto.response.GetMedicalRecordResponse;
import com.hospital.patient.dto.response.GetPatientResponse;
import com.hospital.status.repository.AppointmentStatusRepository;
import com.hospital.doctor.repository.DoctorRepository;
import com.hospital.patient.repository.PatientRepository;
import com.hospital.medicalRecord.service.MedicalRecordService;
import jakarta.servlet.http.HttpServletRequest;
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
    private final JWTService jwtService;


    public List<GetAppointmentResponse> getAllAppointments() {
        List<Appointment> appointments = appointmentRepository.findAll();
        if (appointments.isEmpty()) {
            throw new HospitalBusinessException("no appointments found");
        }
        List<GetAppointmentResponse> appointmentsResponse = new ArrayList<>();

        for (Appointment appointment : appointments) {
            GetAppointmentResponse appointmentResponse = new GetAppointmentResponse();

            appointmentResponse.setId(appointment.getId());
            appointmentResponse.setTiming(appointment.getTiming());
            appointmentResponse.setAppointmentType(appointment.getAppointmentType());
            appointmentResponse.setDoctorId(appointment.getDoctor().getId());
            appointmentResponse.setPatientId(appointment.getPatient().getId());
            appointmentResponse.setCreatedBy(appointment.getCreatedBy());
            appointmentResponse.setCreatedAt(appointment.getCreatedAt());
            appointmentResponse.setUpdatedBy(appointment.getUpdatedBy());
            appointmentResponse.setUpdatedAt(LocalDateTime.now());
            appointmentResponse.setStatusId(appointment.getDoctor().getId());

            appointmentsResponse.add(appointmentResponse);
        }

        return appointmentsResponse;

    }

    public GetAppointmentResponse getAppointmentById(Long id) {
        Optional<Appointment> appointment = appointmentRepository.findById(id);
        if (appointment.isEmpty()) {
            throw new HospitalBusinessException("no appointment found");
        }
        Appointment appointmentDb = appointment.get();
        GetAppointmentResponse appointmentResponse = new GetAppointmentResponse();

        appointmentResponse.setId(appointmentDb.getId());
        appointmentResponse.setTiming(appointmentDb.getTiming());
        appointmentResponse.setAppointmentType(appointmentDb.getAppointmentType());
        appointmentResponse.setDoctorId(appointmentDb.getDoctor().getId());
        appointmentResponse.setPatientId(appointmentDb.getPatient().getId());
        appointmentResponse.setCreatedBy(appointmentDb.getCreatedBy());
        appointmentResponse.setCreatedAt(appointmentDb.getCreatedAt());
        appointmentResponse.setUpdatedBy(appointmentDb.getUpdatedBy());
        appointmentResponse.setUpdatedAt(LocalDateTime.now());
        appointmentResponse.setStatusId(appointmentDb.getStatus().getId());
        return appointmentResponse;

    }

    public CreateAppointmentResponse createAppointment(CreateAppointmentRequest createAppointmentRequest, HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring(7);

        if (createAppointmentRequest.getAppointmentType() == online) {
            createAppointmentRequest.setStatusId(1L);
        } else if (createAppointmentRequest.getAppointmentType() == onsite) {
            createAppointmentRequest.setStatusId(2L);
        }

        if (patientRepository.findById(createAppointmentRequest.getPatientId()).isEmpty()) {
            throw new HospitalBusinessException("no patient found");
        }
        if (doctorRepository.findById(createAppointmentRequest.getDoctorId()).isEmpty()) {
            throw new HospitalBusinessException("no doctor found");
        }
        if (appointmentStatusRepository.findById(createAppointmentRequest.getStatusId()).isEmpty()) {
            throw new HospitalBusinessException("invalid status id");
        }
        Appointment appointment = new Appointment();
        appointment.setId(createAppointmentRequest.getId());
        appointment.setTiming(createAppointmentRequest.getTiming());
        appointment.setAppointmentType(createAppointmentRequest.getAppointmentType());
        appointment.setCreatedBy(jwtService.extractUserName(token));
        appointment.setCreatedAt(LocalDateTime.now());
        appointment.setUpdatedBy(jwtService.extractUserName(token));
        appointment.setUpdatedAt(LocalDateTime.now());
        appointment.setPatient(patientRepository.findById(createAppointmentRequest.getPatientId()).get());
        appointment.setDoctor(doctorRepository.findById(createAppointmentRequest.getDoctorId()).get());
        appointment.setStatus(appointmentStatusRepository.findById(createAppointmentRequest.getStatusId()).get());
        appointmentRepository.save(appointment);
        CreateAppointmentResponse appointmentResponse = new CreateAppointmentResponse();
        appointmentResponse.setId(appointment.getId());

        return appointmentResponse;

    }

    public UpdateAppointmentResponse updateAppointment(UpdateAppointmentRequest updateAppointmentRequest, HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring(7);

        if (patientRepository.findById(updateAppointmentRequest.getPatientId()).isEmpty()) {
            throw new HospitalBusinessException("no patient found");
        }
        if (doctorRepository.findById(updateAppointmentRequest.getDoctorId()).isEmpty()) {
            throw new HospitalBusinessException("no doctor found");
        }
        if (appointmentStatusRepository.findById(updateAppointmentRequest.getStatusId()).isEmpty()) {
            throw new HospitalBusinessException("invalid status id");
        }
        Optional<Appointment> appointmentTemp = appointmentRepository.findById(updateAppointmentRequest.getId());
        if (appointmentTemp.isPresent()) {
            Appointment appointment = appointmentTemp.get();
            appointment.setTiming(updateAppointmentRequest.getTiming());
            appointment.setAppointmentType(updateAppointmentRequest.getAppointmentType());
            appointment.setUpdatedAt(LocalDateTime.now());
            appointment.setUpdatedBy(jwtService.extractUserName(token));
            appointment.setDoctor(doctorRepository.findById(updateAppointmentRequest.getDoctorId()).get());
            appointment.setPatient(patientRepository.findById(updateAppointmentRequest.getPatientId()).get());
            appointment.setStatus(appointmentStatusRepository.findById(updateAppointmentRequest.getStatusId()).get());
            appointmentRepository.save(appointment);
            UpdateAppointmentResponse appointmentResponse = new UpdateAppointmentResponse();
            appointmentResponse.setId(appointment.getId());
            return appointmentResponse;
        }else
            throw new HospitalBusinessException("no appointment found");
    }

    public void deleteAppointment(Long id) {
        Optional<Appointment> appointment = appointmentRepository.findById(id);
        if (appointment.isEmpty())
            throw new HospitalBusinessException("appointment not found");
        else
            appointmentRepository.deleteById(id);

    }

    public BookResponseDto book(BookRequestDto bookRequestDto,HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring(7);

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
                .setCreatedBy(jwtService.extractUserName(token))
                .setCreatedAt(LocalDateTime.now())
                .setUpdatedBy(jwtService.extractUserName(token))
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

    public BookResponseDto bookWithPaid(BookRequestDto bookRequestDto,HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring(7);

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
                .setCreatedBy(jwtService.extractUserName(token))
                .setCreatedAt(LocalDateTime.now())
                .setUpdatedBy(jwtService.extractUserName(token))
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


    public GetPatientResponse currentPatient(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring(7);

        List<Appointment> appointments = appointmentRepository.appointmentsStatusPending();
        if (appointments.isEmpty()) {
            throw new HospitalBusinessException("no appointments in pending");
        }
        Optional<Appointment> appointmentDb = appointments.stream().findFirst();
        Patient patientDb = appointmentDb.get().getPatient();
        List<GetMedicalRecordResponse> medicalRecordsResponse = medicalRecordService.getByPatientId(patientDb.getId(),request);
        GetPatientResponse patientResponse = new GetPatientResponse();
        if (medicalRecordsResponse.isEmpty()) {
            patientResponse.setId(patientDb.getId())
                    .setName(patientDb.getName())
                    .setGender(patientDb.getGender())
                    .setPhone(patientDb.getPhone())
                    .setDateOfBirth(patientDb.getDateOfBirth())
                    .setMedicalRecords(new ArrayList<>())
                    .setCreatedBy(jwtService.extractUserName(token))
                    .setCreatedAt(patientDb.getCreatedAt())
                    .setUpdatedBy(jwtService.extractUserName(token))
                    .setUpdatedAt(patientDb.getUpdatedAt());
        } else {
            patientResponse.setId(patientDb.getId())
                    .setName(patientDb.getName())
                    .setGender(patientDb.getGender())
                    .setPhone(patientDb.getPhone())
                    .setDateOfBirth(patientDb.getDateOfBirth())
                    .setMedicalRecords(medicalRecordsResponse)
                    .setCreatedBy(jwtService.extractUserName(token))
                    .setCreatedAt(patientDb.getCreatedAt())
                    .setUpdatedBy(jwtService.extractUserName(token))
                    .setUpdatedAt(patientDb.getUpdatedAt());
        }
        return patientResponse;
    }


    public GetPatientResponse next(HttpServletRequest request) {
        List<Appointment> appointments = appointmentRepository.appointmentsStatusPending();
        GetPatientResponse patientResponse = currentPatient(request);
        if (appointments.isEmpty()) {
            throw new HospitalBusinessException("no appointments in pending");
        }
        Optional<Appointment> currentAppointment = appointments.stream().findFirst();
        currentAppointment.get().setStatus(new AppointmentStatus(5L, "finished", "انتهاء"));
        currentAppointment.get().setUpdatedAt(LocalDateTime.now());
        appointmentRepository.save(currentAppointment.get());

        return patientResponse;
    }



}
