package com.hospital.controller;

import com.hospital.dto.AppointmentDto;
import com.hospital.dto.BookRequestDto;
import com.hospital.dto.BookResponseDto;
import com.hospital.dto.PatientDto;
import com.hospital.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/appointment")
@RequiredArgsConstructor
public class AppointmentController {
   private final AppointmentService appointmentService;

    @GetMapping("/appointments")
    public ResponseEntity<List<AppointmentDto>> getAllAppointments() {
            return new ResponseEntity<>(appointmentService.getAllAppointments(), HttpStatus.FOUND);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppointmentDto> getAppointmentById(@PathVariable Long id) {
            return new ResponseEntity<>(appointmentService.getAppointmentById(id), HttpStatus.FOUND);
    }

    @PostMapping("/appointments")
    public ResponseEntity<AppointmentDto> createAppointment(@Valid @RequestBody AppointmentDto appointmentDto) {

            return new ResponseEntity<>(appointmentService.createAppointment(appointmentDto), HttpStatus.CREATED);

    }

    @PutMapping("/{id}")
    public ResponseEntity<AppointmentDto> updateAppointment(@PathVariable Long id, @Valid @RequestBody AppointmentDto appointmentDto) {
            return new ResponseEntity<>(appointmentService.updateAppointment(id, appointmentDto), HttpStatus.ACCEPTED);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAppointment(@PathVariable Long id) {
        appointmentService.deleteAppointment(id);
        return new ResponseEntity<>("Appointment is deleted", HttpStatus.OK);
    }

    @PostMapping("/book")
    public ResponseEntity<BookResponseDto> book(@Valid @RequestBody BookRequestDto request) {

        return new ResponseEntity<>(appointmentService.book(request), HttpStatus.OK);
    }

    @PostMapping("/book-with-paid")
    public ResponseEntity<BookResponseDto> bookWithPaid(@Valid @RequestBody BookRequestDto request) {
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

    @PutMapping("/next")
    public ResponseEntity<PatientDto> next() {
        return new ResponseEntity<>(appointmentService.next(), HttpStatus.OK);
    }
}
