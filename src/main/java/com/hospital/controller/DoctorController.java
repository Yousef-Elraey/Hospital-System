package com.hospital.controller;

import com.hospital.dto.DoctorDto;
import com.hospital.dto.MedicalRecordDto;
import com.hospital.dto.PatientDto;
import com.hospital.service.DoctorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/doctor")
@RequiredArgsConstructor
public class DoctorController {
private final DoctorService doctorService;

    @GetMapping("/doctors")
    public ResponseEntity<List<DoctorDto>> getAllDoctors() {
        return new ResponseEntity<>(doctorService.getAllDoctors(), HttpStatus.FOUND);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DoctorDto> getDoctorById(@PathVariable Long id) {
            return new ResponseEntity<>(doctorService.getDoctorById(id), HttpStatus.FOUND);
    }

    @PostMapping("/doctors")
    public ResponseEntity<DoctorDto> addDoctor(@Valid @RequestBody DoctorDto doctorDto) {

            return new ResponseEntity<>(doctorService.addDoctor(doctorDto), HttpStatus.ACCEPTED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DoctorDto> updateDoctorData(@PathVariable Long id,@Valid @RequestBody DoctorDto doctorDto) {


            return new ResponseEntity<>(doctorService.updateDoctorData(id, doctorDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDoctorById(@PathVariable Long id) {
            doctorService.deleteDoctorById(id);
            return new ResponseEntity<>("deleted doctor successfully", HttpStatus.OK);
    }

    @PostMapping("/start")
    public ResponseEntity<PatientDto> startSession(@Valid @RequestBody MedicalRecordDto medicalRecordDto) {
        return new ResponseEntity<>(doctorService.startSession(medicalRecordDto), HttpStatus.OK);
    }

}
