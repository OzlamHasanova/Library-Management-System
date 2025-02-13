package com.intelliacademy.orizonroute.librarymanagmentsystem.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentDTO {
    private Long id;
    private String name;
    private String surname;
    private String fullName;
    private String sif;

    public String getFullName() {
        return name + " " + surname;
    }
}
