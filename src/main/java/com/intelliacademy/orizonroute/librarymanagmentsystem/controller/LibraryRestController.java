package com.intelliacademy.orizonroute.librarymanagmentsystem.controller;

import com.intelliacademy.orizonroute.librarymanagmentsystem.dto.AuthorDTO;
import com.intelliacademy.orizonroute.librarymanagmentsystem.dto.BookDTO;
import com.intelliacademy.orizonroute.librarymanagmentsystem.dto.CategoryDTO;
import com.intelliacademy.orizonroute.librarymanagmentsystem.service.AuthorService;
import com.intelliacademy.orizonroute.librarymanagmentsystem.service.BookService;
import com.intelliacademy.orizonroute.librarymanagmentsystem.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/library")
@RequiredArgsConstructor
public class LibraryRestController {

    private final AuthorService authorService;
    private final BookService bookService;
    private final CategoryService categoryService;

    @GetMapping("/books/category/{categoryId}")
    public List<BookDTO> getBooksByCategory(@PathVariable Long categoryId) {
        return bookService.getBooksByCategory(categoryId);
    }

    @GetMapping("/books/{id}")
    public BookDTO getBookById(@PathVariable Long id) {
        return bookService.getBookById(id);
    }

    @GetMapping("/categories")
    public List<CategoryDTO> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/authors")
    public List<AuthorDTO> getAllAuthors() {
        return authorService.getAllAuthors();
    }
}

