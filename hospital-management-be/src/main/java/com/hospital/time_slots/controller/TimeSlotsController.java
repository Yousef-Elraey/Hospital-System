package com.hospital.time_slots.controller;

import com.hospital.dto.PageResponse;

import com.hospital.time_slots.dto.request.CreateTimeSlotsRequest;
import com.hospital.time_slots.dto.request.GenerateTimeSlotsRequest;
import com.hospital.time_slots.dto.request.SearchTimeSlotsRequest;
import com.hospital.time_slots.dto.request.UpdateTimeSlotsRequest;
import com.hospital.time_slots.dto.response.CreateTimeSlotsResponse;
import com.hospital.time_slots.dto.response.GetTimeSlotsResponse;
import com.hospital.time_slots.dto.response.SearchTimeSlotsResponse;
import com.hospital.time_slots.dto.response.UpdateTimeSlotsResponse;
import com.hospital.time_slots.service.TimeSlotsService;
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
    public ResponseEntity<String> generateTImeSlots(@Valid @RequestBody GenerateTimeSlotsRequest request) {
        timeSlotsService.generateTimeSlots(request);
        return new ResponseEntity<>("time slots generated", HttpStatus.CREATED);
    }

    @GetMapping("/available/{doctorId}")
    public ResponseEntity<List<GetTimeSlotsResponse>> getAvailableTimeSlots(@PathVariable Long doctorId) {
        return new ResponseEntity<>(timeSlotsService.getAvailableTimeSlots(doctorId), HttpStatus.OK);
    }

    @PostMapping("/search")
    public ResponseEntity<PageResponse<SearchTimeSlotsResponse>> searchTimeSlots(@RequestBody SearchTimeSlotsRequest searchTimeSlotsRequest,
                                                                                 @RequestParam(defaultValue = "0") int page,
                                                                                 @RequestParam(defaultValue = "10") int size,
                                                                                 @RequestParam(defaultValue = "id") String sortBy,
                                                                                 @RequestParam(defaultValue = "asc") String direction) {
        return new ResponseEntity<>(timeSlotsService.searchTimeSlots(page, size, sortBy, direction, searchTimeSlotsRequest), HttpStatus.OK);
    }
}
