package com.HospitalManagementSystem.HospitalSystem.controller;

import com.HospitalManagementSystem.HospitalSystem.dto.DoctorDto;
import com.HospitalManagementSystem.HospitalSystem.dto.MedicalRecordDto;
import com.HospitalManagementSystem.HospitalSystem.dto.PatientDto;
import com.HospitalManagementSystem.HospitalSystem.service.AppointmentService;
import com.HospitalManagementSystem.HospitalSystem.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/doctor")
public class DoctorController {
    @Autowired
    DoctorService doctorService;

    @GetMapping("/doctors")
    public ResponseEntity<List<DoctorDto>> getAllDoctors() {
        return new ResponseEntity<>(doctorService.getAllDoctors(), HttpStatus.FOUND);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DoctorDto> getDoctorById(@PathVariable Long id) {
        if (doctorService.getDoctorById(id) != null) {
            return new ResponseEntity<>(doctorService.getDoctorById(id), HttpStatus.FOUND);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @PostMapping("/doctors")
    public ResponseEntity<String> addDoctor(@RequestBody DoctorDto doctorDto) {
        if (doctorDto != null) {
            doctorService.addDoctor(doctorDto);
            return new ResponseEntity<>("doctor added successfully", HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>("add an accepted data", HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateDoctorData(@PathVariable Long id, @RequestBody DoctorDto doctorDto) {
        if (doctorDto != null) {
            doctorService.updateDoctorData(id, doctorDto);
            return new ResponseEntity<>("doctor data updated", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("add an accepted data", HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDoctorById(@PathVariable Long id) {
        if (doctorService.deleteDoctorById(id)) {
            doctorService.deleteDoctorById(id);
            return new ResponseEntity<>("deleted doctor successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("doctor not found", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/start")
    public ResponseEntity<PatientDto> startSession(@RequestBody MedicalRecordDto medicalRecordDto) {
        return new ResponseEntity<>(doctorService.startSession(medicalRecordDto), HttpStatus.OK);
    }


}
