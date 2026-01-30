package com.HospitalManagementSystem.HospitalSystem.service;

import com.HospitalManagementSystem.HospitalSystem.dto.DoctorDto;
import com.HospitalManagementSystem.HospitalSystem.entity.Doctor;
import com.HospitalManagementSystem.HospitalSystem.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class DoctorService {
    @Autowired
    DoctorRepository repo;


    public List<DoctorDto> getAllDoctors() {
        List<Doctor> doctors = repo.findAll();
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
        Doctor doc = repo.findById(id).orElse(null);
        DoctorDto doctorDto = new DoctorDto();
        if (doc != null) {
            doctorDto.setId(doc.getId());
            doctorDto.setName(doc.getName());
            doctorDto.setSpeciality(doc.getSpecialty());
            doctorDto.setContactNumber(doc.getContactNumber());
            doctorDto.setCreatedBy(doc.getCreatedBy());
            doctorDto.setCreatedAt(doc.getCreatedAt());
            doctorDto.setUpdatedBy(doc.getUpdatedBy());
            doctorDto.setUpdatedAt(doc.getUpdatedAt());
            return doctorDto;
        }else {
            return null;
        }

    }

    public void addDoctor(DoctorDto doctorDto) {
        Doctor doctor = new Doctor();
        doctor.setId(doctorDto.getId());
        doctor.setName(doctorDto.getName());
        doctor.setSpecialty(doctorDto.getSpeciality());
        doctor.setContactNumber(doctorDto.getContactNumber());
        doctor.setCreatedBy(doctorDto.getCreatedBy());
        doctor.setCreatedAt(LocalDateTime.now());
        doctor.setUpdatedBy(doctorDto.getUpdatedBy());
        doctor.setUpdatedAt(LocalDateTime.now());

        repo.save(doctor);
    }

    public void updateDoctorData(Long id, DoctorDto doctorDto) {
        Optional<Doctor> doctor = repo.findById(id);
        if (doctor.isPresent()) {
            Doctor doc = doctor.get();
            doc.setName(doctorDto.getName());
            doc.setSpecialty(doctorDto.getSpeciality());
            doc.setContactNumber(doctorDto.getContactNumber());
            doc.setUpdatedAt(LocalDateTime.now());
            doc.setUpdatedBy(doctorDto.getUpdatedBy());
            repo.save(doc);
        }

    }


    public boolean deleteDoctorById(Long id) {
        if (repo.findById(id).isPresent()) {
            repo.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
