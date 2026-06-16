package com.hospital.treatment.controller;

import com.hospital.treatment.dto.request.CreateTreatmentRequest;
import com.hospital.treatment.dto.request.UpdateTreatmentRequest;
import com.hospital.treatment.dto.response.CreateTreatmentResponse;
import com.hospital.treatment.dto.response.GetTreatmentResponse;
import com.hospital.treatment.dto.response.UpdateTreatmentResponse;
import com.hospital.treatment.service.TreatmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/treatment")
@RequiredArgsConstructor
public class TreatmentController {
    private final TreatmentService treatmentService;

    @GetMapping("/treatments")
    public ResponseEntity<List<GetTreatmentResponse>> getAllTreatments(){
        return new ResponseEntity<>(treatmentService.getAllTreatments(), HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<GetTreatmentResponse> getTreatmentById(@PathVariable Long id){
        return new ResponseEntity<>(treatmentService.getTreatmentById(id),HttpStatus.OK);

    }
    @PostMapping("/treatments")
    public ResponseEntity<CreateTreatmentResponse> createTreatment(@RequestBody CreateTreatmentRequest createTreatmentRequest){
        return new ResponseEntity<>(treatmentService.createTreatment(createTreatmentRequest),HttpStatus.CREATED);
    }
    @PutMapping("/update")
    public ResponseEntity<UpdateTreatmentResponse> updateTreatment(@RequestBody UpdateTreatmentRequest updateTreatmentRequest){
        return new ResponseEntity<>(treatmentService.updateTreatment(updateTreatmentRequest),HttpStatus.OK);



    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTreatmentById(@PathVariable Long id){
        treatmentService.deleteTreatment(id);
        return new ResponseEntity<>("diagnose deleted",HttpStatus.NO_CONTENT);
    }

}
