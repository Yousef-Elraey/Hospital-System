package com.hospital.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class Billing extends BaseEntity{

    private Long amount;

    @OneToOne
    @JoinColumn(name = "patient_id",referencedColumnName = "id")
    private Patient patient;

    @Override
    public String toString() {
        return "Billing{" +
                "amount=" + amount +
                ", patient=" + patient +
                "} " + super.toString();
    }
}
