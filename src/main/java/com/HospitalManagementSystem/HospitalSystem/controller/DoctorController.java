package com.HospitalManagementSystem.HospitalSystem.controller;

import com.HospitalManagementSystem.HospitalSystem.dto.DoctorDto;
import com.HospitalManagementSystem.HospitalSystem.entity.Doctor;
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
    DoctorService service;

    @GetMapping("/getAllDoctors")
    public ResponseEntity<List<DoctorDto>> getAllDoctors() {
        return new ResponseEntity<>(service.getAllDoctors(), HttpStatus.FOUND);
    }

    @GetMapping("/getDoctorById/{id}")
    public ResponseEntity<DoctorDto> getDoctorById(@PathVariable Long id) {
        if (service.getDoctorById(id) != null) {
            return new ResponseEntity<>(service.getDoctorById(id), HttpStatus.FOUND);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @PostMapping("/addDoctor")
    public ResponseEntity<String> addDoctor(@RequestBody DoctorDto doctorDto) {
        if (doctorDto != null) {
            service.addDoctor(doctorDto);
            return new ResponseEntity<>("doctor added successfully", HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>("add an accepted data", HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @PutMapping("/updateDoctorData/{id}")
    public ResponseEntity<String> updateDoctorData(@PathVariable Long id, @RequestBody DoctorDto doctorDto) {
        if (doctorDto != null) {
            service.updateDoctorData(id, doctorDto);
            return new ResponseEntity<>("doctor data updated", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("add an accepted data", HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @DeleteMapping("/deleteDoctor/{id}")
    public ResponseEntity<String> deleteDoctorById(@PathVariable Long id) {
        if (service.deleteDoctorById(id)) {
            service.deleteDoctorById(id);
            return new ResponseEntity<>("deleted doctor successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("doctor not found", HttpStatus.NOT_FOUND);
        }
    }


}
