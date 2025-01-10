package com.intelliacademy.orizonroute.librarymanagmentsystem.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class AuthorDTO {
    private Long id;
    private String fullName;
    private LocalDate birthDate;
    private LocalDate deathDate;
}
