package com.hospital.service;

import com.hospital.dto.StatusResponseDto;
import com.hospital.entity.AppointmentStatus;
import com.hospital.repository.AppointmentStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AppointmentStatusService {
  @Autowired
  AppointmentStatusRepository appointmentStatusRepository;

  public List<StatusResponseDto> getAllStatus() {
    List<StatusResponseDto> statusResponseDtos = new ArrayList<>();
    List<AppointmentStatus> appointmentStatusDb = appointmentStatusRepository.findAll();
    appointmentStatusDb.stream().forEach(appointmentStatus -> {
      StatusResponseDto appointmentStatus1 = new StatusResponseDto();
      appointmentStatus1.setId(appointmentStatus.getId())
              .setName_ar(appointmentStatus.getNameAr())
              .setName_en(appointmentStatus.getNameEn());
      statusResponseDtos.add(appointmentStatus1);
    });
    return statusResponseDtos;
    }
}
