package com.intelliacademy.orizonroute.librarymanagmentsystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String surname;
    @Column(unique = true)
    private String sif;
    private boolean isDeleted = false;

    public Student(long l, String john, String doe, String number) {
    }
}
