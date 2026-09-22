package com.hospital.treatment.controller;

import com.hospital.dto.PageResponse;
import com.hospital.treatment.dto.request.CreateTreatmentRequest;
import com.hospital.treatment.dto.request.SearchTreatmentRequest;
import com.hospital.treatment.dto.request.UpdateTreatmentRequest;
import com.hospital.treatment.dto.response.CreateTreatmentResponse;
import com.hospital.treatment.dto.response.GetTreatmentResponse;
import com.hospital.treatment.dto.response.UpdateTreatmentResponse;
import com.hospital.treatment.service.TreatmentService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/treatment")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
public class TreatmentController {
    private final TreatmentService treatmentService;

    @GetMapping("/treatments")
    public ResponseEntity<PageResponse<GetTreatmentResponse>> getAllTreatments(@ParameterObject SearchTreatmentRequest searchTreatmentRequest,
                                                                               @RequestParam(defaultValue = "0")int page,
                                                                               @RequestParam(defaultValue = "10")int size,
                                                                               @RequestParam(defaultValue = "id") String sortBy,
                                                                               @RequestParam(defaultValue = "asc") String direction){
        return new ResponseEntity<>(treatmentService.getAllTreatments(searchTreatmentRequest,page,size,sortBy,direction), HttpStatus.OK);
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
        return new ResponseEntity<>("treatment deleted",HttpStatus.NO_CONTENT);
    }

}
