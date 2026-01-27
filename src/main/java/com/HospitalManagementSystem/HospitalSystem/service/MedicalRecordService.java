package com.HospitalManagementSystem.HospitalSystem.service;

import com.HospitalManagementSystem.HospitalSystem.entity.MedicalRecord;
import com.HospitalManagementSystem.HospitalSystem.repository.MedicalRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class MedicalRecordService {
    @Autowired
    MedicalRecordRepository repo;

    public List<MedicalRecord> getAllRecords() {
       return repo.findAll();

    }

    public MedicalRecord getMedicalRecordById(Long id) {
       return repo.findById(id).orElse(null);
    }

    public void addMedicalRecord(MedicalRecord record) {
        record.setCreatedAt(new Date());
        record.setUpdatedAt(new Date());
        repo.save(record);
    }

    public void updateMedicalRecordData(long id, MedicalRecord record) {
        if (repo.findById(id).isPresent()){
            MedicalRecord temp = repo.findById(id).get();
            temp.setUpdatedAt(new Date());
            temp.setUpdatedBy(record.getUpdatedBy());
            temp.setCreatedAt(repo.findById(id).get().getCreatedAt());
            temp.setCreatedBy(record.getCreatedBy());
            temp.setDoctor(record.getDoctor());
            temp.setPatient(record.getPatient());
            temp.setDiagnose(record.getDiagnose());
            temp.setTreatment(record.getTreatment());
            repo.save(temp);
    }else {
        addMedicalRecord(record);
        }
    }

    public boolean deleteMedicalRecord(Long id) {
        if (repo.findById(id).isPresent()){
                repo.deleteById(id);
                return true;
        }else {
            return false;
        }
    }
}
