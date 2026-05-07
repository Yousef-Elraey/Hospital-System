package com.hospital.patient.controller;

import com.hospital.medicalRecord.dto.request.CreateMedicalRecordRequest;
import com.hospital.medicalRecord.dto.response.GetMedicalRecordResponse;
import com.hospital.patient.dto.request.CreatePatientRequest;
import com.hospital.patient.dto.request.UpdatePatientRequest;
import com.hospital.patient.dto.response.CreatePatientResponse;
import com.hospital.patient.dto.response.GetPatientResponse;
import com.hospital.patient.dto.response.UpdatePatientResponse;
import com.hospital.patient.service.PatientService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patient")
@RequiredArgsConstructor
public class PatientController {

  private final PatientService patientService;

    @GetMapping("/patients")
    public ResponseEntity<List<GetPatientResponse>> getAllPatients() {
        return new ResponseEntity<>(patientService.getAllPatients(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetPatientResponse> getPatientById(@PathVariable Long id) {
        return new ResponseEntity<>(patientService.getPatientById(id), HttpStatus.OK);

    }

    @PostMapping("/patients")
    public ResponseEntity<CreatePatientResponse> addPatient(@Valid @RequestBody CreatePatientRequest createPatientRequest) {

        return new ResponseEntity<>(patientService.addPatient(createPatientRequest), HttpStatus.CREATED);

    }

    @PutMapping("/update")
    public ResponseEntity<UpdatePatientResponse> updatePatientData(@Valid @RequestBody UpdatePatientRequest updatePatientRequest) {
        return new ResponseEntity<>(patientService.updatePatientData(updatePatientRequest ), HttpStatus.OK);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePatientById(@PathVariable Long id) {
        patientService.deletePatientById(id);
        return new ResponseEntity<>("deleted patient successfully", HttpStatus.NO_CONTENT);
    }

    @GetMapping("/history/{id}")
    public ResponseEntity<List<GetMedicalRecordResponse>> showPatientHistory(@PathVariable Long id) {
        return new ResponseEntity<>(patientService.showPatientHistory(id), HttpStatus.OK);
    }

}
