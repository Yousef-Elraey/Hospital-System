package com.HospitalManagementSystem.HospitalSystem.service;

import com.HospitalManagementSystem.HospitalSystem.dto.MedicalRecordDto;
import com.HospitalManagementSystem.HospitalSystem.entity.MedicalRecord;
import com.HospitalManagementSystem.HospitalSystem.repository.DoctorRepository;
import com.HospitalManagementSystem.HospitalSystem.repository.MedicalRecordRepository;
import com.HospitalManagementSystem.HospitalSystem.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class MedicalRecordService {
    @Autowired
    MedicalRecordRepository repo;
    @Autowired
    PatientRepository patientRepository;
    @Autowired
    DoctorRepository doctorRepository;

    public List<MedicalRecordDto> getAllRecords() {
        List<MedicalRecord> medicalRecords = repo.findAll();
        List<MedicalRecordDto> medicalRecordDtos = new ArrayList<>();
        if (!medicalRecords.isEmpty()) {
            for (MedicalRecord record : medicalRecords) {
                MedicalRecordDto medicalRecordDto = new MedicalRecordDto();
                medicalRecordDto.setId(record.getId())
                        .setDiagnose(record.getDiagnose())
                        .setTreatment(record.getTreatment())
                        .setPatientId(record.getPatient().getId())
                        .setDoctorId(record.getDoctor().getId())
                        .setCreatedAt(record.getCreatedAt())
                        .setCreatedBy(record.getCreatedBy())
                        .setUpdatedAt(record.getUpdatedAt())
                        .setUpdatedBy(record.getUpdatedBy());
                medicalRecordDtos.add(medicalRecordDto);

            }
        }

        return medicalRecordDtos;

    }

    public MedicalRecordDto getMedicalRecordById(Long id) {
        MedicalRecord medicalRecord = repo.findById(id).orElse(null);
        MedicalRecordDto medicalRecordDto = new MedicalRecordDto();
        if (medicalRecord != null) {
            medicalRecordDto.setId(medicalRecord.getId())
                    .setDiagnose(medicalRecord.getDiagnose())
                    .setTreatment(medicalRecord.getTreatment())
                    .setPatientId(medicalRecord.getPatient().getId())
                    .setDoctorId(medicalRecord.getDoctor().getId())
                    .setCreatedAt(medicalRecord.getCreatedAt())
                    .setCreatedBy(medicalRecord.getCreatedBy())
                    .setUpdatedAt(medicalRecord.getUpdatedAt())
                    .setUpdatedBy(medicalRecord.getUpdatedBy());
            return medicalRecordDto;
        }else {
            return null;
        }

    }

    public void addMedicalRecord(MedicalRecordDto medicalRecordDto) {
        MedicalRecord dbMedicalRecord = new MedicalRecord();
        dbMedicalRecord.setId(medicalRecordDto.getId())
                .setDiagnose(medicalRecordDto.getDiagnose())
                .setTreatment(medicalRecordDto.getTreatment())
                .setCreatedAt(LocalDateTime.now())
                .setCreatedBy(medicalRecordDto.getCreatedBy())
                .setUpdatedAt(LocalDateTime.now())
                .setUpdatedBy(medicalRecordDto.getUpdatedBy())
                .setPatient(patientRepository.findById(medicalRecordDto.getPatientId()).get())
                .setDoctor(doctorRepository.findById(medicalRecordDto.getDoctorId()).get());

        repo.save(dbMedicalRecord);

    }

    public void updateMedicalRecordData(Long id, MedicalRecordDto medicalRecordDto) {
        Optional<MedicalRecord> medicalRecordTemp = repo.findById(id);
        if (medicalRecordTemp.isPresent()) {
            MedicalRecord medicalRecord = medicalRecordTemp.get();

            medicalRecord.setDiagnose(medicalRecordDto.getDiagnose())
                    .setTreatment(medicalRecordDto.getTreatment())
                    .setUpdatedAt(LocalDateTime.now())
                    .setUpdatedBy(medicalRecordDto.getUpdatedBy())
                    .setPatient(patientRepository.findById(medicalRecordDto.getPatientId()).get())
                    .setDoctor(doctorRepository.findById(medicalRecordDto.getDoctorId()).get());
            repo.save(medicalRecord);

        } else {
            addMedicalRecord(medicalRecordDto);
        }
    }

    public boolean deleteMedicalRecord(Long id) {
        if (repo.findById(id).isPresent()) {
            repo.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
