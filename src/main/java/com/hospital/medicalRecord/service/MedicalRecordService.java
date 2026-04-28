package com.hospital.medicalRecord.service;

import com.hospital.medicalRecord.dto.request.CreateMedicalRecordRequest;
import com.hospital.entity.MedicalRecord;
import com.hospital.common.exception.HospitalBusinessException;
import com.hospital.doctor.repository.DoctorRepository;
import com.hospital.medicalRecord.dto.request.UpdateMedicalRecordRequest;
import com.hospital.medicalRecord.dto.response.CreateMedicalRecordResponse;
import com.hospital.medicalRecord.dto.response.GetMedicalRecordResponse;
import com.hospital.medicalRecord.dto.response.UpdateMedicalRecordResponse;
import com.hospital.medicalRecord.repository.MedicalRecordRepository;
import com.hospital.patient.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
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

    public List<GetMedicalRecordResponse> getAllRecords() {
        List<MedicalRecord> medicalRecords = medicalRecordRepository.findAll();
        if (medicalRecords.isEmpty()){
            throw new HospitalBusinessException("no medical_records found");
        }
        List<GetMedicalRecordResponse> medicalRecordsResponse = new ArrayList<>();

            for (MedicalRecord record : medicalRecords) {
                GetMedicalRecordResponse medicalRecordResponse = new GetMedicalRecordResponse();
                medicalRecordResponse.setId(record.getId())
                        .setDiagnose(record.getDiagnose())
                        .setTreatment(record.getTreatment())
                        .setPatientId(record.getPatient().getId())
                        .setDoctorId(record.getDoctor().getId())
                        .setCreatedAt(record.getCreatedAt())
                        .setCreatedBy(record.getCreatedBy())
                        .setUpdatedAt(record.getUpdatedAt())
                        .setUpdatedBy(record.getUpdatedBy());
                medicalRecordsResponse.add(medicalRecordResponse);
        }

        return medicalRecordsResponse;

    }

    public GetMedicalRecordResponse getMedicalRecordById(Long id) {
        Optional<MedicalRecord> medicalRecord = medicalRecordRepository.findById(id);
        if (medicalRecord.isEmpty()){
            throw new HospitalBusinessException("no medical_record found");
        }
        GetMedicalRecordResponse medicalRecordResponse = new GetMedicalRecordResponse();

            MedicalRecord medicalRecordDb = medicalRecord.get();
            medicalRecordResponse.setId(medicalRecordDb.getId())
                    .setDiagnose(medicalRecordDb.getDiagnose())
                    .setTreatment(medicalRecordDb.getTreatment())
                    .setPatientId(medicalRecordDb.getPatient().getId())
                    .setDoctorId(medicalRecordDb.getDoctor().getId())
                    .setCreatedAt(medicalRecordDb.getCreatedAt())
                    .setCreatedBy(medicalRecordDb.getCreatedBy())
                    .setUpdatedAt(medicalRecordDb.getUpdatedAt())
                    .setUpdatedBy(medicalRecordDb.getUpdatedBy());
            return medicalRecordResponse;
    }

    public CreateMedicalRecordResponse addMedicalRecord(CreateMedicalRecordRequest medicalRecordRequest) {
        if (patientRepository.findById(medicalRecordRequest.getPatientId()).isEmpty()) {
            throw new HospitalBusinessException("no patient found");
        }
        if (doctorRepository.findById(medicalRecordRequest.getDoctorId()).isEmpty()) {
            throw new HospitalBusinessException("no doctor found");
        }

        MedicalRecord dbMedicalRecord = new MedicalRecord();
        dbMedicalRecord.setId(medicalRecordRequest.getId())
                .setDiagnose(medicalRecordRequest.getDiagnose())
                .setTreatment(medicalRecordRequest.getTreatment())
                .setCreatedAt(LocalDateTime.now())
                .setCreatedBy(medicalRecordRequest.getCreatedBy())
                .setUpdatedAt(LocalDateTime.now())
                .setUpdatedBy(medicalRecordRequest.getUpdatedBy())
                .setPatient(patientRepository.findById(medicalRecordRequest.getPatientId()).get())
                .setDoctor(doctorRepository.findById(medicalRecordRequest.getDoctorId()).get());

        medicalRecordRepository.save(dbMedicalRecord);
        CreateMedicalRecordResponse medicalRecordResponse = new CreateMedicalRecordResponse();
        medicalRecordResponse.setId(dbMedicalRecord.getId());
        return medicalRecordResponse;
    }

    public UpdateMedicalRecordResponse updateMedicalRecordData(Long id, UpdateMedicalRecordRequest medicalRecordRequest) {
        if (patientRepository.findById(medicalRecordRequest.getPatientId()).isEmpty()) {
            throw new HospitalBusinessException("no patient found");
        }
        if (doctorRepository.findById(medicalRecordRequest.getDoctorId()).isEmpty()) {
            throw new HospitalBusinessException("no doctor found");
        }

        Optional<MedicalRecord> medicalRecordTemp = medicalRecordRepository.findById(id);
        if (medicalRecordTemp.isPresent()) {
            MedicalRecord medicalRecord = medicalRecordTemp.get();

            medicalRecord.setDiagnose(medicalRecordRequest.getDiagnose())
                    .setTreatment(medicalRecordRequest.getTreatment())
                    .setUpdatedAt(LocalDateTime.now())
                    .setUpdatedBy(medicalRecordRequest.getUpdatedBy())
                    .setPatient(patientRepository.findById(medicalRecordRequest.getPatientId()).get())
                    .setDoctor(doctorRepository.findById(medicalRecordRequest.getDoctorId()).get());
            medicalRecordRepository.save(medicalRecord);
            UpdateMedicalRecordResponse medicalRecordResponse = new UpdateMedicalRecordResponse();
            medicalRecordResponse.setId(medicalRecord.getId());
            return medicalRecordResponse;

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

    public List<GetMedicalRecordResponse> getByPatientId(Long id) {
        List<GetMedicalRecordResponse> medicalRecordsResponse = new ArrayList<>();
        List<MedicalRecord> medicalRecords = medicalRecordRepository.findMedicalRecordsByPatientId(id);
        if (!medicalRecords.isEmpty())
            medicalRecords.forEach(medicalRecord -> {
                GetMedicalRecordResponse medicalRecordResponse = new GetMedicalRecordResponse();
                medicalRecordResponse.setId(medicalRecord.getId())
                        .setDiagnose(medicalRecord.getDiagnose())
                        .setTreatment(medicalRecord.getTreatment())
                        .setPatientId(medicalRecord.getPatient().getId())
                        .setDoctorId(medicalRecord.getDoctor().getId())
                        .setCreatedAt(medicalRecord.getCreatedAt())
                        .setCreatedBy(medicalRecord.getCreatedBy())
                        .setUpdatedAt(medicalRecord.getUpdatedAt())
                        .setUpdatedBy(medicalRecord.getUpdatedBy());
                medicalRecordsResponse.add(medicalRecordResponse);
            });
        return medicalRecordsResponse;

    }

}
