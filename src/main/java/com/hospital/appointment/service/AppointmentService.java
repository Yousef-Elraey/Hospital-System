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

            appointmentResponse
                    .setId(appointment.getId())
                    .setTiming(appointment.getTiming())
                    .setAppointmentType(appointment.getAppointmentType())
                    .setDoctorId(appointment.getDoctor().getId())
                    .setPatientId(appointment.getPatient().getId())
                    .setCreatedBy(appointment.getCreatedBy())
                    .setCreatedAt(appointment.getCreatedAt())
                    .setUpdatedBy(appointment.getUpdatedBy())
                    .setUpdatedAt(LocalDateTime.now())
                    .setStatusId(appointment.getDoctor().getId());

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

        appointmentResponse
                .setId(appointmentDb.getId())
                .setTiming(appointmentDb.getTiming())
                .setAppointmentType(appointmentDb.getAppointmentType())
                .setDoctorId(appointmentDb.getDoctor().getId())
                .setPatientId(appointmentDb.getPatient().getId())
                .setCreatedBy(appointmentDb.getCreatedBy())
                .setCreatedAt(appointmentDb.getCreatedAt())
                .setUpdatedBy(appointmentDb.getUpdatedBy())
                .setUpdatedAt(LocalDateTime.now())
                .setStatusId(appointmentDb.getStatus().getId());
        return appointmentResponse;

    }

    public CreateAppointmentResponse createAppointment(CreateAppointmentRequest createAppointmentRequest) {

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
        appointment.setCreatedAt(LocalDateTime.now());
        appointment.setUpdatedAt(LocalDateTime.now());
        appointment.setPatient(patientRepository.findById(createAppointmentRequest.getPatientId()).get());
        appointment.setDoctor(doctorRepository.findById(createAppointmentRequest.getDoctorId()).get());
        appointment.setStatus(appointmentStatusRepository.findById(createAppointmentRequest.getStatusId()).get());
        appointmentRepository.save(appointment);
        CreateAppointmentResponse appointmentResponse = new CreateAppointmentResponse();
        appointmentResponse.setId(appointment.getId());

        return appointmentResponse;

    }

    public UpdateAppointmentResponse updateAppointment(UpdateAppointmentRequest updateAppointmentRequest) {

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
//        if (appointmentRepository.findFirstByPatientIdOrderByCreatedAtAsc(bookRequestDto.getPatientId()).isPresent()) {
//            throw new HospitalBusinessException("");
//        }
        Appointment appointment = new Appointment();
        appointment.setTiming(bookRequestDto.getAppointmentTiming())
                .setAppointmentType(bookRequestDto.getAppointmentType())
                .setPatient(patientRepository.findById(bookRequestDto.getPatientId()).get())
                .setDoctor(doctorRepository.findById(bookRequestDto.getDoctorId()).get())
                .setStatus(appointmentStatusRepository.findById(1L).get())
                .setCreatedAt(LocalDateTime.now())
                .setUpdatedAt(LocalDateTime.now());
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
                .setPatient(patientRepository.findById(bookRequestDto.getPatientId()).get())
                .setDoctor(doctorRepository.findById(bookRequestDto.getDoctorId()).get())
                .setStatus(appointmentStatusRepository.findById(2L).get())
                .setCreatedAt(LocalDateTime.now())
                .setUpdatedAt(LocalDateTime.now());
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

        Optional<Appointment> appointment1 = appointmentRepository.findFirstByPatientIdAndStatusIdOrderByCreatedAtAsc(patient.get().getId(), 1L);

        Optional<Appointment> appointment2 = appointmentRepository.findFirstByPatientIdAndStatusIdOrderByCreatedAtAsc(patient.get().getId(), 2L);

        if (appointment1.isEmpty() && appointment2.isEmpty()) {
            throw new HospitalBusinessException("no appointments found");
        }
        Optional<AppointmentStatus> appointmentStatus = appointmentStatusRepository.findById(3L);
        if (appointmentStatus.isEmpty()) {
            throw new HospitalBusinessException("invalid status id");
        }

        if (appointment1.isPresent()) {
            Appointment appointmentDb1 = appointment1.get();
            appointmentDb1.setStatus(appointmentStatus.get());
            appointmentDb1.setUpdatedAt(LocalDateTime.now());
            appointmentRepository.save(appointmentDb1);

            BookResponseDto bookResponseDto = new BookResponseDto();
            bookResponseDto.setNumberOfWaiting(((long) appointmentRepository.appointmentsStatusPending().size()) - 1)              // number of (pending)
                    .setStatus(appointmentStatus.get());
            return bookResponseDto;
        }

        Appointment appointmentDb2 = appointment2.get();
        appointmentDb2.setStatus(appointmentStatus.get());
        appointmentDb2.setUpdatedAt(LocalDateTime.now());
        appointmentRepository.save(appointmentDb2);

        BookResponseDto bookResponseDto = new BookResponseDto();
        bookResponseDto.setNumberOfWaiting(((long) appointmentRepository.appointmentsStatusPending().size()) - 1)              // number of (pending)
                .setStatus(appointmentStatus.get());
        return bookResponseDto;
    }


    public GetPatientResponse currentPatient() {


        List<Appointment> appointments = appointmentRepository.appointmentsStatusPending();
        if (appointments.isEmpty()) {
            throw new HospitalBusinessException("no appointments in pending");
        }
        Optional<Appointment> appointmentDb = appointments.stream().findFirst();
        Patient patientDb = appointmentDb.get().getPatient();
        List<GetMedicalRecordResponse> medicalRecordsResponse = medicalRecordService.getByPatientId(patientDb.getId());
        GetPatientResponse patientResponse = new GetPatientResponse();
        if (medicalRecordsResponse.isEmpty()) {
            patientResponse.setId(patientDb.getId())
                    .setName(patientDb.getName())
                    .setGender(patientDb.getGender())
                    .setPhone(patientDb.getPhone())
                    .setDateOfBirth(patientDb.getDateOfBirth())
                    .setMedicalRecords(new ArrayList<>())
                    .setCreatedAt(patientDb.getCreatedAt())
                    .setUpdatedAt(patientDb.getUpdatedAt());
        } else {
            patientResponse.setId(patientDb.getId())
                    .setName(patientDb.getName())
                    .setGender(patientDb.getGender())
                    .setPhone(patientDb.getPhone())
                    .setDateOfBirth(patientDb.getDateOfBirth())
                    .setMedicalRecords(medicalRecordsResponse)
                    .setCreatedAt(patientDb.getCreatedAt())
                    .setUpdatedAt(patientDb.getUpdatedAt());
        }
        return patientResponse;
    }


    public GetPatientResponse next() {
        List<Appointment> appointments = appointmentRepository.appointmentsStatusPending();
        GetPatientResponse patientResponse = currentPatient();
        if (appointments.isEmpty()) {
            throw new HospitalBusinessException("no appointments in pending");
        }
        Optional<Appointment> currentAppointment = appointments.stream().findFirst();
        Appointment currentAppointmentDb = currentAppointment.get();
        currentAppointmentDb.setStatus(new AppointmentStatus(5L, "finished", "انتهاء"));
        currentAppointmentDb.setUpdatedAt(LocalDateTime.now());
        appointmentRepository.save(currentAppointmentDb);

        return patientResponse;
    }



}
