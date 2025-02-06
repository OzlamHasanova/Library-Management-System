package com.intelliacademy.orizonroute.librarymanagmentsystem.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class BookDTO {
    private Long id;
    private String title;
    private String isbn;
    private int publicationYear;
    private String description;
    private String availability;
    private String image;
    private Long stock;
    private Set<Long> authorIds;
    private Set<String> authorNames = new HashSet<>();
    private Long categoryId;
    private String categoryName;
}
