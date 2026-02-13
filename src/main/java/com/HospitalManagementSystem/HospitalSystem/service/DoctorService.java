package com.HospitalManagementSystem.HospitalSystem.service;

import com.HospitalManagementSystem.HospitalSystem.dto.DoctorDto;
import com.HospitalManagementSystem.HospitalSystem.dto.MedicalRecordDto;
import com.HospitalManagementSystem.HospitalSystem.dto.PatientDto;
import com.HospitalManagementSystem.HospitalSystem.entity.Doctor;
import com.HospitalManagementSystem.HospitalSystem.entity.MedicalRecord;
import com.HospitalManagementSystem.HospitalSystem.entity.Patient;
import com.HospitalManagementSystem.HospitalSystem.repository.DoctorRepository;
import com.HospitalManagementSystem.HospitalSystem.repository.MedicalRecordRepository;
import com.HospitalManagementSystem.HospitalSystem.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DoctorService {
    @Autowired
    DoctorRepository doctorRepository;
    @Autowired
    AppointmentService appointmentService;
    @Autowired
    PatientRepository patientRepository;
    @Autowired
    MedicalRecordRepository medicalRecordRepository;


    public List<DoctorDto> getAllDoctors() {
        List<Doctor> doctors = doctorRepository.findAll();
        List<DoctorDto> doctorDtos = new ArrayList<>();
        for (Doctor doctor : doctors) {
            DoctorDto doctorDto = new DoctorDto();
            doctorDto.setId(doctor.getId());
            doctorDto.setName(doctor.getName());
            doctorDto.setSpeciality(doctor.getSpecialty());
            doctorDto.setContactNumber(doctor.getContactNumber());
            doctorDto.setCreatedBy(doctor.getCreatedBy());
            doctorDto.setCreatedAt(doctor.getCreatedAt());
            doctorDto.setUpdatedBy(doctor.getUpdatedBy());
            doctorDto.setUpdatedAt(doctor.getUpdatedAt());
            doctorDtos.add(doctorDto);
        }
        return doctorDtos;

    }

    public DoctorDto getDoctorById(Long id) {
        Doctor doc = doctorRepository.findById(id).orElse(null);
        DoctorDto doctorDto = new DoctorDto();
        if (doc != null) {
            doctorDto.setId(doc.getId());
            doctorDto.setName(doc.getName());
            doctorDto.setSpeciality(doc.getSpecialty());
            doctorDto.setContactNumber(doc.getContactNumber());
            doctorDto.setCreatedBy(doc.getCreatedBy());
            doctorDto.setCreatedAt(doc.getCreatedAt());
            doctorDto.setUpdatedBy(doc.getUpdatedBy());
            doctorDto.setUpdatedAt(doc.getUpdatedAt());
            return doctorDto;
        }else {
            return null;
        }

    }

    public void addDoctor(DoctorDto doctorDto) {
        Doctor doctor = new Doctor();
        doctor.setId(doctorDto.getId());
        doctor.setName(doctorDto.getName());
        doctor.setSpecialty(doctorDto.getSpeciality());
        doctor.setContactNumber(doctorDto.getContactNumber());
        doctor.setCreatedBy(doctorDto.getCreatedBy());
        doctor.setCreatedAt(LocalDateTime.now());
        doctor.setUpdatedBy(doctorDto.getUpdatedBy());
        doctor.setUpdatedAt(LocalDateTime.now());

        doctorRepository.save(doctor);
    }

    public void updateDoctorData(Long id, DoctorDto doctorDto) {
        Optional<Doctor> doctor = doctorRepository.findById(id);
        if (doctor.isPresent()) {
            Doctor doc = doctor.get();
            doc.setName(doctorDto.getName());
            doc.setSpecialty(doctorDto.getSpeciality());
            doc.setContactNumber(doctorDto.getContactNumber());
            doc.setUpdatedAt(LocalDateTime.now());
            doc.setUpdatedBy(doctorDto.getUpdatedBy());
            doctorRepository.save(doc);
        }

    }


    public boolean deleteDoctorById(Long id) {
        if (doctorRepository.findById(id).isPresent()) {
            doctorRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    public PatientDto startSession(MedicalRecordDto medicalRecordDto) {
        PatientDto patientDto = appointmentService.next();
        MedicalRecord medicalRecordDb = new MedicalRecord();
        medicalRecordDb.setDiagnose(medicalRecordDto.getDiagnose())
                .setTreatment(medicalRecordDto.getTreatment())
                .setCreatedAt(LocalDateTime.now())
                .setCreatedBy(medicalRecordDto.getCreatedBy())
                .setUpdatedAt(LocalDateTime.now())
                .setUpdatedBy(medicalRecordDto.getUpdatedBy())
                .setPatient(patientRepository.findById(medicalRecordDto.getPatientId()).get())
                .setDoctor(doctorRepository.findById(medicalRecordDto.getDoctorId()).get());
        medicalRecordRepository.save(medicalRecordDb);
        return patientDto;
    }
}
