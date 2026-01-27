package com.HospitalManagementSystem.HospitalSystem.service;

import com.HospitalManagementSystem.HospitalSystem.entity.Doctor;
import com.HospitalManagementSystem.HospitalSystem.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class DoctorService { 
    @Autowired
    DoctorRepository repo;


    public List<Doctor> getAllDoctors() {
       return repo.findAll();
    }

    public Doctor getDoctorById(Long id) {
        return repo.findById(id).orElse(null);
    }

    public void addDoctor(Doctor doc) {
        doc.setCreatedAt(new Date());
        doc.setUpdatedAt(new Date());
        repo.save(doc);
    }

    public void updateDoctorData(Long id, Doctor doc) {
       if(repo.findById(id).isPresent()){
          Doctor D = repo.findById(id).get();
          D.setName(doc.getName());
           D.setSpecialty(doc.getSpecialty());
           D.setContactNumber(doc.getContactNumber());
           D.setUpdatedAt(new Date());
           D.setUpdatedBy(doc.getUpdatedBy());
           D.setCreatedAt(repo.findById(id).get().getCreatedAt());
           D.setCreatedBy(doc.getCreatedBy());
           repo.save(D);
       }else{
           addDoctor(doc);
       }
    }


    public boolean deleteDoctorById(Long id) {
       if (repo.findById(id).isPresent()){
           repo.deleteById(id);
           return true;
       }else
       {
           return false;
       }
    }
}
