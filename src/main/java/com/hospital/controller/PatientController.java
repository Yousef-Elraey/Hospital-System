package com.hospital.controller;

import com.hospital.dto.MedicalRecordDto;
import com.hospital.dto.PatientDto;
import com.hospital.service.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patient")
@RequiredArgsConstructor
public class PatientController {

  private final PatientService patientService;

    @GetMapping("/patients")
    public ResponseEntity<List<PatientDto>> getAllPatients() {
        return new ResponseEntity<>(patientService.getAllPatients(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientDto> getPatientById(@PathVariable Long id) {
            return new ResponseEntity<>(patientService.getPatientById(id), HttpStatus.OK);

    }

    @PostMapping("/patients")
    public ResponseEntity<PatientDto> addPatient(@Valid @RequestBody PatientDto patientDto) {

            return new ResponseEntity<>(patientService.addPatient(patientDto), HttpStatus.CREATED);

    }

    @PutMapping("/{id}")
    public ResponseEntity<PatientDto> updatePatientData(@PathVariable Long id,@Valid @RequestBody PatientDto patientDto) {


            return new ResponseEntity<>(patientService.updatePatientData(id, patientDto), HttpStatus.NO_CONTENT);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePatientById(@PathVariable Long id) {
       patientService.deletePatientById(id);
            return new ResponseEntity<>("deleted patient successfully", HttpStatus.NO_CONTENT);
    }

    @GetMapping("/history/{id}")
    public ResponseEntity<List<MedicalRecordDto>> showPatientHistory(@PathVariable Long id) {
        return new ResponseEntity<>(patientService.showPatientHistory(id), HttpStatus.OK);
    }


}
