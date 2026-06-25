package com.hospital.doctor.controller;

import com.hospital.doctor.dto.request.CreateDoctorRequest;
import com.hospital.doctor.dto.request.SearchDoctorRequest;
import com.hospital.doctor.dto.request.UpdateDoctorRequest;
import com.hospital.doctor.dto.response.CreateDoctorResponse;
import com.hospital.doctor.dto.response.GetDoctorResponse;
import com.hospital.doctor.dto.response.UpdateDoctorResponse;
import com.hospital.doctor.service.DoctorService;
import com.hospital.dto.PageResponse;
import com.hospital.medicalRecord.dto.request.CreateMedicalRecordRequest;
import com.hospital.patient.dto.response.GetPatientResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/doctor")
@RequiredArgsConstructor
public class DoctorController {
private final DoctorService doctorService;

    @GetMapping("/doctors")
    public ResponseEntity<PageResponse<GetDoctorResponse>> getAllDoctors(@RequestParam(defaultValue = "0")int page,
                                                                         @RequestParam(defaultValue = "10")int size,
                                                                         @RequestParam(defaultValue = "id") String sortBy,
                                                                         @RequestParam(defaultValue = "asc") String direction) {
        return new ResponseEntity<>(doctorService.getAllDoctors(page,size,sortBy,direction), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetDoctorResponse> getDoctorById(@PathVariable Long id) {
        return new ResponseEntity<>(doctorService.getDoctorById(id), HttpStatus.OK);
    }

    @PostMapping("/doctors")
    public ResponseEntity<CreateDoctorResponse> addDoctor(@Valid @RequestBody CreateDoctorRequest createDoctorRequest) {
        return new ResponseEntity<>(doctorService.addDoctor(createDoctorRequest), HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<UpdateDoctorResponse> updateDoctorData(@Valid @RequestBody UpdateDoctorRequest DoctorRequest) {
        return new ResponseEntity<>(doctorService.updateDoctorData(DoctorRequest), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDoctorById(@PathVariable Long id) {
        doctorService.deleteDoctorById(id);
        return new ResponseEntity<>("deleted doctor successfully", HttpStatus.NO_CONTENT);
    }

    @PostMapping("/start")
    public ResponseEntity<GetPatientResponse> startSession(@Valid @RequestBody CreateMedicalRecordRequest createMedicalRecordRequest) {
        return new ResponseEntity<>(doctorService.startSession(createMedicalRecordRequest), HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<GetDoctorResponse> searchDoctor(@Valid @RequestBody SearchDoctorRequest searchDoctorRequest) {
        return new ResponseEntity<>(doctorService.searchDoctor(searchDoctorRequest), HttpStatus.OK);
    }

}
