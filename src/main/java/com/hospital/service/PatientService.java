package com.hospital.service;

import com.hospital.dto.MedicalRecordDto;
import com.hospital.dto.PatientDto;
import com.hospital.entity.MedicalRecord;
import com.hospital.entity.Patient;
import com.hospital.exception.HospitalBusinessException;
import com.hospital.repository.MedicalRecordRepository;
import com.hospital.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class PatientService {
    private final PatientRepository patientRepository;
    private final MedicalRecordRepository medicalRecordRepository;
    private final MedicalRecordService medicalRecordService;

    public List<PatientDto> getAllPatients() {
        List<Patient> patients = patientRepository.findAll();
        List<PatientDto> patientDtos = new ArrayList<>();
        if (!patients.isEmpty()) {
            for (Patient patient : patients) {
                PatientDto patientDto = new PatientDto();
                patientDto.setId(patient.getId())
                        .setDateOfBirth(patient.getDateOfBirth())
                        .setName(patient.getName())
                        .setPhone(patient.getPhone()).
                        setMedicalRecords(medicalRecordService.getByPatientId(patient.getId()))
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
        Optional<Patient> patient = patientRepository.findById(id);
        if (patient.isEmpty()){
            throw new HospitalBusinessException("no patient found");
        }
        PatientDto patientDto = new PatientDto();
            patientDto.setId(patient.get().getId())
                    .setDateOfBirth(patient.get().getDateOfBirth())
                    .setName(patient.get().getName())
                    .setGender(patient.get().getGender())
                    .setPhone(patient.get().getPhone())
                    .setMedicalRecords(medicalRecordService.getByPatientId(id))
                    .setCreatedBy(patient.get().getCreatedBy())
                    .setCreatedAt(patient.get().getCreatedAt())
                    .setUpdatedBy(patient.get().getUpdatedBy())
                    .setUpdatedAt(patient.get().getUpdatedAt());
            return patientDto;
    }


    public PatientDto addPatient(PatientDto patientDto) {
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

        return patientDto;

    }


    public PatientDto updatePatientData(Long id, PatientDto patientDto) {
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
            throw new HospitalBusinessException("no patient found");
        }
        return patientDto;


    }

    public void deletePatientById(Long id) {
        if (patientRepository.findById(id).isEmpty())
            throw new HospitalBusinessException("no patient found");
        else
            patientRepository.deleteById(id);
    }

    public List<MedicalRecordDto> showPatientHistory(Long id) {
        List<MedicalRecord> medicalRecords = medicalRecordRepository.findMedicalRecordsByPatientId(id);
        if (medicalRecords.isEmpty()){
            throw new HospitalBusinessException("no medical_records found");
        }
        List<MedicalRecordDto> medicalRecordDtos = new ArrayList<>();
        medicalRecords.forEach(medicalRecord -> {
                MedicalRecordDto medicalRecordDto = new MedicalRecordDto();
                medicalRecordDto.setId(medicalRecord.getId())
                        .setDiagnose(medicalRecord.getDiagnose())
                        .setTreatment(medicalRecord.getTreatment())
                        .setPatientId(medicalRecord.getPatient().getId())
                        .setDoctorId(medicalRecord.getDoctor().getId())
                        .setCreatedAt(medicalRecord.getCreatedAt())
                        .setCreatedBy(medicalRecord.getCreatedBy())
                        .setUpdatedAt(medicalRecord.getUpdatedAt())
                        .setUpdatedBy(medicalRecord.getUpdatedBy());
                medicalRecordDtos.add(medicalRecordDto);
            });
        return medicalRecordDtos;
    }
}
