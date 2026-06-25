package com.hospital.doctor.service;

import com.hospital.billing.dto.response.GetBillingResponse;
import com.hospital.common.security.JWTService;
import com.hospital.diagnose.repository.DiagnoseRepository;
import com.hospital.doctor.dto.request.CreateDoctorRequest;
import com.hospital.doctor.dto.request.SearchDoctorRequest;
import com.hospital.doctor.dto.request.UpdateDoctorRequest;
import com.hospital.doctor.dto.response.CreateDoctorResponse;
import com.hospital.doctor.dto.response.GetDoctorResponse;
import com.hospital.doctor.dto.response.UpdateDoctorResponse;
import com.hospital.dto.PageResponse;
import com.hospital.entity.*;
import com.hospital.medicalRecord.dto.request.CreateMedicalRecordRequest;
import com.hospital.common.exception.HospitalBusinessException;
import com.hospital.doctor.repository.DoctorRepository;
import com.hospital.medicalRecord.repository.MedicalRecordRepository;
import com.hospital.patient.dto.response.GetPatientResponse;
import com.hospital.patient.repository.PatientRepository;
import com.hospital.appointment.service.AppointmentService;
import com.hospital.speciality.repository.SpecialityRepository;
import com.hospital.treatment.repository.TreatmentRepository;
import jakarta.servlet.http.HttpServletRequest;
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
public class DoctorService {
    private final DoctorRepository doctorRepository;
    private final AppointmentService appointmentService;
    private final PatientRepository patientRepository;
    private final MedicalRecordRepository medicalRecordRepository;
    private final SpecialityRepository specialityRepository;
    private final DiagnoseRepository diagnoseRepository;
    private final TreatmentRepository treatmentRepository;
    private final JWTService jwtService;

    public PageResponse<GetDoctorResponse> getAllDoctors(int page,int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page,size,sort);
        Page<Doctor> doctorPage = doctorRepository.findAll(pageable);
       List<Doctor> doctors = doctorPage.getContent();
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
        return PageResponse.<GetDoctorResponse>builder()
                .data(getDoctorResponses)
                .page(doctorPage.getNumber())
                .size(doctorPage.getSize())
                .totalElements(doctorPage.getTotalElements())
                .totalPages(doctorPage.getTotalPages())
                .first(doctorPage.isFirst())
                .last(doctorPage.isLast())
                .build();

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
        Optional<Diagnose> diagnoseDb = diagnoseRepository.findById(createMedicalRecordRequest.getDiagnoseId());
        Optional<Treatment> treatmentDb = treatmentRepository.findById(createMedicalRecordRequest.getTreatmentId());
        if (patientDb.isEmpty())
            throw new HospitalBusinessException("no patient found");
        if (doctorDb.isEmpty())
            throw new HospitalBusinessException("no doctor found");
        if (diagnoseDb.isEmpty())
            throw new HospitalBusinessException("no diagnose found");
        if (treatmentDb.isEmpty())
            throw new HospitalBusinessException("no treatment found");


        GetPatientResponse patientResponse = appointmentService.next();
        MedicalRecord medicalRecordDb = new MedicalRecord();
        medicalRecordDb.setDiagnose(diagnoseDb.get())
                .setTreatment(treatmentDb.get())
                .setPatient(patientDb.get())
                .setDoctor(doctorDb.get())
                .setCreatedAt(LocalDateTime.now())
                .setUpdatedAt(LocalDateTime.now());
        medicalRecordRepository.save(medicalRecordDb);
        return patientResponse;
    }

    public GetDoctorResponse searchDoctor(SearchDoctorRequest searchDoctorRequest) {
        Optional<Speciality> specialityOp = specialityRepository.findByName(searchDoctorRequest.getSpeciality());
        if (specialityOp.isEmpty()) {
            throw new HospitalBusinessException("no speciality found");
        }
        Speciality speciality = specialityOp.get();

       Optional<Doctor> doctorOp = doctorRepository.findByNameAndContactNumber(searchDoctorRequest.getName(),
                                                                    searchDoctorRequest.getContactNumber());
        if (doctorOp.isEmpty()){
            throw new HospitalBusinessException("no doctor found");
        }
        Doctor doctor = doctorOp.get();

       GetDoctorResponse getDoctorResponse = new GetDoctorResponse();
        getDoctorResponse.setId(doctor.getId())
                .setName(doctor.getName())
                .setSpeciality(speciality)
                .setContactNumber(doctor.getContactNumber())
                .setCreatedBy(doctor.getCreatedBy())
                .setCreatedAt(doctor.getCreatedAt())
                .setUpdatedBy(doctor.getUpdatedBy())
                .setUpdatedAt(doctor.getUpdatedAt());

        return getDoctorResponse;
    }
}
