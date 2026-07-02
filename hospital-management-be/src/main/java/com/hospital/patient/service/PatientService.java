package com.hospital.patient.service;

import com.hospital.common.exception.HospitalBusinessException;
import com.hospital.common.security.JWTService;
import com.hospital.dto.PageResponse;
import com.hospital.entity.MedicalRecord;
import com.hospital.entity.Patient;
import com.hospital.medical_record.dto.response.GetMedicalRecordResponse;
import com.hospital.medical_record.repository.MedicalRecordRepository;
import com.hospital.medical_record.service.MedicalRecordService;
import com.hospital.patient.dto.request.CreatePatientRequest;
import com.hospital.patient.dto.request.SearchPatientRequest;
import com.hospital.patient.dto.request.UpdatePatientRequest;
import com.hospital.patient.dto.response.CreatePatientResponse;
import com.hospital.patient.dto.response.GetPatientResponse;
import com.hospital.patient.dto.response.UpdatePatientResponse;
import com.hospital.patient.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class PatientService {
    private final PatientRepository patientRepository;
    private final MedicalRecordRepository medicalRecordRepository;
    private final MedicalRecordService medicalRecordService;
    private final JWTService jwtService;

    public PageResponse<GetPatientResponse> getAllPatients(int page, int size, String sortBy, String direction){
        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page,size,sort);
        Page<Patient> patientPage = patientRepository.findAll(pageable);
        List<Patient> patients = patientPage.getContent();

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
        return PageResponse.<GetPatientResponse>builder()
                .data(patientsResponse)
                .page(patientPage.getNumber())
                .size(patientPage.getSize())
                .totalElements(patientPage.getTotalElements())
                .totalPages(patientPage.getTotalPages())
                .first(patientPage.isFirst())
                .last(patientPage.isLast())
                .build();
    }

    public GetPatientResponse getPatientById(Long id) {
        Optional<Patient> patientOp = patientRepository.findById(id);
        if (patientOp.isEmpty()) {
            throw new HospitalBusinessException("no patient found");
        }
        Patient patient = patientOp.get();
        GetPatientResponse patientResponse = new GetPatientResponse();
        patientResponse.setId(patient.getId())
                .setDateOfBirth(patient.getDateOfBirth())
                .setName(patient.getName())
                .setGender(patient.getGender())
                .setPhone(patient.getPhone())
                .setMedicalRecords(medicalRecordService.getByPatientId(id))
                .setCreatedBy(patient.getCreatedBy())
                .setCreatedAt(patient.getCreatedAt())
                .setUpdatedBy(patient.getUpdatedBy())
                .setUpdatedAt(patient.getUpdatedAt());
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
                    .setDiagnoseId(medicalRecord.getDiagnose().getId())
                    .setTreatmentId(medicalRecord.getTreatment().getId())
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

    public GetPatientResponse searchPatient(SearchPatientRequest searchPatientRequest) {
        Optional<Patient> patientOp = patientRepository.findByNameAndDateOfBirthAndPhone(
                searchPatientRequest.getName()
                , searchPatientRequest.getDateOfBirth()
                , searchPatientRequest.getPhone());
        if (patientOp.isEmpty()) {
            throw new HospitalBusinessException("no patient found");
        }
        Patient patient = patientOp.get();
        GetPatientResponse getPatientResponse = new GetPatientResponse();
        getPatientResponse.setId(patient.getId())
                .setName(patient.getName())
                .setGender(patient.getGender())
                .setPhone(patient.getPhone())
                .setMedicalRecords(medicalRecordService.getByPatientId(patient.getId()))
                .setDateOfBirth(patient.getDateOfBirth())
                .setCreatedBy(patient.getCreatedBy())
                .setCreatedAt(patient.getCreatedAt())
                .setUpdatedBy(patient.getUpdatedBy())
                .setUpdatedAt(patient.getUpdatedAt());

        return getPatientResponse;
    }
}
