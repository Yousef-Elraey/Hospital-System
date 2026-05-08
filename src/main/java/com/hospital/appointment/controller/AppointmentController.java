package com.hospital.appointment.controller;
import com.hospital.appointment.dto.request.UpdateAppointmentRequest;
import com.hospital.appointment.dto.request.CreateAppointmentRequest;
import com.hospital.appointment.dto.response.CreateAppointmentResponse;
import com.hospital.appointment.dto.response.GetAppointmentResponse;
import com.hospital.appointment.dto.response.UpdateAppointmentResponse;
import com.hospital.dto.BookRequestDto;
import com.hospital.dto.BookResponseDto;
import com.hospital.patient.dto.request.CreatePatientRequest;
import com.hospital.appointment.service.AppointmentService;
import com.hospital.patient.dto.response.GetPatientResponse;
import jakarta.servlet.http.HttpServletRequest;
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
    public ResponseEntity<List<GetAppointmentResponse>> getAllAppointments() {
        return new ResponseEntity<>(appointmentService.getAllAppointments(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetAppointmentResponse> getAppointmentById(@PathVariable Long id) {
        return new ResponseEntity<>(appointmentService.getAppointmentById(id), HttpStatus.OK);
    }

    @PostMapping("/appointments")
    public ResponseEntity<CreateAppointmentResponse> createAppointment(@Valid @RequestBody CreateAppointmentRequest createAppointmentRequest, HttpServletRequest request) {

        return new ResponseEntity<>(appointmentService.createAppointment(createAppointmentRequest,request), HttpStatus.CREATED);

    }

    @PutMapping("/update")
    public ResponseEntity<UpdateAppointmentResponse> updateAppointment(@Valid @RequestBody UpdateAppointmentRequest updateAppointmentRequest, HttpServletRequest request) {
        return new ResponseEntity<>(appointmentService.updateAppointment(updateAppointmentRequest,request), HttpStatus.OK);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAppointment(@PathVariable Long id) {
        appointmentService.deleteAppointment(id);
        return new ResponseEntity<>("Appointment is deleted", HttpStatus.NO_CONTENT);
    }

    @PostMapping("/book")
    public ResponseEntity<BookResponseDto> book(@Valid @RequestBody BookRequestDto bookRequestDto, HttpServletRequest request) {

        return new ResponseEntity<>(appointmentService.book(bookRequestDto,request), HttpStatus.CREATED);
    }

    @PostMapping("/book-with-paid")
    public ResponseEntity<BookResponseDto> bookWithPaid(@Valid @RequestBody BookRequestDto bookRequestDto, HttpServletRequest request) {
        return new ResponseEntity<>(appointmentService.bookWithPaid(bookRequestDto,request), HttpStatus.CREATED);
    }

    @PutMapping("confirm/{phoneNumber}")
    public ResponseEntity<BookResponseDto> confirmBook(@PathVariable String phoneNumber) {
        return new ResponseEntity<>(appointmentService.confirmBook(phoneNumber), HttpStatus.CREATED);
    }

    @GetMapping("/current-patient")
    public ResponseEntity<GetPatientResponse> currentPatient(HttpServletRequest request) {
        return new ResponseEntity<>(appointmentService.currentPatient(request), HttpStatus.OK);

    }

    @PutMapping("/next")
    public ResponseEntity<GetPatientResponse> next(HttpServletRequest request) {
        return new ResponseEntity<>(appointmentService.next(request), HttpStatus.OK);
    }
}
