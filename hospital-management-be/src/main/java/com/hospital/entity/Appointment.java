package com.hospital.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class Appointment extends BaseEntity{

    private LocalDateTime timing;
    @Enumerated(EnumType.STRING)
    private AppointmentType appointmentType;

    @ManyToOne
    @JoinColumn(name = "patient_id", referencedColumnName = "id")
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "doctor_id", referencedColumnName = "id")
    private Doctor doctor;

    @ManyToOne
    @JoinColumn(name = "status_id", referencedColumnName = "id")
    private AppointmentStatus status;

    @Override
    public String toString() {
        return "Appointment{" +
                "timing=" + timing +
                ", appointmentType=" + appointmentType +
                ", patient=" + patient +
                ", doctor=" + doctor +
                ", status=" + status +
                "} " + super.toString();
    }
}
