package com.hospital.timeSlots.controller;

import com.hospital.appointment.dto.request.CreateAppointmentRequest;
import com.hospital.appointment.dto.request.UpdateAppointmentRequest;
import com.hospital.appointment.dto.response.CreateAppointmentResponse;
import com.hospital.appointment.dto.response.GetAppointmentResponse;
import com.hospital.appointment.dto.response.UpdateAppointmentResponse;
import com.hospital.dto.PageResponse;
import com.hospital.timeSlots.dto.request.CreateTimeSlotsRequest;
import com.hospital.timeSlots.dto.request.GenerateTimeSlotsRequest;
import com.hospital.timeSlots.dto.request.UpdateTimeSlotsRequest;
import com.hospital.timeSlots.dto.response.CreateTimeSlotsResponse;
import com.hospital.timeSlots.dto.response.GetTimeSlotsResponse;
import com.hospital.timeSlots.dto.response.UpdateTimeSlotsResponse;
import com.hospital.timeSlots.service.TimeSlotsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/time-slots")
@RequiredArgsConstructor
public class TimeSlotsController {
    private final TimeSlotsService timeSlotsService;

    @GetMapping("time-slots")
    public ResponseEntity<PageResponse<GetTimeSlotsResponse>> getAllTimeSlots(@RequestParam(defaultValue = "0")int page,
                                                                              @RequestParam(defaultValue = "10")int size,
                                                                              @RequestParam(defaultValue = "id") String sortBy,
                                                                              @RequestParam(defaultValue = "asc") String direction){
        return new ResponseEntity<>(timeSlotsService.getAllTimeSlots(page,size,sortBy,direction), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetTimeSlotsResponse> getTimeSlotsById(@PathVariable Long id) {
        return new ResponseEntity<>(timeSlotsService.getTimeSlotsById(id), HttpStatus.OK);
    }

    @PostMapping("/time-slots")
    public ResponseEntity<CreateTimeSlotsResponse> createTimeSlots(@Valid @RequestBody CreateTimeSlotsRequest createTimeSlotsRequest) {

        return new ResponseEntity<>(timeSlotsService.createTimeSlots(createTimeSlotsRequest), HttpStatus.CREATED);

    }

    @PutMapping("/update")
    public ResponseEntity<UpdateTimeSlotsResponse> updateTimeSlots(@Valid @RequestBody UpdateTimeSlotsRequest updateTimeSlotsRequest) {
        return new ResponseEntity<>(timeSlotsService.updateTimeSlots(updateTimeSlotsRequest), HttpStatus.OK);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTimeSlots(@PathVariable Long id) {
        timeSlotsService.deleteTimeSlots(id);
        return new ResponseEntity<>("Appointment is deleted", HttpStatus.NO_CONTENT);
    }
    @PostMapping("/generate")
    public ResponseEntity<String>generateTImeSlots(@Valid @RequestBody GenerateTimeSlotsRequest request){
        timeSlotsService.generateTimeSlots(request);
        return new ResponseEntity<>("time slots generated",HttpStatus.CREATED);
    }
    @GetMapping("/available/{doctorId}")
    public ResponseEntity<List<GetTimeSlotsResponse>> getAvailableTimeSlots(@PathVariable Long doctorId){
        return new ResponseEntity<>(timeSlotsService.getAvailableTimeSlots(doctorId),HttpStatus.OK);
    }
}
