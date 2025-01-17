package com.intelliacademy.orizonroute.librarymanagmentsystem.controller;

import com.intelliacademy.orizonroute.librarymanagmentsystem.dto.BookDTO;
import com.intelliacademy.orizonroute.librarymanagmentsystem.exception.BookNotFoundException;
import com.intelliacademy.orizonroute.librarymanagmentsystem.model.Book;
import com.intelliacademy.orizonroute.librarymanagmentsystem.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping
    public String listBooks(Model model) {
        List<Book> books = bookService.getAllBooks();
        model.addAttribute("books", books);
        return "books/list";
    }

    @GetMapping("/new")
    public String showCreateBookForm(Model model) {
        model.addAttribute("book", new BookDTO());
        return "books/create";
    }

    @PostMapping("/new")
    public String createBook(@ModelAttribute("book") BookDTO bookDTO) {
        bookService.createBook(bookDTO);
        return "redirect:/books";
    }

    @GetMapping("/{id}")
    public String viewBook(@PathVariable Long id, Model model) {
        try {
            BookDTO book = bookService.getBookById(id);
            model.addAttribute("book", book);
            return "books/view";
        } catch (BookNotFoundException e) {
            return "error/404";
        }
    }

    @GetMapping("/{id}/edit")
    public String showUpdateBookForm(@PathVariable Long id, Model model) {
        try {
            BookDTO book = bookService.getBookById(id);
            model.addAttribute("book", book);
            return "books/update";
        } catch (BookNotFoundException e) {
            return "error/404";
        }
    }

    @PostMapping("/{id}")
    public String updateBook(@PathVariable Long id, @ModelAttribute("book") BookDTO bookDTO) {
        try {
            bookService.updateBook(id, bookDTO);
            return "redirect:/books";
        } catch (BookNotFoundException e) {
            return "error/404";
        }
    }

    @PostMapping("/{id}/delete")
    public String deleteBook(@PathVariable Long id) {
        try {
            bookService.deleteBook(id);
            return "redirect:/books";
        } catch (BookNotFoundException e) {
            return "error/404";
        }
    }

}
