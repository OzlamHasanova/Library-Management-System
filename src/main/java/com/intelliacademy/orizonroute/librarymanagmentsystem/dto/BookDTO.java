package com.intelliacademy.orizonroute.librarymanagmentsystem.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookDTO {
    private Long id;
    private String title;
    private String isbn;
    private int publicationYear;
    private String description;
    private String image;
    private Long stock;
}
