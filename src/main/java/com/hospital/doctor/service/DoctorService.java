package com.hospital.doctor.service;

import com.hospital.common.security.JWTService;
import com.hospital.doctor.dto.request.CreateDoctorRequest;
import com.hospital.doctor.dto.request.UpdateDoctorRequest;
import com.hospital.doctor.dto.response.CreateDoctorResponse;
import com.hospital.doctor.dto.response.GetDoctorResponse;
import com.hospital.doctor.dto.response.UpdateDoctorResponse;
import com.hospital.medicalRecord.dto.request.CreateMedicalRecordRequest;
import com.hospital.entity.Doctor;
import com.hospital.entity.MedicalRecord;
import com.hospital.entity.Patient;
import com.hospital.common.exception.HospitalBusinessException;
import com.hospital.doctor.repository.DoctorRepository;
import com.hospital.medicalRecord.repository.MedicalRecordRepository;
import com.hospital.patient.dto.response.GetPatientResponse;
import com.hospital.patient.repository.PatientRepository;
import com.hospital.appointment.service.AppointmentService;
import com.hospital.speciality.repository.SpecialityRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DoctorService {
   private final DoctorRepository doctorRepository;
   private final AppointmentService appointmentService;
   private final PatientRepository patientRepository;
   private final MedicalRecordRepository medicalRecordRepository;
    private final SpecialityRepository specialityRepository;
   private final JWTService jwtService;

    public List<GetDoctorResponse> getAllDoctors() {
        List<Doctor> doctors = doctorRepository.findAll();
        List<GetDoctorResponse> getDoctorResponses = new ArrayList<>();
        for (Doctor doctor : doctors) {
            GetDoctorResponse getDoctorResponse = new GetDoctorResponse();
            getDoctorResponse.setId(doctor.getId());
            getDoctorResponse.setName(doctor.getName());
            getDoctorResponse.setSpeciality(doctor.getSpeciality());
            getDoctorResponse.setContactNumber(doctor.getContactNumber());
            getDoctorResponse.setCreatedBy(doctor.getCreatedBy());
            getDoctorResponse.setCreatedAt(doctor.getCreatedAt());
            getDoctorResponse.setUpdatedBy(doctor.getUpdatedBy());
            getDoctorResponse.setUpdatedAt(doctor.getUpdatedAt());
            getDoctorResponses.add(getDoctorResponse);
        }
        return getDoctorResponses;

    }

    public GetDoctorResponse getDoctorById(Long id) {
        Optional<Doctor> doctorDb = doctorRepository.findById(id);
        if (doctorDb.isEmpty()) {
            throw new HospitalBusinessException("no doctor found");
        } else {
            Doctor doc = doctorDb.get();
            GetDoctorResponse getDoctorResponse = new GetDoctorResponse();
            getDoctorResponse.setId(doc.getId());
            getDoctorResponse.setName(doc.getName());
            getDoctorResponse.setSpeciality(doc.getSpeciality());
            getDoctorResponse.setContactNumber(doc.getContactNumber());
            getDoctorResponse.setCreatedBy(doc.getCreatedBy());
            getDoctorResponse.setCreatedAt(doc.getCreatedAt());
            getDoctorResponse.setUpdatedBy(doc.getUpdatedBy());
            getDoctorResponse.setUpdatedAt(doc.getUpdatedAt());
            return getDoctorResponse;

        }

    }

    public CreateDoctorResponse addDoctor(CreateDoctorRequest createDoctorRequest) {

        Doctor doctor = new Doctor();
        doctor.setId(createDoctorRequest.getId());
        doctor.setName(createDoctorRequest.getName());
        doctor.setSpeciality(specialityRepository.findById(createDoctorRequest.getSpecialityId()).get());
        doctor.setContactNumber(createDoctorRequest.getContactNumber());
        doctor.setCreatedAt(LocalDateTime.now());
        doctor.setUpdatedAt(LocalDateTime.now());
        doctorRepository.save(doctor);
        CreateDoctorResponse doctorResponse = new CreateDoctorResponse();
        doctorResponse.setId(doctor.getId());

        return doctorResponse;
    }

    public UpdateDoctorResponse updateDoctorData(UpdateDoctorRequest doctorRequest) {

        Optional<Doctor> doctor = doctorRepository.findById(doctorRequest.getId());
        if (doctor.isPresent()) {
            Doctor doc = doctor.get();
            doc.setName(doctorRequest.getName());
            doc.setSpeciality(specialityRepository.findById(doctorRequest.getSpecialityId()).get());
            doc.setContactNumber(doctorRequest.getContactNumber());
            doc.setUpdatedAt(LocalDateTime.now());
            doctorRepository.save(doc);
            UpdateDoctorResponse doctorResponse = new UpdateDoctorResponse();
            doctorResponse.setId(doc.getId());
            return doctorResponse;

        } else {
            throw new HospitalBusinessException("no doctor found");
        }
    }


    public void deleteDoctorById(Long id) {
        Optional<Doctor> doctorDb = doctorRepository.findById(id);
        if (doctorDb.isEmpty()) {
            throw new HospitalBusinessException("no doctor found");
        }
        doctorRepository.deleteById(id);


    }

    // still need for (debug and test)
    public GetPatientResponse startSession(CreateMedicalRecordRequest createMedicalRecordRequest) {


        Optional<Patient> patientDb = patientRepository.findById(createMedicalRecordRequest.getPatientId());
        Optional<Doctor> doctorDb = doctorRepository.findById(createMedicalRecordRequest.getDoctorId());
        if (patientDb.isEmpty()) {
            throw new HospitalBusinessException("no patient found");
        }
        if (doctorDb.isEmpty()) {
            throw new HospitalBusinessException("no doctor found");
        }

        GetPatientResponse patientResponse = appointmentService.next();
        MedicalRecord medicalRecordDb = new MedicalRecord();
        medicalRecordDb.setDiagnose(createMedicalRecordRequest.getDiagnose())
                .setTreatment(createMedicalRecordRequest.getTreatment())
                .setPatient(patientDb.get())
                .setDoctor(doctorDb.get())
                .setCreatedAt(LocalDateTime.now())
                .setUpdatedAt(LocalDateTime.now());
        medicalRecordRepository.save(medicalRecordDb);
        return patientResponse;
    }
}
