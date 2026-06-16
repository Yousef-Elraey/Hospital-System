package com.hospital.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Entity
@Setter
@Getter
@Accessors(chain = true)
@Table(name = "treatment")
@AllArgsConstructor
@NoArgsConstructor
public class Treatment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name_en;

    @Column(nullable = false)
    private String name_ar;

    @Column(nullable = false)
    private String activeIngredient;
}
