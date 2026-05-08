package com.hospital.patient.service;

import com.hospital.common.security.JWTService;
import com.hospital.medicalRecord.dto.request.CreateMedicalRecordRequest;
import com.hospital.medicalRecord.dto.response.GetMedicalRecordResponse;
import com.hospital.patient.dto.request.CreatePatientRequest;
import com.hospital.entity.MedicalRecord;
import com.hospital.entity.Patient;
import com.hospital.common.exception.HospitalBusinessException;
import com.hospital.patient.dto.request.UpdatePatientRequest;
import com.hospital.patient.dto.response.CreatePatientResponse;
import com.hospital.patient.dto.response.GetPatientResponse;
import com.hospital.patient.dto.response.UpdatePatientResponse;
import com.hospital.medicalRecord.repository.MedicalRecordRepository;
import com.hospital.patient.repository.PatientRepository;
import com.hospital.medicalRecord.service.MedicalRecordService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
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
    private final JWTService jwtService;

    public List<GetPatientResponse> getAllPatients() {
        List<Patient> patients = patientRepository.findAll();
        List<GetPatientResponse> patientsResponse = new ArrayList<>();
        if (!patients.isEmpty()) {
            for (Patient patient : patients) {
                GetPatientResponse getPatientResponse = new GetPatientResponse();
                getPatientResponse.setId(patient.getId())
                        .setDateOfBirth(patient.getDateOfBirth())
                        .setName(patient.getName())
                        .setPhone(patient.getPhone()).
                        setMedicalRecords(medicalRecordService.getByPatientId(patient.getId()))
                        .setGender(patient.getGender())
                        .setCreatedAt(patient.getCreatedAt())
                        .setUpdatedAt(patient.getUpdatedAt())
                        .setCreatedBy(patient.getCreatedBy())
                        .setUpdatedBy(patient.getUpdatedBy());
                patientsResponse.add(getPatientResponse);
            }
        }
        return patientsResponse;
    }

    public GetPatientResponse getPatientById(Long id) {
        Optional<Patient> patient = patientRepository.findById(id);
        if (patient.isEmpty()) {
            throw new HospitalBusinessException("no patient found");
        }
        GetPatientResponse patientResponse = new GetPatientResponse();
        patientResponse.setId(patient.get().getId())
                .setDateOfBirth(patient.get().getDateOfBirth())
                .setName(patient.get().getName())
                .setGender(patient.get().getGender())
                .setPhone(patient.get().getPhone())
                .setMedicalRecords(medicalRecordService.getByPatientId(id))
                .setCreatedBy(patient.get().getCreatedBy())
                .setCreatedAt(patient.get().getCreatedAt())
                .setUpdatedBy(patient.get().getUpdatedBy())
                .setUpdatedAt(patient.get().getUpdatedAt());
        return patientResponse;
    }


    public CreatePatientResponse addPatient(CreatePatientRequest createPatientRequest) {

        if (patientRepository.findByPhone(createPatientRequest.getPhone()).isPresent()) {
            throw new HospitalBusinessException("this phone number is already exist");
        }
        Patient patient = new Patient();
        patient
                .setName(createPatientRequest.getName())
                .setGender(createPatientRequest.getGender())
                .setPhone(createPatientRequest.getPhone())
                .setDateOfBirth(createPatientRequest.getDateOfBirth())
                .setCreatedAt(LocalDateTime.now())
                .setUpdatedAt(LocalDateTime.now())
                .setId(createPatientRequest.getId());
        patientRepository.save(patient);
        CreatePatientResponse patientResponse = new CreatePatientResponse();
        patientResponse.setId(patient.getId());
        return patientResponse;

    }


    public UpdatePatientResponse updatePatientData(UpdatePatientRequest updatePatientRequest) {

        Optional<Patient> patientTemp = patientRepository.findById(updatePatientRequest.getId());
        if (patientTemp.isPresent()) {
            Patient dbPatient = patientTemp.get();
            dbPatient.setName(updatePatientRequest.getName())
                    .setGender(updatePatientRequest.getGender())
                    .setPhone(updatePatientRequest.getPhone())
                    .setDateOfBirth(updatePatientRequest.getDateOfBirth())
                    .setUpdatedAt(LocalDateTime.now());
            patientRepository.save(dbPatient);
            UpdatePatientResponse patientResponse = new UpdatePatientResponse();
            patientResponse.setId(dbPatient.getId());
            return patientResponse;
        } else
            throw new HospitalBusinessException("no patient found");

    }

    public void deletePatientById(Long id) {
        if (patientRepository.findById(id).isEmpty())
            throw new HospitalBusinessException("no patient found");
        else
            patientRepository.deleteById(id);
    }

    public List<GetMedicalRecordResponse> showPatientHistory(Long id) {

        List<MedicalRecord> medicalRecords = medicalRecordRepository.findMedicalRecordsByPatientId(id);
        if (medicalRecords.isEmpty()){
            throw new HospitalBusinessException("no medical_records found");
        }
        List<GetMedicalRecordResponse> getMedicalRecordResponses = new ArrayList<>();
        medicalRecords.forEach(medicalRecord -> {
                GetMedicalRecordResponse getMedicalRecordResponse = new GetMedicalRecordResponse();
                getMedicalRecordResponse.setId(medicalRecord.getId())
                        .setDiagnose(medicalRecord.getDiagnose())
                        .setTreatment(medicalRecord.getTreatment())
                        .setPatientId(medicalRecord.getPatient().getId())
                        .setDoctorId(medicalRecord.getDoctor().getId())
                        .setCreatedAt(medicalRecord.getCreatedAt())
                        .setCreatedBy(medicalRecord.getCreatedBy())
                        .setUpdatedAt(medicalRecord.getUpdatedAt())
                        .setUpdatedBy(medicalRecord.getUpdatedBy());
                getMedicalRecordResponses.add(getMedicalRecordResponse);
            });
        return getMedicalRecordResponses;
    }
}
