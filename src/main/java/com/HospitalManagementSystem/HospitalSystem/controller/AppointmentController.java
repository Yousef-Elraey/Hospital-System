package com.HospitalManagementSystem.HospitalSystem.controller;

import com.HospitalManagementSystem.HospitalSystem.entity.Appointment;
import com.HospitalManagementSystem.HospitalSystem.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/appointment")
public class AppointmentController {
    @Autowired
    AppointmentService service;
    @GetMapping("/getAllAppointments")
    public ResponseEntity<List<Appointment>> getAllAppointments(){
        if (service.getAllAppointments() == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity<>(service.getAllAppointments(), HttpStatus.FOUND);

    }
    @GetMapping("/getAppointmentById/{id}")
    public ResponseEntity<Appointment> getAppointmentById(@PathVariable Long id) {
        if (service.getAppointmentById(id) != null)
            return new ResponseEntity<>(service.getAppointmentById(id),HttpStatus.FOUND);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }
    @PostMapping("/createAppointment")
    public ResponseEntity<String> createAppointment(@RequestBody Appointment appointment){
        if (appointment != null){
            service.createAppointment(appointment);
            return new ResponseEntity<>("appointment created",HttpStatus.CREATED);
        }
        else{
            return new ResponseEntity<>("add an accepted data",HttpStatus.BAD_REQUEST);
        }

    }
    @PutMapping("/updateAppointment/{id}")
    public ResponseEntity<String> updateAppointment(@PathVariable Long id , @RequestBody Appointment appointment){
        if (appointment!=null)
        {
            service.updateAppointment(id,appointment);
            return new ResponseEntity<>("appointment updated",HttpStatus.ACCEPTED);
        }
        else
            return new ResponseEntity<>("add an accepted data",HttpStatus.NOT_ACCEPTABLE);
        }
 @DeleteMapping("/deleteAppointment/{id}")
 public ResponseEntity<String> deleteAppointment(@PathVariable Long id){
       if (service.deleteAppointment(id))
           return new ResponseEntity<>("Appointment is deleted",HttpStatus.OK);
       else
           return new ResponseEntity<>("Appointment not found",HttpStatus.NOT_FOUND);

 }
}
