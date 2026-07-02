package com.hospital.appointment.controller;

import com.hospital.appointment.dto.request.CreateAppointmentRequest;
import com.hospital.appointment.dto.request.SearchAppointmentRequest;
import com.hospital.appointment.dto.request.UpdateAppointmentRequest;
import com.hospital.appointment.dto.response.CreateAppointmentResponse;
import com.hospital.appointment.dto.response.GetAppointmentResponse;
import com.hospital.appointment.dto.response.UpdateAppointmentResponse;
import com.hospital.appointment.service.AppointmentService;
import com.hospital.dto.BookRequestDto;
import com.hospital.dto.BookResponseDto;
import com.hospital.dto.PageResponse;
import com.hospital.patient.dto.request.SearchPatientRequest;
import com.hospital.patient.dto.response.GetPatientResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/appointment")
@RequiredArgsConstructor
public class AppointmentController {
   private final AppointmentService appointmentService;

    @GetMapping("/appointments")
    public ResponseEntity<PageResponse<GetAppointmentResponse>> getAllAppointments
                                                                (@RequestParam(defaultValue = "0")int page
                                                                 ,@RequestParam(defaultValue = "10") int size,
                                                                 @RequestParam(defaultValue = "id") String sortBy,
                                                                 @RequestParam(defaultValue = "asc") String direction) {
        return new ResponseEntity<>(appointmentService.getAllAppointments(page,size,sortBy,direction), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetAppointmentResponse> getAppointmentById(@PathVariable Long id) {
        return new ResponseEntity<>(appointmentService.getAppointmentById(id), HttpStatus.OK);
    }

    @PostMapping("/appointments")
    public ResponseEntity<CreateAppointmentResponse> createAppointment(@Valid @RequestBody CreateAppointmentRequest createAppointmentRequest) {

        return new ResponseEntity<>(appointmentService.createAppointment(createAppointmentRequest), HttpStatus.CREATED);

    }

    @PutMapping("/update")
    public ResponseEntity<UpdateAppointmentResponse> updateAppointment(@Valid @RequestBody UpdateAppointmentRequest updateAppointmentRequest, HttpServletRequest request) {
        return new ResponseEntity<>(appointmentService.updateAppointment(updateAppointmentRequest), HttpStatus.OK);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAppointment(@PathVariable Long id) {
        appointmentService.deleteAppointment(id);
        return new ResponseEntity<>("Appointment is deleted", HttpStatus.NO_CONTENT);
    }

    @PostMapping("/book")
    public ResponseEntity<BookResponseDto> book(@Valid @RequestBody BookRequestDto bookRequestDto) {

        return new ResponseEntity<>(appointmentService.book(bookRequestDto), HttpStatus.CREATED);
    }

    @PostMapping("/book-with-paid")
    public ResponseEntity<BookResponseDto> bookWithPaid(@Valid @RequestBody BookRequestDto bookRequestDto) {
        return new ResponseEntity<>(appointmentService.bookWithPaid(bookRequestDto), HttpStatus.CREATED);
    }

    @PutMapping("confirm/{phoneNumber}")
    public ResponseEntity<BookResponseDto> confirmBook(@PathVariable String phoneNumber) {
        return new ResponseEntity<>(appointmentService.confirmBook(phoneNumber), HttpStatus.CREATED);
    }

    @GetMapping("/current-patient")
    public ResponseEntity<GetPatientResponse> currentPatient() {
        return new ResponseEntity<>(appointmentService.currentPatient(), HttpStatus.OK);

    }

    @PutMapping("/next")
    public ResponseEntity<GetPatientResponse> next() {
        return new ResponseEntity<>(appointmentService.next(), HttpStatus.OK);
    }

    @PostMapping("/search")
    public ResponseEntity<PageResponse<GetAppointmentResponse>> searchAppointment(@RequestBody SearchAppointmentRequest searchAppointmentRequest,
                                                                                  @RequestParam(defaultValue = "0") int page,
                                                                                  @RequestParam(defaultValue = "10") int size,
                                                                                  @RequestParam(defaultValue = "id") String sortBy,
                                                                                  @RequestParam(defaultValue = "asc") String direction) {
        return new ResponseEntity<>(appointmentService.searchAppointment(page, size, sortBy, direction, searchAppointmentRequest), HttpStatus.OK);
    }
}
