package com.hospital.medical_record.service;

import com.hospital.appointment.specification.AppointmentSpecification;
import com.hospital.common.exception.ErrorCode;
import com.hospital.common.exception.HospitalBusinessException;
import com.hospital.common.security.JwtService;
import com.hospital.diagnose.repository.DiagnoseRepository;
import com.hospital.doctor.repository.DoctorRepository;
import com.hospital.dto.PageResponse;
import com.hospital.entity.Appointment;
import com.hospital.entity.MedicalRecord;
import com.hospital.medical_record.dto.request.CreateMedicalRecordRequest;
import com.hospital.medical_record.dto.request.SearchMedicalRecordRequest;
import com.hospital.medical_record.dto.request.UpdateMedicalRecordRequest;
import com.hospital.medical_record.dto.response.CreateMedicalRecordResponse;
import com.hospital.medical_record.dto.response.GetMedicalRecordResponse;
import com.hospital.medical_record.dto.response.SearchMedicalRecordResponse;
import com.hospital.medical_record.dto.response.UpdateMedicalRecordResponse;
import com.hospital.medical_record.repository.MedicalRecordRepository;
import com.hospital.medical_record.specification.MedicalRecordSpecification;
import com.hospital.patient.repository.PatientRepository;
import com.hospital.treatment.repository.TreatmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
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
    private final DiagnoseRepository diagnoseRepository;
    private final TreatmentRepository treatmentRepository;
    private final JwtService jwtService;

    public PageResponse<GetMedicalRecordResponse> searchMedicalRecord(SearchMedicalRecordRequest searchMedicalRecordRequest,
                                                                       int page, int size,String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page,size,sort);

        Specification<MedicalRecord> specification  = Specification.where(null);
        specification = specification
                .and(MedicalRecordSpecification.hasDoctorId(searchMedicalRecordRequest.getDoctorId()))
                .and(MedicalRecordSpecification.hasPatientId(searchMedicalRecordRequest.getPatientId()))
                .and(MedicalRecordSpecification.hasDiagnoseId(searchMedicalRecordRequest.getDiagnoseId()))
                .and(MedicalRecordSpecification.hasTreatmentId(searchMedicalRecordRequest.getTreatmentId()));

        Page<MedicalRecord> medicalRecordPage = medicalRecordRepository.findAll(specification,pageable);
       List<MedicalRecord> medicalRecords = medicalRecordPage.getContent();

        List<GetMedicalRecordResponse> medicalRecordsResponse = new ArrayList<>();
        if (!medicalRecords.isEmpty()){
            for (MedicalRecord record : medicalRecords) {
                GetMedicalRecordResponse medicalRecordResponse = new GetMedicalRecordResponse();
                medicalRecordResponse.setId(record.getId())
                        .setDiagnoseId(record.getDiagnose().getId())
                        .setTreatmentId(record.getTreatment().getId())
                        .setPatientId(record.getPatient().getId())
                        .setDoctorId(record.getDoctor().getId())
                        .setCreatedAt(record.getCreatedAt())
                        .setCreatedBy(record.getCreatedBy())
                        .setUpdatedAt(record.getUpdatedAt())
                        .setUpdatedBy(record.getUpdatedBy());
                medicalRecordsResponse.add(medicalRecordResponse);
            }
        }

        return PageResponse.<GetMedicalRecordResponse>builder()
                .data(medicalRecordsResponse)
                .page(medicalRecordPage.getNumber())
                .size(medicalRecordPage.getSize())
                .totalElements(medicalRecordPage.getTotalElements())
                .totalPages(medicalRecordPage.getTotalPages())
                .first(medicalRecordPage.isFirst())
                .last(medicalRecordPage.isLast())
                .build();

    }

    public GetMedicalRecordResponse getMedicalRecordById(Long id) {
        Optional<MedicalRecord> medicalRecord = medicalRecordRepository.findById(id);
        if (medicalRecord.isEmpty()){
            throw new HospitalBusinessException(HttpStatus.NOT_FOUND, ErrorCode.MEDICAL_RECORD_NOT_FOUND.name()
                    , "medical_record with id (" +id+ ") not found");
        }
        GetMedicalRecordResponse medicalRecordResponse = new GetMedicalRecordResponse();

            MedicalRecord medicalRecordDb = medicalRecord.get();
            medicalRecordResponse.setId(medicalRecordDb.getId())
                    .setDiagnoseId(medicalRecordDb.getDiagnose().getId())
                    .setTreatmentId(medicalRecordDb.getTreatment().getId())
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
            throw new HospitalBusinessException(HttpStatus.NOT_FOUND,ErrorCode.PATIENT_NOT_FOUND.name()
                    ,"patient with id ("+medicalRecordRequest.getPatientId()+") not found");
        }
        if (doctorRepository.findById(medicalRecordRequest.getDoctorId()).isEmpty()) {
            throw new HospitalBusinessException(HttpStatus.NOT_FOUND,ErrorCode.DOCTOR_NOT_FOUND.name()
                    ,"doctor with id ("+medicalRecordRequest.getDoctorId()+") not found");
        }
        if (diagnoseRepository.findById(medicalRecordRequest.getDiagnoseId()).isEmpty()) {
            throw new HospitalBusinessException(HttpStatus.NOT_FOUND, ErrorCode.DIAGNOSE_NOT_FOUND.name()
                    , "diagnose with id (" +medicalRecordRequest.getDiagnoseId()+ ") not found");
        }
        if (treatmentRepository.findById(medicalRecordRequest.getTreatmentId()).isEmpty()) {
            throw new HospitalBusinessException(HttpStatus.NOT_FOUND, ErrorCode.TREATMENT_NOT_FOUND.name()
                    , "treatment with id (" +medicalRecordRequest.getTreatmentId()+ ") not found");
        }

        MedicalRecord dbMedicalRecord = new MedicalRecord();
        dbMedicalRecord
                .setPatient(patientRepository.findById(medicalRecordRequest.getPatientId()).get())
                .setDoctor(doctorRepository.findById(medicalRecordRequest.getDoctorId()).get())
                .setDiagnose(diagnoseRepository.findById(medicalRecordRequest.getDiagnoseId()).get())
                .setTreatment(treatmentRepository.findById(medicalRecordRequest.getTreatmentId()).get());

        medicalRecordRepository.save(dbMedicalRecord);
        CreateMedicalRecordResponse medicalRecordResponse = new CreateMedicalRecordResponse();
        medicalRecordResponse.setId(dbMedicalRecord.getId());
        return medicalRecordResponse;
    }

    public UpdateMedicalRecordResponse updateMedicalRecordData(UpdateMedicalRecordRequest medicalRecordRequest) {

        if (patientRepository.findById(medicalRecordRequest.getPatientId()).isEmpty()) {
            throw new HospitalBusinessException(HttpStatus.NOT_FOUND,ErrorCode.PATIENT_NOT_FOUND.name()
                    ,"patient with id ("+medicalRecordRequest.getPatientId()+") not found");
        }
        if (doctorRepository.findById(medicalRecordRequest.getDoctorId()).isEmpty()) {
            throw new HospitalBusinessException(HttpStatus.NOT_FOUND,ErrorCode.DOCTOR_NOT_FOUND.name()
                    ,"doctor with id ("+medicalRecordRequest.getDoctorId()+") not found");
        }
        if (diagnoseRepository.findById(medicalRecordRequest.getDiagnoseId()).isEmpty()) {
            throw new HospitalBusinessException(HttpStatus.NOT_FOUND, ErrorCode.DIAGNOSE_NOT_FOUND.name()
                    , "diagnose with id (" +medicalRecordRequest.getDiagnoseId()+ ") not found");
        }
        if (treatmentRepository.findById(medicalRecordRequest.getTreatmentId()).isEmpty()) {
            throw new HospitalBusinessException(HttpStatus.NOT_FOUND, ErrorCode.TREATMENT_NOT_FOUND.name()
                    , "treatment with id (" +medicalRecordRequest.getTreatmentId()+ ") not found");
        }

        Optional<MedicalRecord> medicalRecordTemp = medicalRecordRepository.findById(medicalRecordRequest.getId());
        if (medicalRecordTemp.isPresent()) {
            MedicalRecord medicalRecord = medicalRecordTemp.get();

            medicalRecord.setDiagnose(diagnoseRepository.findById(medicalRecordRequest.getDiagnoseId()).get())
                    .setTreatment(treatmentRepository.findById(medicalRecordRequest.getTreatmentId()).get())
                    .setPatient(patientRepository.findById(medicalRecordRequest.getPatientId()).get())
                    .setDoctor(doctorRepository.findById(medicalRecordRequest.getDoctorId()).get())
                    .setUpdatedAt(LocalDateTime.now());
                              medicalRecordRepository.save(medicalRecord);
            UpdateMedicalRecordResponse medicalRecordResponse = new UpdateMedicalRecordResponse();
            medicalRecordResponse.setId(medicalRecord.getId());
            return medicalRecordResponse;

        } else {
            throw new HospitalBusinessException(HttpStatus.NOT_FOUND, ErrorCode.MEDICAL_RECORD_NOT_FOUND.name()
                    , "medical_record with id (" +medicalRecordRequest+ ") not found");        }
    }

    public void deleteMedicalRecord(Long id) {
        Optional<MedicalRecord> medicalRecord = medicalRecordRepository.findById(id);
        if (medicalRecord.isEmpty())
            throw new HospitalBusinessException(HttpStatus.NOT_FOUND, ErrorCode.MEDICAL_RECORD_NOT_FOUND.name()
                    , "medical_record with id (" +id+ ") not found");        else
            medicalRecordRepository.deleteById(id);
    }

    public List<GetMedicalRecordResponse> getByPatientId(Long id) {

        List<GetMedicalRecordResponse> medicalRecordsResponse = new ArrayList<>();
        List<MedicalRecord> medicalRecords = medicalRecordRepository.findMedicalRecordsByPatientId(id);
        if (medicalRecords.isEmpty()){
            return new ArrayList<>();
        }
        else {
            medicalRecords.forEach(medicalRecord -> {
                GetMedicalRecordResponse medicalRecordResponse = new GetMedicalRecordResponse();
                medicalRecordResponse.setId(medicalRecord.getId())
                        .setDiagnoseId(medicalRecord.getDiagnose().getId())
                        .setTreatmentId(medicalRecord.getTreatment().getId())
                        .setPatientId(medicalRecord.getPatient().getId())
                        .setDoctorId(medicalRecord.getDoctor().getId())
                        .setCreatedAt(medicalRecord.getCreatedAt())
                        .setUpdatedAt(medicalRecord.getUpdatedAt());
                medicalRecordsResponse.add(medicalRecordResponse);
            });
        }
        return medicalRecordsResponse;

    }
}
