package com.hospital.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Setter
@Getter
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "time_slots")
@Builder
public class TimeSlots {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "doctor_id", referencedColumnName = "id")
    private Doctor doctor;
    @Enumerated(value = EnumType.STRING)
    private AppointmentType appointmentType;
    private LocalDate day;
    private LocalTime start;
    private LocalTime end;
    @Enumerated(value = EnumType.STRING)
    @Builder.Default
    private TimeSlotsStatus status = TimeSlotsStatus.AVAILABLE;

}
