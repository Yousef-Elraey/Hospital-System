package com.hospital.medicalRecord.controller;

import com.hospital.dto.PageResponse;
import com.hospital.medicalRecord.dto.request.CreateMedicalRecordRequest;
import com.hospital.medicalRecord.dto.request.UpdateMedicalRecordRequest;
import com.hospital.medicalRecord.dto.response.CreateMedicalRecordResponse;
import com.hospital.medicalRecord.dto.response.GetMedicalRecordResponse;
import com.hospital.medicalRecord.dto.response.UpdateMedicalRecordResponse;
import com.hospital.medicalRecord.service.MedicalRecordService;
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
    public ResponseEntity<PageResponse<GetMedicalRecordResponse>> getAllRecords(@RequestParam(defaultValue = "0")int page,
                                                                                @RequestParam(defaultValue = "10")int size,
                                                                                @RequestParam(defaultValue = "id") String sortBy,
                                                                                @RequestParam(defaultValue = "asc") String direction) {
            return new ResponseEntity<>(medicalRecordService.getAllMedicalRecords(page,size,sortBy,direction), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetMedicalRecordResponse> getMedicalRecordById(@PathVariable Long id) {
        return new ResponseEntity<>(medicalRecordService.getMedicalRecordById(id), HttpStatus.OK);
    }

    @PostMapping("/medical-records")
    public ResponseEntity<CreateMedicalRecordResponse> addMedicalRecord(@Valid @RequestBody CreateMedicalRecordRequest createMedicalRecordRequest) {

        return new ResponseEntity<>(medicalRecordService.addMedicalRecord(createMedicalRecordRequest), HttpStatus.CREATED);

    }

    @PutMapping("/update")
    public ResponseEntity<UpdateMedicalRecordResponse> updateMedicalRecordData(@Valid @RequestBody UpdateMedicalRecordRequest medicalRecordRequest) {
        return new ResponseEntity<>(medicalRecordService.updateMedicalRecordData(medicalRecordRequest), HttpStatus.OK);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMedicalRecord(@PathVariable Long id) {
        medicalRecordService.deleteMedicalRecord(id);
        return new ResponseEntity<>("medical record is deleted", HttpStatus.NO_CONTENT);

    }

    @GetMapping("/medical-record-patient-id/{id}")
    public ResponseEntity<List<GetMedicalRecordResponse>> getByPatientId(@PathVariable Long id) {
        return new ResponseEntity<>(medicalRecordService.getByPatientId(id), HttpStatus.OK);
    }
}
