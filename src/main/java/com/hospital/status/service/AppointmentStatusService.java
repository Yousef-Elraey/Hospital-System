package com.hospital.status.service;

import com.hospital.status.dto.response.StatusResponseDto;
import com.hospital.entity.AppointmentStatus;
import com.hospital.common.exception.HospitalBusinessException;
import com.hospital.status.repository.AppointmentStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentStatusService {
  private final AppointmentStatusRepository appointmentStatusRepository;

  public List<StatusResponseDto> getAllStatus() {
    List<StatusResponseDto> statusResponseDtos = new ArrayList<>();
    List<AppointmentStatus> appointmentStatusDb = appointmentStatusRepository.findAll();
    if (appointmentStatusDb.isEmpty()){
      throw new HospitalBusinessException("no status found");
    }
    appointmentStatusDb.forEach(appointmentStatus -> {
      StatusResponseDto appointmentStatus1 = new StatusResponseDto();
      appointmentStatus1
              .setName_ar(appointmentStatus.getNameAr())
              .setName_en(appointmentStatus.getNameEn())
              .setId(appointmentStatus.getId());
      statusResponseDtos.add(appointmentStatus1);
    });
    return statusResponseDtos;
    }
}
