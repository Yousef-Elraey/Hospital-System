package com.hospital.controller;

import com.hospital.dto.MedicalRecordDto;
import com.hospital.service.MedicalRecordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/medical-record")
@RequiredArgsConstructor
public class MedicalRecordController {

   private final MedicalRecordService medicalRecordService;

    @GetMapping("/medical-records")
    public ResponseEntity<List<MedicalRecordDto>> getAllRecords() {
            return new ResponseEntity<>(medicalRecordService.getAllRecords(), HttpStatus.FOUND);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicalRecordDto> getMedicalRecordById(@PathVariable Long id) {
            return new ResponseEntity<>(medicalRecordService.getMedicalRecordById(id), HttpStatus.FOUND);
    }

    @PostMapping("/medical-records")
    public ResponseEntity<MedicalRecordDto> addMedicalRecord(@Valid @RequestBody MedicalRecordDto medicalRecordDto) {

            return new ResponseEntity<>(medicalRecordService.addMedicalRecord(medicalRecordDto), HttpStatus.ACCEPTED);

    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicalRecordDto> updateMedicalRecordData(@PathVariable Long id,@Valid @RequestBody MedicalRecordDto medicalRecordDto) {


            return new ResponseEntity<>(medicalRecordService.updateMedicalRecordData(id, medicalRecordDto), HttpStatus.ACCEPTED);


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
