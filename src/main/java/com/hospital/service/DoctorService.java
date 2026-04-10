package com.hospital.service;

import com.hospital.dto.DoctorDto;
import com.hospital.dto.MedicalRecordDto;
import com.hospital.dto.PatientDto;
import com.hospital.entity.Doctor;
import com.hospital.entity.MedicalRecord;
import com.hospital.entity.Patient;
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
public class DoctorService {
   private final DoctorRepository doctorRepository;
   private final AppointmentService appointmentService;
   private final PatientRepository patientRepository;
   private final MedicalRecordRepository medicalRecordRepository;


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
        Optional<Doctor> doctorDb = doctorRepository.findById(id);
        if (doctorDb.isEmpty()) {
            throw new HospitalBusinessException("no doctor found");
        } else {
            Doctor doc = doctorDb.get();
            DoctorDto doctorDto = new DoctorDto();
            doctorDto.setId(doc.getId());
            doctorDto.setName(doc.getName());
            doctorDto.setSpeciality(doc.getSpecialty());
            doctorDto.setContactNumber(doc.getContactNumber());
            doctorDto.setCreatedBy(doc.getCreatedBy());
            doctorDto.setCreatedAt(doc.getCreatedAt());
            doctorDto.setUpdatedBy(doc.getUpdatedBy());
            doctorDto.setUpdatedAt(doc.getUpdatedAt());
            return doctorDto;

        }

    }

    public DoctorDto addDoctor(DoctorDto doctorDto) {
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
        return doctorDto;
    }

    public DoctorDto updateDoctorData(Long id, DoctorDto doctorDto) {
        Optional<Doctor> doctor = doctorRepository.findById(id);
        if (doctor.isPresent()) {
            Doctor doc = doctor.get();
            doc.setName(doctorDto.getName());
            doc.setSpecialty(doctorDto.getSpeciality());
            doc.setContactNumber(doctorDto.getContactNumber());
            doc.setUpdatedAt(LocalDateTime.now());
            doc.setUpdatedBy(doctorDto.getUpdatedBy());
            doctorRepository.save(doc);
        } else {
            throw new HospitalBusinessException("no doctor found");
        }
return doctorDto;
    }


    public void deleteDoctorById(Long id) {
        Optional<Doctor> doctorDb = doctorRepository.findById(id);
        if (doctorDb.isEmpty())
            throw new HospitalBusinessException("no doctor found");
        else
            doctorRepository.deleteById(id);


    }
// still need for (debug and test)
    public PatientDto startSession(MedicalRecordDto medicalRecordDto) {
       Optional<Patient> patientDb = patientRepository.findById(medicalRecordDto.getPatientId());
       Optional<Doctor> doctorDb  = doctorRepository.findById(medicalRecordDto.getDoctorId());
       if (patientDb.isEmpty()) {
            throw new HospitalBusinessException("no patient found");
        }
        if (doctorDb.isEmpty()) {
            throw new HospitalBusinessException("no doctor found");
        }

        PatientDto patientDto = appointmentService.next();
        MedicalRecord medicalRecordDb = new MedicalRecord();
        medicalRecordDb.setDiagnose(medicalRecordDto.getDiagnose())
                .setTreatment(medicalRecordDto.getTreatment())
                .setCreatedAt(LocalDateTime.now())
                .setCreatedBy(medicalRecordDto.getCreatedBy())
                .setUpdatedAt(LocalDateTime.now())
                .setUpdatedBy(medicalRecordDto.getUpdatedBy())
                .setPatient(patientDb.get())
                .setDoctor(doctorDb.get());
        medicalRecordRepository.save(medicalRecordDb);
        return patientDto;
    }
}
