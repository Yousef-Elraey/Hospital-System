package com.hospital.medical_record.controller;

import com.hospital.dto.PageResponse;
import com.hospital.medical_record.dto.request.CreateMedicalRecordRequest;
import com.hospital.medical_record.dto.request.SearchMedicalRecordRequest;
import com.hospital.medical_record.dto.request.UpdateMedicalRecordRequest;
import com.hospital.medical_record.dto.response.CreateMedicalRecordResponse;
import com.hospital.medical_record.dto.response.GetMedicalRecordResponse;
import com.hospital.medical_record.dto.response.SearchMedicalRecordResponse;
import com.hospital.medical_record.dto.response.UpdateMedicalRecordResponse;
import com.hospital.medical_record.service.MedicalRecordService;
import com.hospital.patient.dto.request.SearchPatientRequest;
import com.hospital.patient.dto.response.GetPatientResponse;
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

    @PostMapping("/search")
    public ResponseEntity<PageResponse<SearchMedicalRecordResponse>> searchMedicalRecord(@RequestBody SearchMedicalRecordRequest searchMedicalRecordRequest,
                                                                                         @RequestParam(defaultValue = "0") int page,
                                                                                         @RequestParam(defaultValue = "10") int size,
                                                                                         @RequestParam(defaultValue = "id") String sortBy,
                                                                                         @RequestParam(defaultValue = "asc") String direction) {
        return new ResponseEntity<>(medicalRecordService.searchMedicalRecord(page, size, sortBy, direction, searchMedicalRecordRequest), HttpStatus.OK);
    }
}
