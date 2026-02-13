package com.HospitalManagementSystem.HospitalSystem.service;

import com.HospitalManagementSystem.HospitalSystem.dto.PatientDto;
import com.HospitalManagementSystem.HospitalSystem.entity.Patient;
import com.HospitalManagementSystem.HospitalSystem.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;


@Service
public class PatientService {
    @Autowired
    PatientRepository patientRepository;

    public List<PatientDto> getAllPatients() {
        List<Patient> patients = patientRepository.findAll();
        List<PatientDto> patientDtos = new ArrayList<>();
        if (!patients.isEmpty()) {
            for (Patient patient : patients) {
                PatientDto patientDto = new PatientDto();
                patientDto.setId(patient.getId())
                        .setDateOfBirth(patient.getDateOfBirth())
                        .setName(patient.getName())
                        .setPhone(patient.getPhone())
                        .setGender(patient.getGender())
                        .setCreatedAt(patient.getCreatedAt())
                        .setUpdatedAt(patient.getUpdatedAt())
                        .setCreatedBy(patient.getCreatedBy())
                        .setUpdatedBy(patient.getUpdatedBy());
                patientDtos.add(patientDto);
            }
        }
        return patientDtos;
    }

    public PatientDto getPatientById(Long id) {
        Patient patient = patientRepository.findById(id).orElse(null);
        PatientDto patientDto = new PatientDto();
        if (patient != null) {
            patientDto.setId(patient.getId())
                    .setDateOfBirth(patient.getDateOfBirth())
                    .setName(patient.getName())
                    .setGender(patient.getGender())
                    .setPhone(patient.getPhone())
                    .setCreatedBy(patient.getCreatedBy())
                    .setCreatedAt(patient.getCreatedAt())
                    .setUpdatedBy(patient.getUpdatedBy())
                    .setUpdatedAt(patient.getUpdatedAt());
            return patientDto;
        }else {
            return null;
        }


    }


    public void addPatient(PatientDto patientDto) {
        Patient patient = new Patient();
        patient.setId(patientDto.getId())
                .setName(patientDto.getName())
                .setGender(patientDto.getGender())
                .setPhone(patientDto.getPhone())
                .setDateOfBirth(patientDto.getDateOfBirth())
                .setCreatedBy(patientDto.getCreatedBy())
                .setCreatedAt(LocalDateTime.now())
                .setUpdatedBy(patientDto.getUpdatedBy())
                .setUpdatedAt(LocalDateTime.now());
        patientRepository.save(patient);


    }


    public void updatePatientData(Long id, PatientDto patientDto) {
        Optional<Patient> patientTemp = patientRepository.findById(id);
        if (patientTemp.isPresent()) {
            Patient dbPatient = patientTemp.get();
            dbPatient.setName(patientDto.getName())
                    .setGender(patientDto.getGender())
                    .setPhone(patientDto.getPhone())
                    .setDateOfBirth(patientDto.getDateOfBirth())
                    .setUpdatedBy(patientDto.getUpdatedBy())
                    .setUpdatedAt(LocalDateTime.now());
            patientRepository.save(dbPatient);
        } else {
            addPatient(patientDto);
        }


    }

    public boolean deletePatientById(Long id) {
        if (patientRepository.findById(id).isPresent()) {
            patientRepository.deleteById(id);
            return true;
        } else {
            return false;
        }

    }
}
