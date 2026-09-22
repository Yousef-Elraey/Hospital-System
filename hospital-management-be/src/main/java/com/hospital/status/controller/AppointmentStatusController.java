package com.hospital.status.controller;

import com.hospital.dto.PageResponse;
import com.hospital.status.dto.request.SearchAppointmentStatusRequest;
import com.hospital.status.dto.response.StatusResponseDto;
import com.hospital.status.service.AppointmentStatusService;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/status")
@RequiredArgsConstructor
public class AppointmentStatusController {
private final AppointmentStatusService appointmentStatusService;

    @GetMapping("/statuses")
    public ResponseEntity<PageResponse<StatusResponseDto>> getAllStatus(@ParameterObject SearchAppointmentStatusRequest searchAppointmentStatusRequest,
                                                                        @RequestParam(defaultValue = "0")int page,
                                                                        @RequestParam(defaultValue = "10")int size,
                                                                        @RequestParam(defaultValue = "id") String sortBy,
                                                                        @RequestParam(defaultValue = "asc") String direction){
    return new ResponseEntity<>(appointmentStatusService.getAllStatus(searchAppointmentStatusRequest,page,size,sortBy,direction), HttpStatus.OK);
}

}
