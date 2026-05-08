package com.hospital.medicalRecord.controller;

import com.hospital.medicalRecord.dto.request.CreateMedicalRecordRequest;
import com.hospital.medicalRecord.dto.request.UpdateMedicalRecordRequest;
import com.hospital.medicalRecord.dto.response.CreateMedicalRecordResponse;
import com.hospital.medicalRecord.dto.response.GetMedicalRecordResponse;
import com.hospital.medicalRecord.dto.response.UpdateMedicalRecordResponse;
import com.hospital.medicalRecord.service.MedicalRecordService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<List<GetMedicalRecordResponse>> getAllRecords() {
            return new ResponseEntity<>(medicalRecordService.getAllRecords(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetMedicalRecordResponse> getMedicalRecordById(@PathVariable Long id) {
        return new ResponseEntity<>(medicalRecordService.getMedicalRecordById(id), HttpStatus.OK);
    }

    @PostMapping("/medical-records")
    public ResponseEntity<CreateMedicalRecordResponse> addMedicalRecord(@Valid @RequestBody CreateMedicalRecordRequest createMedicalRecordRequest, HttpServletRequest request) {

        return new ResponseEntity<>(medicalRecordService.addMedicalRecord(createMedicalRecordRequest,request), HttpStatus.CREATED);

    }

    @PutMapping("/update")
    public ResponseEntity<UpdateMedicalRecordResponse> updateMedicalRecordData(@Valid @RequestBody UpdateMedicalRecordRequest medicalRecordRequest, HttpServletRequest request) {
        return new ResponseEntity<>(medicalRecordService.updateMedicalRecordData(medicalRecordRequest,request), HttpStatus.OK);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMedicalRecord(@PathVariable Long id) {
        medicalRecordService.deleteMedicalRecord(id);
        return new ResponseEntity<>("medical record is deleted", HttpStatus.NO_CONTENT);

    }

    @GetMapping("/medical-record-patient-id/{id}")
    public ResponseEntity<List<GetMedicalRecordResponse>> getByPatientId(@PathVariable Long id, HttpServletRequest request) {
        return new ResponseEntity<>(medicalRecordService.getByPatientId(id,request), HttpStatus.OK);
    }
}
