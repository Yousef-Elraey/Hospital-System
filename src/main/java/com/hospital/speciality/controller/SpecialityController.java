package com.hospital.speciality.controller;

import com.hospital.medicalRecord.dto.response.GetMedicalRecordResponse;
import com.hospital.speciality.dto.request.CreateSpecialityRequest;
import com.hospital.speciality.dto.request.UpdateSpecialityRequest;
import com.hospital.speciality.dto.response.CreateSpecialityResponse;
import com.hospital.speciality.dto.response.GetSpecialityResponse;
import com.hospital.speciality.dto.response.UpdateSpecialityResponse;
import com.hospital.speciality.service.SpecialityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/speciality")
@RequiredArgsConstructor
public class SpecialityController {

  private final SpecialityService specialityService;

    @GetMapping("/specialities")
    public ResponseEntity<List<GetSpecialityResponse>> getAllSpecialities() {
        return new ResponseEntity<>(specialityService.getAllSpecialities(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetSpecialityResponse> getSpecialityById(@PathVariable Long id) {
        return new ResponseEntity<>(specialityService.getSpecialityById(id), HttpStatus.OK);

    }

    @PostMapping("/specialities")
    public ResponseEntity<CreateSpecialityResponse> addSpeciality(@Valid @RequestBody CreateSpecialityRequest createSpecialityRequest) {
        return new ResponseEntity<>(specialityService.addSpeciality(createSpecialityRequest), HttpStatus.CREATED);

    }

    @PutMapping("/update")
    public ResponseEntity<UpdateSpecialityResponse> updateSpeciality(@Valid @RequestBody UpdateSpecialityRequest updateSpecialityRequest) {
        return new ResponseEntity<>(specialityService.updateSpeciality(updateSpecialityRequest), HttpStatus.OK);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSpecialityById(@PathVariable Long id) {
        specialityService.deleteSpecialityById(id);
        return new ResponseEntity<>("deleted patient successfully", HttpStatus.NO_CONTENT);
    }

}
