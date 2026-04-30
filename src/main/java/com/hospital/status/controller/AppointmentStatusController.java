package com.hospital.status.controller;

import com.hospital.status.dto.response.StatusResponseDto;
import com.hospital.status.service.AppointmentStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/status")
@RequiredArgsConstructor
public class AppointmentStatusController {
private final AppointmentStatusService appointmentStatusService;

    @GetMapping("/")
    public ResponseEntity<List<StatusResponseDto>> getAllStatus(){
    return new ResponseEntity<>(appointmentStatusService.getAllStatus(), HttpStatus.OK);
}

}
