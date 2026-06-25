package com.hospital.entity;



import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Fetch;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "patient")
@Setter
@Getter
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class Patient extends BaseEntity{

     @Column(nullable = false)
     private String name;
     @Column(nullable = false)
     @Enumerated(EnumType.STRING)
     private Gender gender;
     @Column(nullable = false)
     private String phone;
     @Column(nullable = false)
     private LocalDate dateOfBirth;
    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
     private List<MedicalRecord> medicalRecords;


    @Override
    public String toString() {
        return "Patient{" +
                "name='" + name + '\'' +
                ", gender=" + gender +
                ", phone='" + phone + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", medicalRecords=" + medicalRecords +
                "} " + super.toString();
    }
}
