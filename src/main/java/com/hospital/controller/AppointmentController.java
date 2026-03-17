package com.hospital.controller;

import com.hospital.dto.AppointmentDto;
import com.hospital.dto.BookRequestDto;
import com.hospital.dto.BookResponseDto;
import com.hospital.dto.PatientDto;
import com.hospital.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/appointment")
public class AppointmentController {
    @Autowired
    AppointmentService appointmentService;

    @GetMapping("/appointments")
    public ResponseEntity<List<AppointmentDto>> getAllAppointments() {
        if (appointmentService.getAllAppointments() != null)
            return new ResponseEntity<>(appointmentService.getAllAppointments(), HttpStatus.FOUND);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @GetMapping("/{id}")
    public ResponseEntity<AppointmentDto> getAppointmentById(@PathVariable Long id) {
        if (appointmentService.getAppointmentById(id) != null)
            return new ResponseEntity<>(appointmentService.getAppointmentById(id), HttpStatus.FOUND);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @PostMapping("/appointments")
    public ResponseEntity<String> createAppointment(@RequestBody AppointmentDto appointmentDto) {
        if (appointmentDto != null) {
            appointmentService.createAppointment(appointmentDto);
            return new ResponseEntity<>("appointment created", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("add an accepted data", HttpStatus.BAD_REQUEST);
        }

    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateAppointment(@PathVariable Long id, @RequestBody AppointmentDto appointmentDto) {
        if (appointmentDto != null) {
            appointmentService.updateAppointment(id, appointmentDto);
            return new ResponseEntity<>("appointment updated", HttpStatus.ACCEPTED);
        } else
            return new ResponseEntity<>("add an accepted data", HttpStatus.NOT_ACCEPTABLE);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAppointment(@PathVariable Long id) {
        appointmentService.deleteAppointment(id);
        return new ResponseEntity<>("Appointment is deleted", HttpStatus.OK);
    }

    @PostMapping("/book")
    public ResponseEntity<BookResponseDto> book(@RequestBody BookRequestDto request) {

        return new ResponseEntity<>(appointmentService.book(request), HttpStatus.OK);
    }

    @PostMapping("/book-with-paid")
    public ResponseEntity<BookResponseDto> bookWithPaid(@RequestBody BookRequestDto request) {
        return new ResponseEntity<>(appointmentService.bookWithPaid(request), HttpStatus.OK);
    }

    @PutMapping("confirm/{phoneNumber}")
    public ResponseEntity<BookResponseDto> confirmBook(@PathVariable String phoneNumber) {
        return new ResponseEntity<>(appointmentService.confirmBook(phoneNumber), HttpStatus.OK);
    }

    @GetMapping("/current-patient")
    public ResponseEntity<PatientDto> currentPatient() {
        return new ResponseEntity<>(appointmentService.currentPatient(), HttpStatus.OK);

    }

    @DeleteMapping("/next")
    public ResponseEntity<PatientDto> next() {
        return new ResponseEntity<>(appointmentService.next(), HttpStatus.OK);
    }
}
