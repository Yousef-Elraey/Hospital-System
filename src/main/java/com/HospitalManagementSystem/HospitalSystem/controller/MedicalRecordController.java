package com.HospitalManagementSystem.HospitalSystem.controller;

import com.HospitalManagementSystem.HospitalSystem.dto.MedicalRecordDto;
import com.HospitalManagementSystem.HospitalSystem.entity.MedicalRecord;
import com.HospitalManagementSystem.HospitalSystem.service.MedicalRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/medical_record")
public class MedicalRecordController {
   @Autowired
    MedicalRecordService service;

    @GetMapping("/getAllMedicalRecords")
    public ResponseEntity<List<MedicalRecordDto>> getAllRecords() {
        if (service.getAllRecords() != null)
            return new ResponseEntity<>(service.getAllRecords(), HttpStatus.FOUND);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/getMedicalRecordById/{id}")
    public ResponseEntity<MedicalRecordDto> getMedicalRecordById(@PathVariable Long id) {
        if (service.getMedicalRecordById(id) != null)
            return new ResponseEntity<>(service.getMedicalRecordById(id), HttpStatus.FOUND);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/addMedicalRecord")
    public ResponseEntity<String> addMedicalRecord(@RequestBody MedicalRecordDto medicalRecordDto) {
        if (medicalRecordDto != null) {
            service.addMedicalRecord(medicalRecordDto);
            return new ResponseEntity<>("medical record added", HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>("add an accepted data", HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/updateMedicalRecordData/{id}")
    public ResponseEntity<String> updateMedicalRecordData(@PathVariable Long id, @RequestBody MedicalRecordDto medicalRecordDto) {
        if (medicalRecordDto != null) {
            service.updateMedicalRecordData(id, medicalRecordDto);
            return new ResponseEntity<>("medical record updated", HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>("add an accepted data", HttpStatus.BAD_REQUEST);
        }

    }

    @DeleteMapping("/deleteMedicalRecordById/{id}")
    public ResponseEntity<String> deleteMedicalRecord(@PathVariable Long id) {
        if (service.deleteMedicalRecord(id))
            return new ResponseEntity<>("medical record is deleted", HttpStatus.OK);
        else
            return new ResponseEntity<>("medical record not found", HttpStatus.NOT_FOUND);
    }
}
