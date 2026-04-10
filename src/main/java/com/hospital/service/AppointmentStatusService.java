package com.hospital.service;

import com.hospital.dto.StatusResponseDto;
import com.hospital.entity.AppointmentStatus;
import com.hospital.exception.HospitalBusinessException;
import com.hospital.repository.AppointmentStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
      appointmentStatus1.setId(appointmentStatus.getId())
              .setName_ar(appointmentStatus.getNameAr())
              .setName_en(appointmentStatus.getNameEn());
      statusResponseDtos.add(appointmentStatus1);
    });
    return statusResponseDtos;
    }
}
