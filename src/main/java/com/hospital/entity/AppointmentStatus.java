package com.hospital.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import lombok.experimental.Accessors;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class AppointmentStatus {
    @Id
    private Long id;
    private String nameEn;
    private String nameAr;
}
