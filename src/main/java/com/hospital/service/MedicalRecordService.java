package com.hospital.service;

import com.hospital.dto.MedicalRecordDto;
import com.hospital.entity.Appointment;
import com.hospital.entity.MedicalRecord;
import com.hospital.exception.HospitalBusinessException;
import com.hospital.repository.DoctorRepository;
import com.hospital.repository.MedicalRecordRepository;
import com.hospital.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MedicalRecordService {

    private final MedicalRecordRepository medicalRecordRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    public List<MedicalRecordDto> getAllRecords() {
        List<MedicalRecord> medicalRecords = medicalRecordRepository.findAll();
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
        Optional<MedicalRecord> medicalRecord = medicalRecordRepository.findById(id);
        MedicalRecordDto medicalRecordDto = new MedicalRecordDto();
        if (medicalRecord.isPresent()) {
            MedicalRecord medicalRecordDb = medicalRecord.get();
            medicalRecordDto.setId(medicalRecordDb.getId())
                    .setDiagnose(medicalRecordDb.getDiagnose())
                    .setTreatment(medicalRecordDb.getTreatment())
                    .setPatientId(medicalRecordDb.getPatient().getId())
                    .setDoctorId(medicalRecordDb.getDoctor().getId())
                    .setCreatedAt(medicalRecordDb.getCreatedAt())
                    .setCreatedBy(medicalRecordDb.getCreatedBy())
                    .setUpdatedAt(medicalRecordDb.getUpdatedAt())
                    .setUpdatedBy(medicalRecordDb.getUpdatedBy());
            return medicalRecordDto;
        }else {
            return null;
        }

    }

    public void addMedicalRecord(MedicalRecordDto medicalRecordDto) {
        if (patientRepository.findById(medicalRecordDto.getPatientId()).isEmpty()) {
            throw new HospitalBusinessException("no patient found");
        }
        if (doctorRepository.findById(medicalRecordDto.getDoctorId()).isEmpty()) {
            throw new HospitalBusinessException("no doctor found");
        }

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

        medicalRecordRepository.save(dbMedicalRecord);

    }

    public void updateMedicalRecordData(Long id, MedicalRecordDto medicalRecordDto) {
        if (patientRepository.findById(medicalRecordDto.getPatientId()).isEmpty()) {
            throw new HospitalBusinessException("no patient found");
        }
        if (doctorRepository.findById(medicalRecordDto.getDoctorId()).isEmpty()) {
            throw new HospitalBusinessException("no doctor found");
        }

        Optional<MedicalRecord> medicalRecordTemp = medicalRecordRepository.findById(id);
        if (medicalRecordTemp.isPresent()) {
            MedicalRecord medicalRecord = medicalRecordTemp.get();

            medicalRecord.setDiagnose(medicalRecordDto.getDiagnose())
                    .setTreatment(medicalRecordDto.getTreatment())
                    .setUpdatedAt(LocalDateTime.now())
                    .setUpdatedBy(medicalRecordDto.getUpdatedBy())
                    .setPatient(patientRepository.findById(medicalRecordDto.getPatientId()).get())
                    .setDoctor(doctorRepository.findById(medicalRecordDto.getDoctorId()).get());
            medicalRecordRepository.save(medicalRecord);

        } else {
            throw new HospitalBusinessException("no medical_record found");
        }
    }

    public void deleteMedicalRecord(Long id) {
        Optional<MedicalRecord> medicalRecord = medicalRecordRepository.findById(id);
        if (medicalRecord.isEmpty())
            throw new HospitalBusinessException("medicalRecord not found");
        else
            medicalRecordRepository.deleteById(id);
    }

    public List<MedicalRecordDto> getByPatientId(Long id) {
        List<MedicalRecordDto> medicalRecordDtos = new ArrayList<>();
        Optional<List<MedicalRecord>> medicalRecords = medicalRecordRepository.findMedicalRecordsByPatientId(id);
        if (medicalRecords.isPresent()) {
            List<MedicalRecord> medicalRecordsDb = medicalRecords.get();
            medicalRecordsDb.forEach(medicalRecord -> {
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
        }else {
            return new ArrayList<>();
        }
    }

}
