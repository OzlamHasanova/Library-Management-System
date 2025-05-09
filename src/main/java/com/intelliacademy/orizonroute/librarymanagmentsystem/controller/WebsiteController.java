package com.intelliacademy.orizonroute.librarymanagmentsystem.controller;

import com.intelliacademy.orizonroute.librarymanagmentsystem.dto.BookDTO;
import com.intelliacademy.orizonroute.librarymanagmentsystem.service.BookService;
import com.intelliacademy.orizonroute.librarymanagmentsystem.service.CategoryService;
import com.intelliacademy.orizonroute.librarymanagmentsystem.service.impl.BookServiceImpl;
import com.intelliacademy.orizonroute.librarymanagmentsystem.service.impl.CategoryServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/website")
public class WebsiteController {
    private final BookService bookService;
    private final CategoryService categoryService;


    @GetMapping("/books")
    public String showAllBooks(@RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size,
                               @RequestParam(defaultValue = "title") String sortBy,
                               Model model) {
        Page<BookDTO> books = bookService.getAllBooks(page, size, sortBy);
        model.addAttribute("books", books.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", books.getTotalPages());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "website/books";
    }


    @GetMapping("/books/category/{id}")
    public String showBooksByCategory(@PathVariable Long id, Model model) {
        model.addAttribute("books", bookService.getBooksByCategory(id));
        model.addAttribute("categories", categoryService.getAllCategories());
        return "website/books";
    }

    @GetMapping("/book/{id}")
    public String showBookDetails(@PathVariable Long id, Model model) {
        model.addAttribute("book", bookService.getBookById(id));
        return "website/book-details";
    }
}

