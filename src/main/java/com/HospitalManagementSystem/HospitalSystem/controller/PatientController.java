package com.HospitalManagementSystem.HospitalSystem.controller;

import com.HospitalManagementSystem.HospitalSystem.entity.Patient;
import com.HospitalManagementSystem.HospitalSystem.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patient")
public class PatientController {
    @Autowired
    PatientService service;
   @GetMapping("/getAllPatients")
    public ResponseEntity<List<Patient>> getAllPatients(){
      return new ResponseEntity<>( service.getAllPatients(), HttpStatus.FOUND);
  }
  @GetMapping("/getPatientById/{id}")
  public ResponseEntity<Patient> getPatientById(@PathVariable Long id){
       if (service.getPatientById(id)==null){
           return new ResponseEntity<>(HttpStatus.NOT_FOUND);
       }else {
           return new ResponseEntity<>(service.getPatientById(id),HttpStatus.FOUND) ;
       }
  }
    @PostMapping("/addPatient")
    public ResponseEntity<String> addPatient(@RequestBody Patient p){
       if (p != null) {
           service.addPatient(p);
           return new ResponseEntity<>("patient added successfully", HttpStatus.ACCEPTED);
       }
       else{
           return new ResponseEntity<>("add an accepted data", HttpStatus.NOT_ACCEPTABLE);
       }
    }
    @PutMapping("/updatePatientData/{id}")
    public ResponseEntity<String> updatePatientData(@PathVariable Long id,@RequestBody Patient p){
        if (p!=null)
        {
            service.updatePatientData(id,p);
            return new ResponseEntity<>("patient data is updated",HttpStatus.OK);
        }
        else
            return new ResponseEntity<>("add an accepted data",HttpStatus.NOT_ACCEPTABLE);
    }
    @DeleteMapping("/deletePatientById/{id}")
    public ResponseEntity <String> deletePatientById(@PathVariable Long id){
        if (service.deletePatientById(id)){
            service.deletePatientById(id);
            return new ResponseEntity<>("deleted patient successfully",HttpStatus.OK);
        }else {
            return new ResponseEntity<>("patient not found",HttpStatus.NOT_FOUND);
        }
    }


}
