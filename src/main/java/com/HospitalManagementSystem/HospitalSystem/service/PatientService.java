package com.HospitalManagementSystem.HospitalSystem.service;

import com.HospitalManagementSystem.HospitalSystem.entity.Patient;
import com.HospitalManagementSystem.HospitalSystem.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


@Service
public class PatientService {
    @Autowired
    PatientRepository repo;
    public List<Patient> getAllPatients() {
       return repo.findAll();
    }

    public Patient getPatientById(Long id) {
        return repo.findById(id).orElse(null);
    }


    public void addPatient(Patient p) {
        p.setCreatedAt(new Date());
        p.setUpdatedAt(new Date());
        repo.save(p);
    }


    public void updatePatientData( Long id ,Patient p) {
    if (repo.findById(id).isPresent())
    {
        Patient p1 = repo.findById(id).get();
                p1.setGender(p.getGender());
                p1.setName(p.getName());
                p1.setPhone(p.getPhone());
                p1.setDateOfBirth(p.getDateOfBirth());
                p1.setUpdatedAt(new Date());
                p1.setUpdatedBy(p.getUpdatedBy());
                p1.setCreatedAt(repo.findById(id).get().getCreatedAt());
                p1.setCreatedBy(p.getCreatedBy());
                repo.save(p1);
    }
    else
    {
        addPatient(p);
    }


    }

    public boolean deletePatientById(Long id) {
        if (repo.findById(id).isPresent()){
            repo.deleteById(id);
            return true;
        }else
        {
            return false;
        }

    }
}
