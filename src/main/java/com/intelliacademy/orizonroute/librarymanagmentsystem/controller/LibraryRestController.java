package com.intelliacademy.orizonroute.librarymanagmentsystem.controller;

import com.intelliacademy.orizonroute.librarymanagmentsystem.common.ApiMessages;
import com.intelliacademy.orizonroute.librarymanagmentsystem.dto.AuthorDTO;
import com.intelliacademy.orizonroute.librarymanagmentsystem.dto.BookDTO;
import com.intelliacademy.orizonroute.librarymanagmentsystem.dto.CategoryDTO;
import com.intelliacademy.orizonroute.librarymanagmentsystem.service.AuthorService;
import com.intelliacademy.orizonroute.librarymanagmentsystem.service.BookService;
import com.intelliacademy.orizonroute.librarymanagmentsystem.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/library")
@RequiredArgsConstructor
public class LibraryRestController {

    private final AuthorService authorService;
    private final BookService bookService;
    private final CategoryService categoryService;

    @GetMapping("/categories/{categoryId}/books")
    @Operation(summary = ApiMessages.Book.GET_BOOKS_BY_CATEGORY)
    public ResponseEntity<List<BookDTO>> getBooksByCategory(@PathVariable Long categoryId) {
        List<BookDTO> books = bookService.getBooksByCategory(categoryId);
        return ResponseEntity.ok(books);
    }

    @GetMapping("/books/{id}")
    @Operation(summary = ApiMessages.Book.GET_BOOK_BY_ID)
    public ResponseEntity<BookDTO> getBookById(@PathVariable Long id) {
        BookDTO book = bookService.getBookById(id);
        return ResponseEntity.ok(book);
    }

    @GetMapping("/authors")
    @Operation(summary = ApiMessages.Author.GET_ALL_AUTHORS)
    public ResponseEntity<List<AuthorDTO>> getAllAuthors(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AuthorDTO> authors = authorService.getAuthors(pageable);
        return ResponseEntity.ok(authors.getContent());
    }

    @GetMapping("/categories")
    @Operation(summary = ApiMessages.Category.GET_ALL_CATEGORIES)
    public ResponseEntity<List<CategoryDTO>> getAllCategories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<CategoryDTO> categories = categoryService.getCategories(pageable);
        return ResponseEntity.ok(categories.getContent());
    }
}
