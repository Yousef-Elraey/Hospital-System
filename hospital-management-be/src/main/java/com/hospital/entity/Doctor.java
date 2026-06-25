package com.hospital.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;


@Entity
@Table(name = "doctor")
@Setter
@Getter
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class Doctor extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(name = "contact_number", nullable = false)
    private String contactNumber;
    @ManyToOne
    @JoinColumn(name = "speciality_id", referencedColumnName = "id")
    private Speciality speciality;


    @Override
    public String toString() {
        return "Doctor{" +
                "name='" + name + '\'' +
                ", contactNumber='" + contactNumber + '\'' +
                ", speciality=" + speciality +
                "} " + super.toString();
    }
}

