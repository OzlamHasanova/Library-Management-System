package com.intelliacademy.orizonroute.librarymanagmentsystem.model;


import com.intelliacademy.orizonroute.librarymanagmentsystem.model.enums.BookAvailability;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "books")
@Getter
@Setter
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String isbn;

    private int publicationYear;

    @Column(length = 500)
    private String description;

    private String image;

    private Long stock;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookAvailability availability = BookAvailability.AVAILABLE;

    @Column(nullable = false)
    private boolean isDeleted = false;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinTable(
            name = "book_author",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    private Set<Author> authors = new HashSet<>();


    @ManyToOne
    @JoinColumn(
            name = "category_id",
           nullable = false
    )
    private Category category;

}

