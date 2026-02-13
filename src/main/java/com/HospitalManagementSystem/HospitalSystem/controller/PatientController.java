package com.HospitalManagementSystem.HospitalSystem.controller;

import com.HospitalManagementSystem.HospitalSystem.dto.PatientDto;
import com.HospitalManagementSystem.HospitalSystem.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
public class PatientController {
    @Autowired
    PatientService patientService;

    @GetMapping("/patients")
    public ResponseEntity<List<PatientDto>> getAllPatients() {
        return new ResponseEntity<>(patientService.getAllPatients(), HttpStatus.FOUND);
    }

    @GetMapping("/patients/{id}")
    public ResponseEntity<PatientDto> getPatientById(@PathVariable Long id) {
        if (patientService.getPatientById(id) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(patientService.getPatientById(id), HttpStatus.FOUND);
        }
    }

    @PostMapping("/patients")
    public ResponseEntity<String> addPatient(@RequestBody PatientDto patientDto) {
        if (patientDto != null) {
            patientService.addPatient(patientDto);
            return new ResponseEntity<>("patient added successfully", HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>("add an accepted data", HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @PutMapping("/patients/{id}")
    public ResponseEntity<String> updatePatientData(@PathVariable Long id, @RequestBody PatientDto patientDto) {
        if (patientDto != null) {
            patientService.updatePatientData(id, patientDto);
            return new ResponseEntity<>("patient data is updated", HttpStatus.OK);
        } else
            return new ResponseEntity<>("add an accepted data", HttpStatus.NOT_ACCEPTABLE);
    }

    @DeleteMapping("/patients/{id}")
    public ResponseEntity<String> deletePatientById(@PathVariable Long id) {
        if (patientService.deletePatientById(id)) {
            patientService.deletePatientById(id);
            return new ResponseEntity<>("deleted patient successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("patient not found", HttpStatus.NOT_FOUND);
        }
    }


}
