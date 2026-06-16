package com.hospital.diagnose.controller;

import com.hospital.diagnose.dto.request.CreateDiagnoseRequest;
import com.hospital.diagnose.dto.request.UpdateDiagnoseRequest;
import com.hospital.diagnose.dto.response.CreateDiagnoseResponse;
import com.hospital.diagnose.dto.response.GetDiagnoseResponse;
import com.hospital.diagnose.dto.response.UpdateDiagnoseResponse;
import com.hospital.diagnose.service.DiagnoseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/diagnose")
@RequiredArgsConstructor
public class DiagnoseController {
    private final DiagnoseService diagnoseService;

    @GetMapping("/diagnoses")
    public ResponseEntity<List<GetDiagnoseResponse>> getAllDiagnoses(){
        return new ResponseEntity<>(diagnoseService.getAllDiagnoses(), HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<GetDiagnoseResponse> getDiagnoseById(@PathVariable Long id){
        return new ResponseEntity<>(diagnoseService.getDiagnoseById(id),HttpStatus.OK);

    }
    @PostMapping("/diagnoses")
    public ResponseEntity<CreateDiagnoseResponse> createDiagnose(@RequestBody CreateDiagnoseRequest createDiagnoseRequest){
        return new ResponseEntity<>(diagnoseService.createDiagnose(createDiagnoseRequest),HttpStatus.CREATED);
    }
    @PutMapping("/update")
    public ResponseEntity<UpdateDiagnoseResponse> updateDiagnose(@RequestBody UpdateDiagnoseRequest updateDiagnoseRequest){
        return new ResponseEntity<>(diagnoseService.updateDiagnose(updateDiagnoseRequest),HttpStatus.OK);



    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDiagnoseById(@PathVariable Long id){
        diagnoseService.deleteDiagnose(id);
        return new ResponseEntity<>("diagnose deleted",HttpStatus.NO_CONTENT);
    }

}
