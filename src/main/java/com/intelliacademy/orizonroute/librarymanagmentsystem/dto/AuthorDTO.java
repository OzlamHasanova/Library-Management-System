package com.intelliacademy.orizonroute.librarymanagmentsystem.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthorDTO {
    private Long id;
    private String fullName;
    private LocalDate birthDate;
    private LocalDate deathDate;
    private String biography;

}
