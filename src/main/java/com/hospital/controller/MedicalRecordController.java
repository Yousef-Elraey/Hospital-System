package com.hospital.controller;

import com.hospital.dto.MedicalRecordDto;
import com.hospital.service.MedicalRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/medical-record")
public class MedicalRecordController {
    @Autowired
    MedicalRecordService medicalRecordService;

    @GetMapping("/medical-records")
    public ResponseEntity<List<MedicalRecordDto>> getAllRecords() {
        if (medicalRecordService.getAllRecords() != null)
            return new ResponseEntity<>(medicalRecordService.getAllRecords(), HttpStatus.FOUND);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicalRecordDto> getMedicalRecordById(@PathVariable Long id) {
        if (medicalRecordService.getMedicalRecordById(id) != null)
            return new ResponseEntity<>(medicalRecordService.getMedicalRecordById(id), HttpStatus.FOUND);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/medical-records")
    public ResponseEntity<String> addMedicalRecord(@RequestBody MedicalRecordDto medicalRecordDto) {
        if (medicalRecordDto != null) {
            medicalRecordService.addMedicalRecord(medicalRecordDto);
            return new ResponseEntity<>("medical record added", HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>("add an accepted data", HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateMedicalRecordData(@PathVariable Long id, @RequestBody MedicalRecordDto medicalRecordDto) {
        if (medicalRecordDto != null) {
            medicalRecordService.updateMedicalRecordData(id, medicalRecordDto);
            return new ResponseEntity<>("medical record updated", HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>("add an accepted data", HttpStatus.BAD_REQUEST);
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMedicalRecord(@PathVariable Long id) {
        medicalRecordService.deleteMedicalRecord(id);
            return new ResponseEntity<>("medical record is deleted", HttpStatus.OK);

    }

    @GetMapping("/medical-record-patient-id/{id}")
    public ResponseEntity<List<MedicalRecordDto>> getByPatientId(@PathVariable Long id) {
        return new ResponseEntity<>(medicalRecordService.getByPatientId(id), HttpStatus.OK);
    }
}
